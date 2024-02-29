package agents;
import game.Game;

/**
 * Represents an agent (AgentB) that follows a single-point strategy to interact
 * with a game. This agent utilizes AgentA for decision-making and applies
 * changes to the game state based on certain conditions.
 */
public class AgentB {
    private Game game;
    private AgentA agentA;
    private Boolean moveMade;

    /**
     * Constructs an AgentB with a reference to the game.
     * 
     * @param game The game instance this agent will interact with.
     */
    public AgentB(Game game) {
        this.game = game;
        this.agentA = new AgentA(game);
    }

    /**
     * Checks the entire game board to apply the single-point strategy. Iterates
     * over the game board, using AgentA to gather information about each cell and
     * its neighbors, then decides whether to paint or clear cells based on the
     * gathered information.
     */
    public void checkBoard() {
        // iterate over whole board
        // Check if indices are within bounds
        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                // at square, is it painted, cleared, or covered
                if (r >= 0 && r < game.getSize() && c >= 0 && c < game.getSize()) {
                    if (game.getState()[r][c] == 0) {
                        // check inital cell
                        if (game.getState()[r][c] == 0) { // Check if the cell is covered
                            int cellClue = game.getBoard()[r][c];
                            if (cellClue != -1) { // If there's a clue at this cell
                                agentA.countCellsAround(r, c);
                            }
                            // iterate over neighbors
                            for (int r1 = r - 1; r1 <= r + 1; r1++) {
                                for (int c1 = c - 1; c1 <= c + 1; c1++) {
                                    if (r1 >= 0 && r1 < game.getSize() && c1 >= 0 && c1 < game.getSize()) {
                                        // get neighbor clue
                                        int neighborClue = game.getBoard()[r1][c1];
                                        if (neighborClue != -1) {
                                            // count num painted, covered, cleared around neighbor
                                            agentA.countCellsAround(r1, c1);
                                            // if the neighboring clue is the same as the num of painted cells,
                                            // clear all
                                            // the cells around it
                                            if (neighborClue == agentA.paintedCells) {
                                                for (int r2 = r1 - 1; r2 <= r1 + 1; r2++) {
                                                    for (int c2 = c1 - 1; c2 <= c1 + 1; c2++) {
                                                        if (r2 >= 0 && r2 < game.getSize() && c2 >= 0
                                                                && c2 < game.getSize()) {
                                                            if (game.getState()[r2][c2] == 0) { // covered cells
                                                                                                // only
                                                                clearCells(r2, c2);
                                                                moveMade = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (agentA.paintedCells + agentA.coveredCells == neighborClue) {
                                                for (int r2 = r1 - 1; r2 <= r1 + 1; r2++) {
                                                    for (int c2 = c1 - 1; c2 <= c1 + 1; c2++) {
                                                        if (r2 >= 0 && r2 < game.getSize() && c2 >= 0
                                                                && c2 < game.getSize()) {
                                                            if (game.getState()[r2][c2] == 0) { // covered cells
                                                                                                // only
                                                                paintCell(r2, c2);
                                                                moveMade = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Paints a specific cell on the game board.
     * 
     * @param row The row index of the cell to paint.
     * @param col The column index of the cell to paint.
     */
    public void paintCell(int row, int col) {
        game.setState(row, col, 1);
    }

    /**
     * Clears a specific cell on the game board.
     * 
     * @param row The row index of the cell to clear.
     * @param col The column index of the cell to clear.
     */
    public void clearCells(int row, int col) {
        game.setState(row, col, 2);
    }

    /**
     * Executes the strategy of AgentB, making moves until no further moves can be
     * made, then evaluates the game state for completion and correctness.
     * 
     * @return An integer indicating the final status of the game after AgentB's
     *         moves:
     *         3 if the game is complete and correct,
     *         1 if the game is complete but not correct,
     *         2 if the game is not complete but correct so far,
     *         0 otherwise.
     */
    public int playB() {
        do {
            moveMade = false; // Reset before each checkBoard call
            checkBoard();
        } while (moveMade);

        int output = 0;

        boolean isComplete = agentA.checkCompletion();
        boolean isCorrect = agentA.checkCorrectness();

        if (isComplete && isCorrect) {
            output = 3;
        } else if (isComplete && !isCorrect) {
            output = 1;
        } else if (!isComplete && isCorrect) {
            output = 2;
        }
        return output;
    }

    /**
     * Returns the game instance associated with this agent.
     * 
     * @return The game instance.
     */
    public Game getGame() {
        return game;
    }
}