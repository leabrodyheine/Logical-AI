package Testing;

import game.Game;
import agents.AgentB;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AgentBTest {
    private Game game;
    private AgentB agentB;

    @Before
    public void setUp() {
        game = new Game();
        agentB = new AgentB(game);
    }

    @Test
    public void testPaintCellBasedOnClue() {
        game = new Game(3);
        int[][] board = {
                { 2, 3, 2 },
                { 3, -1, 3 },
                { 2, 3, 2 }
        };

        game.setBoard(board); // Assuming setBoard directly sets clues
        // Initially, all cells are covered
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (r >= 0 && r < game.getSize() && c >= 0 && c < game.getSize()) {
                    game.setState(r, c, 0);
                }
            }
        }
        // Simulate a scenario that should lead AgentB to paint the central cell
        game.setState(0, 0, 1);
        game.setState(0, 1, 1);
        game.setState(0, 2, 1);
        game.setState(1, 0, 1);
        game.setState(1, 2, 1);
        game.setState(2, 0, 1);
        game.setState(2, 1, 1);
        game.setState(2, 2, 1);

        agentB.checkBoard();

        assertEquals("Expected the central cell to be painted based on clues", 0, game.getState()[1][1]);
    }

    @Test
    public void testClearCellWhenNoPaintingRequired() {
        game = new Game(3);
        int[][] board = {
                { 1, -1, 1 },
                { -1, 4, -1 },
                { 1, -1, 1 }
        };
        game.setBoard(board);
        // Initially, all cells are covered, except those directly needed for the test
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                game.setState(r, c, 0);
            }
        }
        // Paint cells around the clue that requires no additional painting
        game.setState(0, 0, 1);
        game.setState(0, 2, 1);
        game.setState(2, 0, 1);
        game.setState(2, 2, 1);

        agentB.checkBoard();

        assertEquals("Expected no additional cells to be painted", 0, game.getState()[1][1]);
    }

    @Test
    public void testNoMoveMadeWhenNoActionIsNecessary() {
        game.setSize(2);
        int[][] board = {
                { 1, 0 },
                { 0, 1 }
        };
        game.setBoard(board);
        // Set state to reflect the board meeting all clues without needing further
        // action
        game.setState(0, 0, 1);
        game.setState(0, 1, 2);
        game.setState(1, 0, 2);
        game.setState(1, 1, 1);

        agentB.checkBoard();

        // Since there's no direct boolean to check if a move was made, we infer it by
        // the lack of state change
        boolean noMoveMade = game.getState()[0][0] == 1 && game.getState()[0][1] == 2 &&
                game.getState()[1][0] == 2 && game.getState()[1][1] == 1;

        assertTrue("Expected no moves to be made", noMoveMade);
    }

    @Test
    public void testNoActionNeededForOptimalBoard() {
        game.setSize(2);
        int[][] board = {
                { 0, 1 },
                { 1, 0 }
        };
        game.setBoard(board);
        // Set state to reflect an already optimal setup
        game.setState(0, 0, 2); // Cleared
        game.setState(0, 1, 1); // Painted
        game.setState(1, 0, 1); // Painted
        game.setState(1, 1, 2); // Cleared

        agentB.checkBoard();

        // Verify that no changes were made to the board state
        assertEquals("Expected cell to remain cleared", 2, game.getState()[0][0]);
        assertEquals("Expected cell to remain painted", 1, game.getState()[0][1]);
        assertEquals("Expected cell to remain painted", 1, game.getState()[1][0]);
        assertEquals("Expected cell to remain cleared", 2, game.getState()[1][1]);
    }

    @Test
    public void testPaintingBasedOnSingleClue() {
        game.setSize(2);
        int[][] board = {
                { 1, -1 },
                { -1, -1 }
        };
        game.setBoard(board);
        // Initially, cells are covered
        game.setState(0, 0, 0); // Covered cell with a clue
        game.setState(0, 1, 0); // Covered, no clue
        game.setState(1, 0, 0); // Covered, no clue
        game.setState(1, 1, 0); // Covered, no clue

        agentB.checkBoard();

        // Verify that the correct cell was painted based on the clue
        assertEquals("Expected cell to be painted based on the clue", 0, game.getState()[0][1]);
    }

    @Test
    public void testClearingUnnecessaryCells() {
        game.setSize(2);
        int[][] board = {
                { -1, 0 },
                { 0, -1 }
        };
        game.setBoard(board);
        // Setup with all cells covered initially
        game.setState(0, 0, 0); // Covered, no clue
        game.setState(0, 1, 0); // Covered cell with a clue indicating 0 surrounding painted cells
        game.setState(1, 0, 0); // Covered cell with a clue indicating 0 surrounding painted cells
        game.setState(1, 1, 0); // Covered, no clue

        agentB.checkBoard();

        // Verify cells around the clues are cleared, not painted
        assertEquals("Expected cell to be cleared based on the clue", 2, game.getState()[0][0]);
        assertEquals("Expected cell to be cleared based on the clue", 2, game.getState()[1][1]);
    }

    @Test
    public void testHandlingFullyRevealedBoard() {
        game.setSize(2);
        int[][] board = {
                { 1, 1 },
                { 1, 1 }
        };
        game.setBoard(board);
        // Board is fully revealed with all cells painted correctly according to clues
        game.setState(0, 0, 1);
        game.setState(0, 1, 1);
        game.setState(1, 0, 1);
        game.setState(1, 1, 1);

        agentB.checkBoard();

        // Verify that no additional moves were made
        boolean noAdditionalMoves = true;
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                if (game.getState()[r][c] != 1) {
                    noAdditionalMoves = false;
                    break;
                }
            }
        }
        assertTrue("Expected no additional moves on a fully revealed board", noAdditionalMoves);
    }
}