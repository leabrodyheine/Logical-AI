package Testing;

import game.Game;
import org.junit.Before;
import org.junit.Test;

import agents.AgentC2;

import static org.junit.Assert.*;

public class AgentC2Test {
    private Game game;
    private AgentC2 agentC2;

    @Before
    public void setUp() {
        game = new Game();
        agentC2 = new AgentC2(game);
    }
    @Test
    public void testPartialSolve() {
        game.setGame(".4*,.-*,.-_,.-_,.-_;.-*,.-*,.-_,.0_,.-_;.-_,.-_,.2_,.-_,.-_;.1_,.-*,.-_,.-_,.-_;.-_,.1_,.-_,.-_,.0_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest4() {
        game.setGame(".-_,*-*,*6*,*4*,_-_;_2_,*-*,*8*,*-*,_3_;_3_,.5_,*7*,*-*,_-_;*-*,*4*,_4_,.-*,_-_;*-*,_-_,.-_,_-_,_1_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testMedium6() {
        game.setGame(".2_,.-_,.-_,.-*,.2_,.-*,.1_;.-*,.-*,.2_,.1_,.-_,.-_,.-_;.-*,.3_,.1_,.0_,.1_,.-_,.3*;.-_,.-_,.2_,.2_,.-_,.5*,.-*;.-_,.3_,.5*,.-*,.-_,.-*,.4*;.3_,.-*,.-*,.7*,.-*,.4_,.-_;.-*,.-*,.-_,.-*,.4*,.2_,.0_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest2() {
        game.setGame(".1_,.3_,.-*;.-*,.-_,.-*;.2*,.-_,.1_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTestMedium0() {
        game.setGame(".3*,.-*,.-*,.4*,.-*,.-*,.-*;.-*,.-_,.-*,.4_,.-_,.-*,.6*;.-*,.6_,.3_,.2_,.2_,.-*,.-*;.-*,.-*,.-*,.-_,.2_,.-_,.-*;.3_,.-*,.4_,.-_,.-_,.6*,.5*;.2_,.-_,.3_,.3*,.-*,.-*,.-*;.1*,.-_,.-_,.-*,.4_,.3_,.2_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTestMedium1() {
        game.setGame(".-_,.-_,.-_,.3*,.4_,.2_,.-_;.1_,.-*,.-_,.6*,.-*,.-*,.-_;.2_,.-_,.7*,.-*,.9*,.-*,.3_;.-_,.-*,.7*,.-*,.-*,.-*,.-_;.4*,.-*,.6*,.6_,.6_,.-*,.5*;.-*,.6_,.-*,.5_,.-*,.-*,.-*;.-_,.-_,.2*,.-_,.3*,.4_,.2_");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest5() {
        game.setGame(".1_,.3_,.-*,.2_,.-_;.-*,.-_,.-*,.3_,.-_;.3*,.-_,.3_,.5_,.-*;.4*,.-_,.-*,.-*,.-*;.-*,.-*,.-_,.4_,.-*");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputSmall7() {
        game.setGame(".3*,.4*,.4_,.2_,.1_;.4_,.6*,.7*,.4*,.2_;.4_,.6*,.8*,.5*,.3_;.4*,.5*,.5_,.4*,.3_;.3*,.3_,.2_,.2_,.2*");
        int result = agentC2.playC2();
        assertEquals("Expected solution", 3, result);
    }
}

