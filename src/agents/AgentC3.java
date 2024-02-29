package agents;

import java.util.ArrayList;
import java.util.List;

import game.Game;

/**
 * AgentC3 is responsible for making decisions in a game environment based on
 * probabilistic analysis of game states. It collaborates with AgentA and AgentB
 * to determine the most strategic moves based on the current state of the game
 * and predefined heuristics.
 */
public class AgentC3 {
    private Game game;
    private AgentB agentB;
    private AgentA agentA;
    private List<Cell> markedCells = new ArrayList<>();

    /**
     * Constructs a new AgentC3 with a specific game instance.
     *
     * @param game The game instance to be associated with this agent.
     */
    public AgentC3(Game game) {
        this.game = game;
        this.agentA = new AgentA(game);
        this.agentB = new AgentB(game);
    }

    /**
     * Creates a copy of the game board.
     *
     * @param original The original game board to be copied.
     * @return A new copy of the original game board.
     */
    private int[][] cloneBoard(int[][] original) {
        int N = game.getSize();
        int[][] copy = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, N);
        }
        return copy;
    }

    /**
     * Identifies and collects all unmarked cells within the current game state.
     *
     * @param game The game instance from which to find unmarked cells.
     * @return A list of Cell objects representing all unmarked cells in the game.
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
     * Checks if a given assignment to a list of cells (frontier) is valid based on
     * the current game state and the specific cell being considered. It temporarily
     * applies the assignment to validate the game state and reverts changes
     * afterward.
     *
     * @param frontier    The list of cells considered for assignment.
     * @param assignment  The proposed assignment for each cell in the frontier.
     * @param boardState  The current state of the game board.
     * @param currentCell The specific cell under consideration.
     * @return true if the assignment is valid; false otherwise.
     */
    public boolean isValidAssignment(List<Cell> frontier,
            int[] assignment,
            int[][] boardState,
            Cell currentCell) {
        // Temporarily apply the assignment
        for (int i = 0; i < frontier.size(); i++) {
            Cell cell = frontier.get(i);
            boardState[cell.row][cell.col] = assignment[i];
        }

        // Check validity
        boolean valid = true;
        for (int i = 0; i < frontier.size(); i++) {
            Cell cell = frontier.get(i);
            if (!isValid(cell.row, cell.col, boardState)) {
                valid = false;
                break;
            }
        }
        for (Cell cell : markedCells) {
            if (!isValid(cell.row, cell.col, boardState)) {
                valid = false;
                break;
            }
        }
        if (!isValid(currentCell.row, currentCell.col, boardState)) {
            valid = false;
        }
        // Revert state
        for (Cell cell : frontier) {
            boardState[cell.row][cell.col] = 0;
        }
        return valid;
    }

    /**
     * Overloaded method of isValidAssignment to check validity up to a given index
     * in the frontier.
     *
     * @param frontier    The list of cells considered for assignment.
     * @param assignment  The proposed assignment for each cell in the frontier.
     * @param boardState  The current state of the game board.
     * @param currentCell The specific cell under consideration.
     * @param index       The index up to which the assignment should be checked.
     * @return true if the partial assignment is valid; false otherwise.
     */
    public boolean isValidAssignment(List<Cell> frontier,
            int[] assignment,
            int[][] boardState,
            Cell currentCell,
            int index) {

        // Temporarily apply the assignment
        for (int i = 0; i < index; i++) {
            Cell cell = frontier.get(i);
            boardState[cell.row][cell.col] = assignment[i];
        }

        // Check validity
        boolean valid = true;
        for (int i = 0; i < index; i++) {
            Cell cell = frontier.get(i);
            if (!isValid(cell.row, cell.col, boardState)) {
                valid = false;
                break;
            }
        }
        for (Cell cell : markedCells) {
            if (!isValid(cell.row, cell.col, boardState)) {
                valid = false;
                break;
            }
        }
        if (!isValid(currentCell.row, currentCell.col, boardState)) {
            valid = false;
        }
        for (Cell cell : frontier) {
            boardState[cell.row][cell.col] = 0;
        }
        return valid;
    }

    /**
     * Determines if the current game state around a specified cell adheres to the
     * game rules.
     *
     * @param row        The row index of the cell to check.
     * @param col        The column index of the cell to check.
     * @param boardState The current state of the game board.
     * @return true if the cell's state is valid according to the game rules; false
     *         otherwise.
     */
    private boolean isValid(int row, int col, int[][] boardState) {
        int clue = game.getBoard()[row][col];
        int n = numNeighbors(row, col) - clue;
        if (clue == -1)
            return true;

        int N = game.getSize();
        int paintedCount = 0;
        int clearedCount = 0;
        for (int r = Math.max(0, row - 1); r < Math.min(N, row + 2); r++) {
            for (int c = Math.max(0, col - 1); c < Math.min(N, col + 2); c++) {
                if (boardState[r][c] == 1) {
                    paintedCount++; // Painted
                }
                if (boardState[r][c] == 2) {
                    clearedCount++;
                }
            }
        }
        return (paintedCount <= clue) && (clearedCount <= n);
    }

    /**
     * Calculates the number of neighboring cells around a given cell, including
     * diagonals.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return The number of neighboring cells around the specified cell.
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
     * Generates all possible valid combinations of assignments for the cells in the
     * frontier, considering a specific cell's impact on the game state.
     *
     * @param boardState The current state of the game board.
     * @param cell       The specific cell that influences the generation of
     *                   combinations.
     * @return A list of integer arrays, where each array represents a valid
     *         combination of assignments for the cells in the frontier.
     */
    public List<int[]> generateCombinations(int[][] boardState, Cell cell) {
        List<int[]> solutions = new ArrayList<>();
        List<Cell> frontier = new ArrayList<>();
        int N = game.getSize();

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (boardState[r][c] == 0)
                    frontier.add(new Cell(r, c));
            }
        }
        generate(solutions, frontier, new int[frontier.size()], 0, boardState, cell);
        return solutions;
    }

    /**
     * Recursively generates valid combinations of assignments for the frontier
     * cells up to a given index. Validates assignments at each step to ensure
     * compliance with game rules.
     *
     * @param solutions  The list of solutions found so far.
     * @param frontier   The list of cells considered for assignment.
     * @param current    The current assignment being considered.
     * @param index      The current index in the frontier being processed.
     * @param boardState The current state of the game board for validation.
     * @param cell       The specific cell influencing the generation process.
     */
    private void generate(List<int[]> solutions,
            List<Cell> frontier,
            int[] current,
            int index,
            int[][] boardState,
            Cell cell) {
        if (!isValidAssignment(frontier, current, boardState, cell, index)) {
            return;
        }
        if (index == frontier.size()) {
            if (isValidAssignment(frontier, current, boardState, cell)) {
                solutions.add(current.clone());
            }
            return;
        }

        for (int stateToTry : new int[] { 1, 2 }) { // Try painted and cleared
            current[index] = stateToTry; // Assign state
            generate(solutions, frontier, current, index + 1, boardState, cell);
        }
    }

    /**
     * Calculates the probability of a cell being painted based on the current game
     * state and the potential impact of marking the cell as painted or cleared.
     *
     * @param cell The cell for which the probability is being calculated.
     * @return The calculated probability of the cell being painted.
     */
    private double calcProb(Cell cell) {
        int[][] gameClone = cloneBoard(game.getState());
        gameClone[cell.row][cell.col] = 1;
        List<int[]> frontierAssignmentsPainted = generateCombinations(gameClone, cell);
        gameClone[cell.row][cell.col] = 2;
        List<int[]> frontierAssignmentsCleared = generateCombinations(gameClone, cell);

        double probTrue = 0.25 * frontierAssignmentProbs(frontierAssignmentsPainted);
        double probFalse = 0.75 * frontierAssignmentProbs(frontierAssignmentsCleared);

        double alpha = 1 / (probFalse + probTrue);

        return alpha * probTrue;
    }

    /**
     * Computes the combined probability of a set of assignments to the frontier
     * cells being valid, based on predefined probabilities for cells being painted
     * or cleared.
     *
     * @param frontierAssignments A list of assignments to the frontier cells.
     * @return The combined probability of the frontier assignments being valid.
     */
    private double frontierAssignmentProbs(List<int[]> frontierAssignments) {
        double finalProbability = 0;
        for (int[] assignment : frontierAssignments) {
            double probability = 1;
            for (int p : assignment) {
                if (p == 1) {
                    probability *= 0.25;
                } else {
                    probability *= 0.75;
                }
            }
            finalProbability += probability;
        }
        return finalProbability;
    }

    /**
     * Orchestrates the gameplay for AgentC3, integrating strategies from AgentA and
     * AgentB, and making decisions based on probabilistic analysis of the game state.
     *
     * @return The final score or outcome achieved by AgentC3 in the game.
     */
    public int playC3() {
        while (!agentA.checkCompletion()) {
            // loop over every unmarked cell
            double maxProb = 0;
            double minProb = 1;
            Cell maxProbCell = new Cell(-1, -1);
            Cell minProbCell = new Cell(-1, -1);
            List<Cell> unmarkedCells = getUnmarkedCells(game);

            agentB.playB();
            for (Cell unmarkedCell : unmarkedCells) {
                double probability = calcProb(unmarkedCell);
                if (probability > maxProb) {
                    maxProb = probability;
                    maxProbCell = unmarkedCell;
                }
                if (probability < minProb) {
                    minProb = probability;
                    minProbCell = unmarkedCell;
                }
            }
            if (minProb < (1 - maxProb)) {
                game.setState(minProbCell.row, minProbCell.col, 2);
                markedCells.add(minProbCell);
            } else {
                game.setState(maxProbCell.row, maxProbCell.col, 1);
                markedCells.add(maxProbCell);
            }
        }
        return agentA.playA();
    }
}
