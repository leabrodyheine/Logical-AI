package Testing;

import game.Game;
import org.junit.Before;
import org.junit.Test;

import agents.AgentC1;

import static org.junit.Assert.*;

public class AgentC1Test {
    private Game game;
    private AgentC1 agentC1;

    @Before
    public void setUp() {
        game = new Game();
        agentC1 = new AgentC1(game);
    }

    @Test
    public void testCompleteSolve() {
        game = new Game(3);
        // Define a board state here that is solvable by AgentC1
        int[][] board = {
                { -1, 1, -1 },
                { 1, 2, 1 },
                { -1, 1, -1 }
        };

        game.setBoard(board);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                game.setState(r, c, 0);
            }
        }
        agentC1 = new AgentC1(game);
        int result = agentC1.playC1();

        // Assuming 3 indicates a complete and correct solution
        assertEquals("Expected a complete and correct solution", 3, result);
    }

    @Test
    public void testAlreadySolved() {
        game = new Game(2);

        int[][] board = {
                { 1, 1 },
                { -1, -1 }
        };
        game.setBoard(board);

        game.setState(0, 0, 1); // Painted
        game.setState(0, 1, 2); // Cleared
        game.setState(1, 0, 2); // Cleared
        game.setState(1, 1, 2); // Painted
        agentC1 = new AgentC1(game);
        int result = agentC1.playC1();

        // Assuming 3 indicates no further action was necessary
        assertEquals("Expected no action as the game is already solved", 3, result);
    }

    @Test
    public void testPartialSolve() {
        game.setGame(".4*,.-*,.-_,.-_,.-_;.-*,.-*,.-_,.0_,.-_;.-_,.-_,.2_,.-_,.-_;.1_,.-*,.-_,.-_,.-_;.-_,.1_,.-_,.-_,.0_");
        int result = agentC1.playC1();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest4() {
        game.setGame(".-_,*-*,*6*,*4*,_-_;_2_,*-*,*8*,*-*,_3_;_3_,.5_,*7*,*-*,_-_;*-*,*4*,_4_,.-*,_-_;*-*,_-_,.-_,_-_,_1_");
        int result = agentC1.playC1();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testMedium6() {
        game.setGame(".2_,.-_,.-_,.-*,.2_,.-*,.1_;.-*,.-*,.2_,.1_,.-_,.-_,.-_;.-*,.3_,.1_,.0_,.1_,.-_,.3*;.-_,.-_,.2_,.2_,.-_,.5*,.-*;.-_,.3_,.5*,.-*,.-_,.-*,.4*;.3_,.-*,.-*,.7*,.-*,.4_,.-_;.-*,.-*,.-_,.-*,.4*,.2_,.0_");
        int result = agentC1.playC1();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest2() {
        game.setGame(".1_,.3_,.-*;.-*,.-_,.-*;.2*,.-_,.1_");
        int result = agentC1.playC1();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTestMedium0() {
        game.setGame(".3*,.-*,.-*,.4*,.-*,.-*,.-*;.-*,.-_,.-*,.4_,.-_,.-*,.6*;.-*,.6_,.3_,.2_,.2_,.-*,.-*;.-*,.-*,.-*,.-_,.2_,.-_,.-*;.3_,.-*,.4_,.-_,.-_,.6*,.5*;.2_,.-_,.3_,.3*,.-*,.-*,.-*;.1*,.-_,.-_,.-*,.4_,.3_,.2_");
        int result = agentC1.playC1();
        assertEquals("Expected solution", 3, result);
    }
}
