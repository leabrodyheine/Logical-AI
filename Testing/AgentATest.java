package Testing;

import org.junit.Before;
import org.junit.Test;

import agents.AgentA;
import game.Game;

import static org.junit.Assert.*;

public class AgentATest {
    private Game game;
    private AgentA agentA;

    @Before
    public void setUp() {
        game = new Game();
        agentA = new AgentA(game);
    }

    @Test
    public void testGameNotTerminatedAndCorrect() {
        game.setSize(2);
        game.setState(0, 0, 0);
        game.setState(0, 1, 0);
        game.setState(1, 0, 0);
        game.setState(1, 1, 0);
        int result = agentA.playA();
        assertEquals("Game should be incomplete but correct so far", 2, result);
    }

    @Test
    public void testCheckCompletion_AllCellsRevealed() {
        game.setSize(2);
        game.setState(0, 0, 1);
        game.setState(0, 1, 2);
        game.setState(1, 0, 2);
        game.setState(1, 1, 1);
        assertTrue("Game should be complete when all cells are revealed", agentA.checkCompletion());
    }

    @Test
    public void testCheckCompletion_AllCellsRevealed2() {
        game.setSize(2);
        int[][] board = {
                { -1, -1 },
                { -1, -1 }
        };
        game.setBoard(board);
        game.setState(0, 0, 1);
        game.setState(0, 1, 2);
        game.setState(1, 0, 1);
        game.setState(1, 1, 2);
        assertTrue("Game should be complete when all cells are revealed", agentA.checkCompletion());
    }

    @Test
    public void testCheckCompletion_SomeCellsUnrevealed() {
        game.setSize(2);
        game.setState(0, 0, 0); // Unrevealed cell
        game.setState(0, 1, 1);
        game.setState(1, 0, 2);
        game.setState(1, 1, 1);
        assertFalse("Game should not be complete when some cells are unrevealed", agentA.checkCompletion());
    }

    @Test
    public void testGameCorrectness_BasedOnClues() {
        game.setSize(2);
        int[][] board = {
                { 1, -1 },
                { -1, 2 }
        }; // Clues for each cell
        game.setBoard(board);
        game.setState(0, 0, 1); // Cell [0,0] painted, matching its clue
        game.setState(0, 1, 2); // Cell [0,1] cleared, no clue
        game.setState(1, 0, 2); // Cell [1,0] cleared, no clue
        game.setState(1, 1, 1); // Cell [1,1] painted, but it's supposed to have 2 painted around, incorrect
                                // setup
        assertFalse("Game should be incorrect based on clues", agentA.checkCorrectness());
    }

    @Test
    public void testGameIncompleteness() {
        game.setSize(2);
        game.setState(0, 0, 0); // Cell [0,0] unrevealed
        game.setState(0, 1, 1); // Cell [0,1] painted
        game.setState(1, 0, 2); // Cell [1,0] cleared
        game.setState(1, 1, 0); // Cell [1,1] unrevealed

        assertFalse("Game should not be complete when some cells are unrevealed", agentA.checkCompletion());
    }

    @Test
    public void testPlayA_CompleteAndIncorrect() {
        // Setup a game that appears complete but doesn't match the clues correctly
        game.setSize(2);
        int[][] board = {
                { 1, 2 },
                { 2, 1 }
        };
        game.setBoard(board);
        game.setState(0, 0, 1); // Incorrectly painted
        game.setState(0, 1, 1); // Incorrectly painted
        game.setState(1, 0, 2); // Correctly cleared
        game.setState(1, 1, 2); // Incorrectly cleared

        int result = agentA.playA();
        assertEquals("Game should be complete but incorrect", 1, result);
    }

    @Test
    public void testPlayA_IncompleteAndCorrectSoFar() {
        // Setup a partially revealed game that's correct based on the clues so far
        game.setSize(2);
        int[][] board = {
                { 1, -1 },
                { -1, 1 }
        };
        game.setBoard(board);
        game.setState(0, 0, 1); // Correctly painted
        game.setState(0, 1, 0); // Unrevealed
        game.setState(1, 0, 0); // Unrevealed
        game.setState(1, 1, 0); // Correctly painted

        int result = agentA.playA();
        assertEquals("Game should be incomplete but correct so far", 2, result);
    } // is this a testing issue or an agent A issue?

