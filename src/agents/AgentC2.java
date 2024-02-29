package agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import game.Game;

/**
 * Represents an agent (AgentC2) that interacts with the game environment
 * and employs logical reasoning (SAT4J) to solve puzzles or make decisions
 * based
 * on the game state. It collaborates with other agents (AgentB) to achieve its
 * goals.
 */
public class AgentC2 {
    private Game game;
    private AgentB agentB;
    private AgentA agentA;

    /**
     * Constructs a new AgentC2 with a specific game instance.
     *
     * @param game The game instance to be associated with this agent.
     */
    public AgentC2(Game game) {
        this.game = game;
        this.agentA = new AgentA(game);
        this.agentB = new AgentB(game);
    }

    /**
     * Generates a knowledge base (KB) of constraints based on the current state of
     * the game.
     *
     * @param game The game instance from which to generate the knowledge base.
     * @return A list of integer arrays, where each array represents a constraint
     *         derived from the game's state.
     */
    public List<int[]> makeKB(Game game) {
        List<int[]> constraints = new ArrayList<>();

        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getBoard()[r][c] != -1) {
                    constraints.addAll(makeClueConst(r, c));
                }
            }
        }
        constraints.addAll(makeExistingConst());
        return constraints;
    }

    /**
     * Prints the constraints of the knowledge base to the standard output.
     *
     * @param constraints The list of constraints to be printed.
     */
    public static void printConstraints(List<int[]> constraints) {
        for (int[] constraint : constraints) {
            System.out.println(Arrays.toString(constraint));
        }
        System.out.println(constraints.size());
    }

    /**
     * A recursive strategy for solving the game by exploring different states and
     * backtracking if necessary.
     *
     * @return An integer indicating the result of the strategy execution.
     */
    private int recursiveStrat() {
        int output = 0;
        boolean isComplete = agentA.checkCompletion();
        boolean isCorrect = agentA.checkCorrectness();

        if (isComplete && isCorrect) {
            output = 3;
            return output;
        }

        List<int[]> KBU = makeKB(game);

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

    /**
     * Generates constraints based on a clue located at a specific cell in the game
     * board.
     *
     * @param row The row index of the cell containing the clue.
     * @param col The column index of the cell containing the clue.
     * @return A list of integer arrays representing the generated constraints.
     */
    public List<int[]> makeClueConst(int row, int col) {
        List<int[]> constraints = new ArrayList<>();
        List<Cell> unmarkedNeighbors = new ArrayList<>();
        int[] rowOffsets = { -1, -1, -1, 0, 0, 1, 1, 1, 0 };
        int[] colOffsets = { -1, 0, 1, -1, 1, -1, 0, 1, 0 };
        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];
            boolean inBounds = (newRow >= 0 && newRow < game.getSize() && newCol >= 0 && newCol < game.getSize());
            if (inBounds) {
                unmarkedNeighbors.add(new Cell(newRow, newCol));
            }
        }
        int clueNum = game.getBoard()[row][col];
        int k = clueNum;
        constraints.addAll(makePaintedConst(row, col, unmarkedNeighbors, k + 1));
        constraints.addAll(makeClearedConst(row, col, unmarkedNeighbors, numNeighbors(row, col) - k + 1));
        return constraints;
    }

    /**
     * Generates constraints for all existing marked cells in the game's state.
     *
     * @return A list of integer arrays representing the constraints for existing
     *         cell states.
     */
    private List<int[]> makeExistingConst() {
        List<int[]> constraints = new ArrayList<>();
        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                int name;
                if (game.getState()[r][c] == 1) {
                    name = r * 10 + c;
                    if (name == 0) {
                        name = 100;
                    }
                    int[] constraint = new int[] { name };
                    constraints.add(constraint);
                } else if (game.getState()[r][c] == 2) {
                    name = -(r * 10 + c);
                    if (name == 0) {
                        name = -100;
                    }
                    int[] constraint = new int[] { name };
                    constraints.add(constraint);
                }
            }
        }
        return constraints;
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
     * Calculates the number of neighboring cells around a given cell.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return The number of neighbors around the specified cell.
     */
    private int numNeighbors(int row, int col) {
        int numNeigh = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Check if indices are within bounds
                if (r >= 0 && r < game.getSize() && c >= 0 && c < game.getSize()) {
                    numNeigh++;
                }
            }
        }
        return numNeigh;
    }

    /**
     * Generates constraints that ensure a specific number of cells are painted
     * among a list of unmarked neighbors.
     *
     * @param row               The row index of the cell.
     * @param col               The column index of the cell.
     * @param unmarkedNeighbors A list of Cell objects representing unmarked
     *                          neighboring cells.
     * @param n                 The number of cells that must be painted.
     * @return A list of integer arrays representing the painted constraints.
     */
    private List<int[]> makePaintedConst(int row, int col, List<Cell> unmarkedNeighbors, int n) {
        List<int[]> constraints = new ArrayList<>();

        for (List<Cell> combo : generateCombinations(unmarkedNeighbors, n)) {
            int[] constraint = new int[combo.size()];
            for (int i = 0; i < combo.size(); i++) {
                int name = combo.get(i).row * 10 + combo.get(i).col;
                if (name == 0) {
                    name = 100;
                }
                constraint[i] = -name;
            }
            constraints.add(constraint);
        }
        return constraints;
    }

    /**
     * Generates constraints that ensure a specific number of cells are cleared
     * among a list of unmarked neighbors.
     *
     * @param row               The row index of the cell.
     * @param col               The column index of the cell.
     * @param unmarkedNeighbors A list of Cell objects representing unmarked
     *                          neighboring cells.
     * @param n                 The number of cells that must be cleared.
     * @return A list of integer arrays representing the cleared constraints.
     */
    private List<int[]> makeClearedConst(int row, int col, List<Cell> unmarkedNeighbors, int n) {
        List<int[]> constraints = new ArrayList<>();
        for (List<Cell> combo : generateCombinations(unmarkedNeighbors, n)) {
            int[] constraint = new int[combo.size()];
            for (int i = 0; i < combo.size(); i++) {
                int name = combo.get(i).row * 10 + combo.get(i).col;
                if (name == 0) {
                    name = 100;
                }
                constraint[i] = name;
            }
            constraints.add(constraint);
        }
        return constraints;
    }

    /**
     * A helper method used to generate all combinations of a specific size from a
     * list of cells. This method is utilized for generating constraint combinations
     * based on game
     * logic.
     *
     * @param cells The list of Cell objects to generate combinations from.
     * @param k     The size of each combination to be generated.
     * @return A list of lists, where each inner list is a combination of Cell
     *         objects.
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
     * Determines if a set of constraints is satisfiable using a SAT solver.
     * This method is crucial for verifying if the current game state or any
     * hypothesized states are logically consistent.
     *
     * @param constraints The list of constraints to be checked for satisfiability.
     * @return true if the constraints are satisfiable, false otherwise.
     */
    public static List<List<Cell>> generateCombinations(List<Cell> cells, int k) {
        List<List<Cell>> allCombinations = new ArrayList<>();
        backtrack(cells, k, 0, new ArrayList<>(), allCombinations);
        return allCombinations;
    }

    /**
     * Checks if a set of constraints can be satisfied within the context of a game
     * board. The method utilizes a SAT solver to determine the satisfiability of
     * the constraints. Each constraint is represented as an array of integers,
     * where each integer corresponds to a variable in the SAT problem. The method
     * configures the SAT solver with the total number of variables based on the
     * game board size and adds each constraint as a clause to the solver. If any
     * clause leads to a contradiction, it is caught and printed out. The
     * satisfiability of the constraints is then evaluated.
     *
     * @param constraints A list of constraints, where each constraint (int array)
     *                    represents a clause for the SAT solver. Positive numbers
     *                    represent the literal variables, while negative numbers
     *                    represent their negations.
     * @return true if the set of constraints is satisfiable, meaning there exists
     *         at least one assignment of values to variables that satisfies all
     *         constraints. Returns false if the constraints are unsatisfiable or if
     *         a timeout occurs during the satisfiability check.
     * @throws TimeoutException If the SAT solver exceeds a predefined time limit
     *                          while attempting to determine satisfiability. This
     *                          exception is caught within the method, and false is
     *                          returned as the result. The stack trace of the
     *                          exception is printed to standard error.
     */
    public boolean isSatisfiable(List<int[]> constraints) {
        int maxVar = game.getSize() * game.getSize();
        int numClauses = constraints.size();

        ISolver solver = SolverFactory.newDefault();
        solver.newVar(maxVar);
        solver.setExpectedNumberOfClauses(numClauses);
        for (int[] constraint : constraints) {
            try {
                if (constraint.length > 0) {
                    solver.addClause(new VecInt(constraint));
                }
            } catch (ContradictionException e) {
                System.out.println(constraint);
            }
        }
        IProblem problem = solver;
        try {
            return problem.isSatisfiable();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Initiates the agent's strategy for playing the game. This method wraps the
     * recursive strategy and could be used to initiate the game-solving process
     * externally.
     *
     * @return An integer indicating the outcome of the game based on the agent's
     *         strategy. The meaning of this value is determined by the
     *         implementation of recursiveStrat.
     */
    public int playC2() {
        return recursiveStrat();
    }
}
