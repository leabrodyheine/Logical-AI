import java.util.Scanner;

import agents.AgentA;
import agents.AgentB;
import agents.AgentC1;
import agents.AgentC2;
import agents.AgentC3;
import game.Game;

/**
 * Main class for a game application, designed to initialize and play a game
 * based on the specified agent. This class processes command-line arguments to
 * determine the game agent to use, supports verbose output, reads game
 * specifications from standard input, initializes the game state, and executes
 * the game strategy. The outcome of the game is determined and printed based on
 * the actions taken by the chosen agent.
 * 
 * author: a.toniolo
 */
public class P1main {
	/**
	 * Main entry point for the application. Processes command-line arguments to
	 * select an agent, optionally enables verbose output, and reads game
	 * specifications to initialize the game state. Executes the game strategy for
	 * the selected agent and prints the outcome.
	 * 
	 * @param args Command-line arguments specifying the agent to use, optional
	 *             verbosity, and other parameters. Expected format:
	 *             <A|B|C1|C2|C3|D> [verbose] [<any other param>]
	 */
	public static void main(String[] args) {

		// Check command-line inputs and set verbosity
		boolean verbose = false;

		if (args.length < 1) {
			System.out.println("usage: ./playSweeper.sh <A|B|C1|C2|C3|D> [verbose] [<any other param>]");
			System.exit(1);
		}
		if (args.length > 1 && args[1].equals("verbose")) {
			verbose = true; // prints additional details if true
		}
		// get specific game
		System.out.println("Please enter the game spec:");
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine(); // requires a single line as specified in the .txt examples
		System.out.println(line);
		Game board = new Game();
		boolean parse = board.setGame(line);
		if (!parse) {
			System.out.println("Something went wrong with your game spec, please try again");
			System.exit(1);
		}

		// start
		System.out.println("Agent " + args[0] + " plays  \n");
		System.out.println("Game:");
		board.printGame();
		System.out.println("Intitial view:");
		board.printBoard();

		System.out.println("Start!");

		int output = 0;

		switch (args[0]) {
			case "A":
				AgentA agentA = new AgentA(board);
				output = agentA.playA();
				break;

			case "B":
				AgentB agentB = new AgentB(board);
				output = agentB.playB();
				break;

			case "C1":
				AgentC1 agentC1 = new AgentC1(board);
				output = agentC1.playC1();
				break;

			case "C2":
				AgentC2 agentC2 = new AgentC2(board);
				output = agentC2.playC2();
				break;

			case "C3":
				AgentC3 agentC3 = new AgentC3(board);
				output = agentC3.playC3();
				break;

			case "D":
				break;

		}

		board.printBoard();
		switch (output) {

			/*
			 * output options:
			 * 0=!complete && !correct
			 * 1=complete && !correct
			 * 2=!complete && correct
			 * 3=complete && correct
			 */

			case 0:
				System.out.println("\nResult: Game not terminated and incorrect\n");
				break;

			case 1:
				System.out.println("\nResult: Agent loses: Game terminated but incorrect \n");
				break;

			case 2:
				System.out.println("\nResult: Game not terminated but correct \n");
				break;

			case 3:
				System.out.println("\nResult: Agent wins: Game terminated and correct \n");
				break;

			default:
				System.out.println("\nResult: Unknown\n");

		}

	}

}