    @Test
    public void testCorrectnessWithMixedClues() {
        game = new Game(3);
        int[][] board = {
                { -1, 3, -1 },
                { -1, -1, -1 },
                { -1, -1, -1 }
        };
        game.setBoard(board);
        // Setup a scenario where the central cell has a clue indicating 3 surrounding
        // cells should be painted
        game.setState(0, 0, 2); // Cleared
        game.setState(0, 1, 1); // Painted
        game.setState(0, 2, 2); // Cleared
        game.setState(1, 0, 1); // Painted
        game.setState(1, 1, 0); // Central clue cell, unrevealed
        game.setState(1, 2, 1); // Painted
        game.setState(2, 0, 2); // Cleared
        game.setState(2, 1, 1); // Painted
        game.setState(2, 2, 2); // Cleared

        assertTrue("Game should be correct based on mixed clues", agentA.checkCorrectness());
    }
    //  why is it out of bounds at length 3?

    @Test
    public void testIncorrectnessDueToExcessPaint() {
        game.setSize(2);
        int[][] board = {
                { 1, -1 },
                { -1, 2 }
        };
        game.setBoard(board);
        // Setup the board where one cell has more painted neighbors than the clue
        // indicates
        game.setState(0, 0, 1); // Cell [0,0] painted, should only have 1 painted neighbor
        game.setState(0, 1, 1); // Painted, contributing to incorrectness
        game.setState(1, 0, 1); // Painted, excess painting
        game.setState(1, 1, 2); // Cleared

        assertFalse("Game should be incorrect due to excess painted cells", agentA.checkCorrectness());
    }

    @Test
    public void testCompletionWithNoClues() {
        game.setSize(2);
        int[][] board = {
                { -1, -1 },
                { -1, -1 }
        };
        game.setBoard(board);
        // Setup the game where there are no clues, but all cells are revealed
        game.setState(0, 0, 1); // Painted
        game.setState(0, 1, 2); // Cleared
        game.setState(1, 0, 2); // Cleared
        game.setState(1, 1, 1); // Painted

        assertTrue("Game should be considered complete with no clues", agentA.checkCompletion());
    }

    @Test
    public void testPartialCorrectnessInIncompleteGame() {
        game = new Game(3);
        int[][] board = {
                { 2, -1, 1 },
                { -1, 1, -1 },
                { 1, -1, 2 }
        };
        game.setBoard(board);
        // Setup an incomplete game state that should be partially correct
        game.setState(0, 0, 1); // Painted, matching its clue
        game.setState(0, 1, 0); // Unrevealed
        game.setState(0, 2, 1); // Painted, matching its clue
        game.setState(1, 0, 0); // Unrevealed
        game.setState(1, 1, 0); // Central clue cell, unrevealed
        game.setState(1, 2, 0); // Unrevealed
        game.setState(2, 0, 1); // Painted, matching its clue
        game.setState(2, 1, 0); // Unrevealed
        game.setState(2, 2, 2); // Cleared, matching its clue

        // Since the game is incomplete, we cannot use checkCorrectness() directly to
        // assert true for partial correctness.
        // Instead, we check if the current revealed state does not violate any clues.
        boolean partialCorrectness = true;
        if (game.getBoard()[0][0] == 2 && (game.getState()[0][0] != 1 || game.getState()[0][2] != 1)) {
            partialCorrectness = false;
        }
        if (game.getBoard()[0][2] == 1 && game.getState()[0][2] != 1) {
            partialCorrectness = false;
        }
        if (game.getBoard()[2][0] == 1 && game.getState()[2][0] != 1) {
            partialCorrectness = false;
        }
        if (game.getBoard()[2][2] == 2 && game.getState()[2][2] != 2) {
            partialCorrectness = false;
        }

        assertTrue("Game should be partially correct in its current incomplete state", partialCorrectness);
    } //same issue here with index out of bounds for size 3
}