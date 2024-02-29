package agents;

import java.util.ArrayList;
import java.util.List;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import game.Game;

/**
 * Represents a AgentC1 capable of determining the satisfiability of a game
 * state and recursively choosing new cells to mark based on that determination.
 * Utilizes strategies from both AgentA and AgentB, alongside logic programming
 * tools for satisfiability checking.
 */
public class AgentC1 {

    private Game game;
    private AgentB agentB;
    private AgentA agentA;
    private FormulaFactory f;

    /**
     * Constructs an AgentC1 with a reference to the game.
     * 
     * @param game The game instance this agent will interact with.
     */
    public AgentC1(Game game) {
        this.game = game;
        this.agentB = new AgentB(game);
        this.agentA = new AgentA(game);
        this.f = new FormulaFactory();
    }

    /**
     * Creates a knowledge base (KB) representing the current state of the game.
     * The KB is formulated as a string of logical clauses.
     * 
     * @param game The game instance from which to generate the KB.
     * @return A string representing the KB.
     */
    public String makeKB(Game game) {
        StringBuilder KBU = new StringBuilder();

        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getBoard()[r][c] != -1) {
                    String segment = makeClauses(r, c);
                    if (KBU.length() > 0) {
                        KBU.append(" & ");
                    }
                    KBU.append("(" + segment + ")");
                }
            }
        }
        KBU.append(makePaintedConst(game));
        KBU.append(makeClearedConst(game));
        return KBU.toString();
    }

    /**
     * Generates logical clauses representing the constraint that specific cells are
     * painted.
     * 
     * @param game The game instance from which to generate the painted cells
     *             constraint.
     * @return A string representing the logical constraint for painted cells.
     */
    private String makePaintedConst(Game game) {
        List<String> constraint = new ArrayList<>();

        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getState()[r][c] == 1) {
                    constraint.add("D" + r + c);
                }
            }
        }
        String paintedStr = String.join(" & ", constraint);
        if (paintedStr.length() > 0) {
            return " & (" + paintedStr + ")";
        } else {
            return paintedStr;
        }
    }

    /**
     * Generates logical clauses representing the constraint that specific cells are
     * cleared.
     * 
     * @param game The game instance from which to generate the cleared cells
     *             constraint.
     * @return A string representing the logical constraint for cleared cells.
     */
    private String makeClearedConst(Game game) {
        List<String> constraint = new ArrayList<>();

        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getState()[r][c] == 2) {
                    constraint.add("~D" + r + c);
                }
            }
        }
        String clearedStr = String.join(" & ", constraint);
        if (clearedStr.length() > 0) {
            return " & (" + clearedStr + ")";
        } else {
            return clearedStr;
        }
    }

    /**
     * Creates logical clauses for a given cell based on its clue and the state of
     * its neighboring cells.
     * 
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return A string representing the logical clauses for the cell.
     */
    public String makeClauses(int row, int col) {
        StringBuilder expression = new StringBuilder();
        int clue = game.getBoard()[row][col];
        // generate combos of clue neighbors
        List<Cell> neighbors = getNeighbors(row, col, game.getSize());
        List<List<Cell>> combos = generateCombinations(neighbors, clue);
        // for each combo make a subclause
        List<String> subClauses = new ArrayList<String>();
        for (List<Cell> combo : combos) {
            subClauses.add(getSubClause(neighbors, combo));
        }
        expression.append(String.join(" | ", subClauses));
        return expression.toString();
    }

    /**
     * Helper method to generate a subclause for a combination of cells.
     * 
     * @param neighbors The list of neighboring cells.
     * @param combo     The combination of cells for which to generate the
     *                  subclause.
     * @return A string representing the subclause.
     */
    private String getSubClause(List<Cell> neighbors, List<Cell> combo) {
        List<String> subClause = new ArrayList<>();
        String cellName;
        for (Cell neighbor : neighbors) {
            if (combo.contains(neighbor)) {
                cellName = "D" + neighbor.row + neighbor.col;
            } else {
                cellName = "~D" + neighbor.row + neighbor.col;
            }
            subClause.add(cellName);
        }
        return "(" + String.join(" & ", subClause) + ")";
    }

    /**
     * Generates a list of neighboring cells for a given cell, including the cell
     * itself.
     * 
     * @param row      The row index of the cell.
     * @param col      The column index of the cell.
     * @param gridSize The size of the game grid.
     * @return A list of Cell objects representing the neighbors.
     */
    public static List<Cell> getNeighbors(int row, int col, int gridSize) {
        List<Cell> neighbors = new ArrayList<>();
        // Define the range of neighbors; this example uses the immediate 8 surrounding
        // cells.
        int[] rowOffsets = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] colOffsets = { -1, 0, 1, -1, 1, -1, 0, 1 };
        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];
            // Check if the new position is within the grid bounds.
            if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize) {
                neighbors.add(new Cell(newRow, newCol));
            }
        }
        neighbors.add(new Cell(row, col));
        return neighbors;
    }

    /**
     * Main method for testing purposes, showcasing the generation and printing of
     * cell combinations.
     */
    public static void main(String[] args) {
        int gridSize = 3; // For a 3x3 grid.
        // Specify the cell for which to find the neighbors.
        int specificRow = 2;
        int specificCol = 1;

        // Get the neighbors for the specified cell.
        List<Cell> neighbors = getNeighbors(specificRow, specificCol, gridSize);

        // Generate all combinations of 4 out of these neighbors.
        List<List<Cell>> combinations = generateCombinations(neighbors, 4);
        printCombinations(combinations);
    }

    /**
     * Prints all combinations of cells generated from a list.
     * 
     * @param combinations The list of cell combinations to print.
     */
    public static void printCombinations(List<List<Cell>> combinations) {
        for (List<Cell> combination : combinations) {
            System.out.println(combination);
        }
        System.out.println(combinations.size());
    }

    /**
     * Recursively generates all combinations of a specified size from a list of
     * cells. This method implements a classic backtracking algorithm to explore all
     * potential combinations of cells up to the specified size (k). Combinations
     * are accumulated in a list that stores all valid combinations found during the
     * recursion.
     *
     * @param cells              The list of cells from which to generate
     *                           combinations. This list represents the entire set
     *                           of possible cells that can be included in each
     *                           combination.
     * @param k                  The target size of each combination to generate.
     *                           This value determines how many cells should be
     *                           included in each combination. AKA the clue.
     * @param start              The starting index in the list of cells for this
     *                           recursion step. This parameter helps in ensuring
     *                           that the combinations are generated without
     *                           repetition and that each cell is considered only
     *                           once for each specific position in the combination.
     * @param currentCombination A list that represents the current combination
     *                           being built as the recursion progresses. This list
     *                           is temporarily modified during the recursion to
     *                           explore different combinations.
     * @param allCombinations    A list of lists that accumulates all the valid
     *                           combinations found. Each valid combination is added
     *                           as a separate list of cells to this list. This
     *                           parameter effectively collects the output of the
     *                           backtracking algorithm.
     */
    private static void backtrack(List<Cell> cells, int k, int start, List<Cell> currentCombination,
            List<List<Cell>> allCombinations) {
        if (currentCombination.size() == k) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = start; i < cells.size(); i++) {
            currentCombination.add(cells.get(i));
            backtrack(cells, k, i + 1, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    /**
     * Generates all combinations of a specified size k from a list of cells.
     *
     * @param cells The list of cells from which to generate combinations.
     * @param k     The size of the combinations to generate.
     * @return A list of combinations, where each combination is a list of Cell
     *         objects.
     */

    public static List<List<Cell>> generateCombinations(List<Cell> cells, int k) {
        List<List<Cell>> allCombinations = new ArrayList<>();
        backtrack(cells, k, 0, new ArrayList<>(), allCombinations);
        return allCombinations;
    }

    /**
     * Executes the AgentC1 strategy, utilizing AgentB's strategy as a foundation,
     * and then making additional moves based on the satisfiability of the current
     * game state.
     *
     * @return An integer code representing the outcome of the strategy application:
     *         3 for a complete and correct solution,
     *         1 for a complete but incorrect solution,
     *         2 for an incomplete but so far correct solution,
     *         0 for any other outcome.
     */
    public int playC1() {
        return recursiveStrat();
    }

    /**
     * Attempts to find a satisfiable configuration of the game state through
     * recursive strategy. Marks and unmarks cells as part of the search for a
     * satisfiable solution.
     *
     * @return An integer representing the result of the recursive strategy.
     */
    private int recursiveStrat() {
        int output = 0;
        boolean isComplete = agentA.checkCompletion();
        boolean isCorrect = agentA.checkCorrectness();

        if (isComplete && isCorrect) {
            output = 3;
            return output;
        }
        String KBU = makeKB(game);

        if (isSatisfiable(KBU)) {
            List<Cell> unmarkedCells = getUnmarkedCells(game);
            agentB.playB();

            for (Cell cell : unmarkedCells) {
                // mark the cell as painted
                game.setState(cell.row, cell.col, 1);
                int result = recursiveStrat();
                // If the game state is satisfiable, continue to the next cell
                if (result == 3) {
                    return result;
                }
                // If not satisfiable or remaining cells can't lead to a solution, unmark and
                // backtrack
                game.setState(cell.row, cell.col, 2);
            }
        }
        return 0;
    }

    public int[][] copyGameState(int[][] state) {
        int[][] stateCopy = new int[state.length][];
        for (int i = 0; i < state.length; i++) {
            int[] aMatrix = state[i];
            int aLength = aMatrix.length;
            stateCopy[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, stateCopy[i], 0, aLength);
        }
        return stateCopy;
    }

    /**
     * Retrieves a list of all unmarked cells within the current game state.
     *
     * @param game The game instance from which to find unmarked cells.
     * @return A list of unmarked Cell objects.
     */
    private List<Cell> getUnmarkedCells(Game game) {
        List<Cell> unmarkedCells = new ArrayList<>();
        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getState()[r][c] == 0) {
                    unmarkedCells.add(new Cell(r, c));
                }
            }
        }
        return unmarkedCells;
    }

    /**
     * Checks if a given knowledge base (KB) string is satisfiable, indicating that
     * the current game state has a solution according to the constraints expressed
     * in the KB.
     *
     * @param KBU The knowledge base string representing the game state and
     *            constraints.
     * @return True if the KB is satisfiable, false otherwise.
     */
    private Boolean isSatisfiable(String KBU) {
        PropositionalParser p = new PropositionalParser(f);
        Formula formula;
        try {
            formula = p.parse(KBU);
            SATSolver miniSat = MiniSat.miniSat(f);
            miniSat.add(formula);
            Tristate result = miniSat.sat();
            return result == Tristate.TRUE;
        } catch (ParserException e) {
            System.out.println("Parser Error");
        }
        return false;
    }
}
