package agents;
import game.Game;

/**
 * Represents Agent A in the Mosaic game, extending the basic Game class
 * functionality. Agent A is responsible for checking the completion and
 * correctness of the game state based on the visible board and the cells'
 * states without accessing the game's solution. This agent serves as a
 * foundational component for understanding and interacting with the game's
 * mechanics, providing the groundwork for further agent development.
 */
public class AgentA {

    private Game game;
    public int paintedCells = 0;
    public int coveredCells = 0;
    public int clearedCells = 0;

    public AgentA(Game game) {
        this.game = game;
    }

    /**
     * Checks if the game is completed by verifying if all cells on the board have
     * been revealed. Iterates through the entire board, examining each cell's state
     * to determine if it remains unrevealed. An unrevealed cell is indicated by a
     * state of 0.
     *
     * @return {@code true} if all cells have been revealed, indicating the game is
     *         complete. Returns {@code false} if any cell remains unrevealed,
     *         indicating the game is not yet complete.
     */
    public boolean checkCompletion() {
        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                // Assuming state = 0 is unrevealed
                if (game.getState()[r][c] == 0) {
                    return false; // Game not complete
                }
            }
        }
        return true;
    }

    /**
     * Counts the number of painted cells surrounding a given cell. The search for
     * surrounding cells includes all adjacent cells in a 3x3 grid centered on the
     * given cell. Boundary checks are performed to avoid indexing outside the
     * board.
     *
     * @param row The row index of the cell to check surrounding painted cells.
     * @param col The column index of the cell to check surrounding painted cells.
     * @return The count of painted cells surrounding the given cell.
     */
    public void countCellsAround(int row, int col) {
        paintedCells = 0;
        coveredCells = 0;
        clearedCells = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Check if indices are within bounds
                if (r >= 0 && r < game.getSize() && c >= 0 && c < game.getSize()) {
                    if (game.getState()[r][c] == 1) {
                        paintedCells++;
                    } else if (game.getState()[r][c] == 0) {
                        coveredCells++;
                    } else if (game.getState()[r][c] == 2) {
                        clearedCells++;
                    }
                }
            }
        }
    }

    /**
     * Checks if the current state of the board adheres to the clues provided.
     * Iterates over each cell in the board, using the clues from the {@code board}
     * array and comparing them to the actual state of cells around each clue cell
     * from the {@code state} array. Assumes that a clue of -1 indicates no specific
     * clue for that cell, and only cells with valid clues are checked.
     *
     * @return {@code true} if all clues match the surrounding painted cells' count,
     *         {@code false} otherwise.
     */
    public boolean checkCorrectness() {
        boolean correct = true;
        // Iterate over each row and column to check clues against the state
        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                // If there's a clue (not -1), check the surrounding cells
                if (game.getBoard()[r][c] != -1) {
                    int clue = game.getBoard()[r][c];
                    countCellsAround(r, c);
                    if ((clue < paintedCells) || (coveredCells + paintedCells < clue)) {
                        correct = false;
                    }
                }
            }
        }
        return correct;
    }

    /**
     * Executes the strategy for playing the game, evaluating both the completion
     * and correctness of the current game state.
     *
     * This method checks if the game state meets specific conditions to determine
     * the outcome of the strategy applied. The outcome is represented as an integer
     * value, with different values indicating various states of the game:
     *
     * - 3 indicates that the game is both complete and correct according to the
     * game's rules.
     * - 2 indicates that the game is not complete, but the parts that have been
     * played are correct so far.
     * - 1 indicates that the game is complete but not correct, meaning there are
     * errors in the solution.
     * - 0 indicates that the game is neither complete nor correct, or that it
     * cannot be determined to be correct based on the current state.
     *
     * The method relies on two checks: `checkCompletion` and `checkCorrectness`,
     * which need to be implemented to assess the game state accurately.
     *
     * @return An integer code representing the outcome of the game's current state:
     *         - 3 for a game state that is complete and correct,
     *         - 2 for a game state that is incomplete but correct so far,
     *         - 1 for a game state that is complete but incorrect,
     *         - 0 for any other outcome.
     */
    public int playA() {
        int output = 0;

        boolean isComplete = checkCompletion();
        boolean isCorrect = checkCorrectness();

        if (isComplete && isCorrect) {
            output = 3;
        } else if (isComplete && !isCorrect) {
            output = 1;
        } else if (!isComplete && isCorrect) {
            output = 2;
        } else {
            output = 0;
        }

        return output;
    }
}
