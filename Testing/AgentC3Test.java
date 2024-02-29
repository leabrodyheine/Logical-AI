package Testing;

import game.Game;
import org.junit.Before;
import org.junit.Test;

import agents.AgentC3;

import static org.junit.Assert.*;

public class AgentC3Test {
    private Game game;
    private AgentC3 agentC3;

    @Before
    public void setUp() {
        game = new Game();
        agentC3 = new AgentC3(game);
    }
    
    @Test //FIND NEW TEST
    public void testInputMedium7() {
        game.setGame(".-*,.4*,.-*,.2_,.-_,.-*,.3*;.-_,.-_,.-*,.-_,.-_,.-_,.-*;.1_,.-_,.-*,.-*,.1_,.-_,.-_;.-_,.-*,.-_,.2_,.1_,.1_,.-_;.5*,.-*,.4_,.1_,.1_,.-_,.-*;.-*,.-*,.-*,.-_,.3_,.6*,.-*;.-_,.5*,.4*,.-_,.-*,.-*,.4*");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest4() {
        game.setGame(".-_,*-*,*6*,*4*,_-_;_2_,*-*,*8*,*-*,_3_;_3_,.5_,*7*,*-*,_-_;*-*,*4*,_4_,.-*,_-_;*-*,_-_,.-_,_-_,_1_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testMedium6() {
        game.setGame(".2_,.-_,.-_,.-*,.2_,.-*,.1_;.-*,.-*,.2_,.1_,.-_,.-_,.-_;.-*,.3_,.1_,.0_,.1_,.-_,.3*;.-_,.-_,.2_,.2_,.-_,.5*,.-*;.-_,.3_,.5*,.-*,.-_,.-*,.4*;.3_,.-*,.-*,.7*,.-*,.4_,.-_;.-*,.-*,.-_,.-*,.4*,.2_,.0_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest2() {
        game.setGame(".1_,.3_,.-*;.-*,.-_,.-*;.2*,.-_,.1_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTestMedium0() {
        game.setGame(".3*,.-*,.-*,.4*,.-*,.-*,.-*;.-*,.-_,.-*,.4_,.-_,.-*,.6*;.-*,.6_,.3_,.2_,.2_,.-*,.-*;.-*,.-*,.-*,.-_,.2_,.-_,.-*;.3_,.-*,.4_,.-_,.-_,.6*,.5*;.2_,.-_,.3_,.3*,.-*,.-*,.-*;.1*,.-_,.-_,.-*,.4_,.3_,.2_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 1, result);
    }

    @Test
    public void testInputTestMedium1() {
        game.setGame(".-_,.-_,.-_,.3*,.4_,.2_,.-_;.1_,.-*,.-_,.6*,.-*,.-*,.-_;.2_,.-_,.7*,.-*,.9*,.-*,.3_;.-_,.-*,.7*,.-*,.-*,.-*,.-_;.4*,.-*,.6*,.6_,.6_,.-*,.5*;.-*,.6_,.-*,.5_,.-*,.-*,.-*;.-_,.-_,.2*,.-_,.3*,.4_,.2_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest5() {
        game.setGame(".1_,.3_,.-*,.2_,.-_;.-*,.-_,.-*,.3_,.-_;.3*,.-_,.3_,.5_,.-*;.4*,.-_,.-*,.-*,.-*;.-*,.-*,.-_,.4_,.-*");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }

    @Test
    public void testInputTest7() {
        game.setGame(".-*,.-*,.-_,.4_,.-*;.4_,.-*,.5*,.-*,.-*;.4*,.-_,.5_,.6*,.-*;.5*,.-*,.3_,.-_,.4*;.4*,.-*,.2_,.-_,.-*");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }
    @Test
    public void testInputTest0() {
        game.setGame(".2_,.-*,.4*;.-_,.6*,.-*;.3*,.-*,.-_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }
    @Test
    public void testInputMedium3() {
        game.setGame(".-*,.3*,.-_,.-_,.-_,.-_,.-*;.-*,.-_,.-_,.2*,.2_,.1_,.-_;.-*,.-_,.2_,.-*,.3_,.1_,.0_;.-*,.3_,.-_,.-_,.-*,.-_,.-_;.-*,.3_,.1_,.-*,.-*,.6*,.-*;.-*,.-_,.-_,.-_,.4_,.-*,.-*;.-*,.3*,.-_,.0_,.-_,.-_,.2_");
        int result = agentC3.playC3();
        assertEquals("Expected solution", 3, result);
    }
}

