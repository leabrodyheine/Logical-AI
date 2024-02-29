package game;

/**
 * Represents the game state for a puzzle game, holding the board configuration,
 * the state of each cell, and the solution (game) state. This class offers
 * methods to set up the game, modify cell states, and print the game board.
 * author: a.toniolo
 */

public class Game {
	int[][] board;
	int[][] state;
	int[][] game;
	int size;

	/*
	 * board is the one the agent will play with state indicates the status of the
	 * cells game is the hidden board, the one the agent has to be able to find
	 */
	public Game() {
		size = 2;
		board = new int[size][size];
		state = new int[size][size];
		game = new int[size][size];
	}

	public Game(int n) {
		size = n;
		board = new int[size][size];
		state = new int[size][size];
		game = new int[size][size];
	}

	/**
	 * Parses a game specification string and initializes the game board, state, and
	 * solution based on it.
	 * 
	 * @param map A string representation of the game specification.
	 * @return true if the game was successfully set up, false otherwise.
	 */
	public boolean setGame(String map) {
		try {
			String[] lines = map.split(";"); // rows
			size = lines.length;
			board = new int[size][size];
			state = new int[size][size];
			game = new int[size][size];
			for (int r = 0; r < size; r++) {
				String line = lines[r];
				String[] set = line.split(","); // columns
				int[] bl = new int[size];
				int[] sl = new int[size];
				int[] gl = new int[size];
				for (int c = 0; c < size; c++) {
					String ch = set[c];
					char i = ch.charAt(0); // status
					char e = ch.charAt(ch.length() - 1); // paint
					String m = ch.substring(1, ch.length() - 1); // clue
					int state = 0;
					int paint = 0;
					int clue = 0;
					switch (i) {
						case '*':
							state = 1;
							break;
						case '_':
							state = 2;
							break;
						case '.':
							state = 0;
					}
					if (m.equals("-")) {
						clue = -1;
					} else {
						clue = Integer.parseInt(m);
					}
					switch (e) {
						case '*':
							paint = 1;
							break;
						case '_':
							paint = 2;
							break;
					}
					bl[c] = clue;
					sl[c] = state;
					gl[c] = paint;
				}
				board[r] = bl;
				state[r] = sl;
				game[r] = gl;

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Prints the initial game view, showing clues and the solution state without
	 * revealing it to the player.
	 */
	public void printGame() {
		// this method is used to print the initial game view

		System.out.println();
		// first line
		System.out.format("%4s", "   ");
		for (int c = 0; c < board[0].length; c++) {
			System.out.format("%4s", c);
		}
		System.out.println();
		// second line
		System.out.format("%5s", "   ");
		for (int c = 0; c < board[0].length; c++) {
			System.out.print("--- ");// separator
		}
		System.out.println();
		// the board
		for (int r = 0; r < board.length; r++) {
			System.out.print(" " + r + "| ");// index+separator
			for (int c = 0; c < board[0].length; c++) {
				String code = "";
				// print clues
				if (board[r][c] == -1) {
					code += " ";
				} else {
					code += board[r][c];
				}
				// print paint
				switch (game[r][c]) {
					case 1:
						code += "*";
						break;
					case 2:
						code += "_";
						break;
				}
				System.out.format("%4s", code);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Prints the current view of the game board from the player's perspective,
	 * showing the state of each cell.
	 */
	public void printBoard() {
		// this is used to print the agent view

		System.out.println();
		// first line
		System.out.format("%4s", "   ");
		for (int c = 0; c < board[0].length; c++) {
			System.out.format("%4s", c);
		}
		System.out.println();
		// second line
		System.out.format("%5s", "   ");
		for (int c = 0; c < board[0].length; c++) {
			System.out.print("--- ");// separator
		}
		System.out.println();
		// the board
		for (int r = 0; r < board.length; r++) {
			System.out.print(" " + r + "| ");// index+separator
			for (int c = 0; c < board[0].length; c++) {
				String code = "";
				// print state
				switch (state[r][c]) {
					case 0:
						code += ".";
						break;
					case 1:
						code += "*";
						break;
					case 2:
						code += "_";
						break;
				}
				// print clues
				if (board[r][c] == -1) {
					code += "-";
				} else {
					code += board[r][c];
				}
				System.out.format("%4s", code);
			}
			System.out.println();
		}
		System.out.println();

	}



	/**
	 * Returns the board array containing clues for each cell.
	 * 
	 * @return The board array.
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * Returns the state array representing the current state of each cell on the
	 * board.
	 * 
	 * @return The state array.
	 */
	public int[][] getState() {
		return state;
	}

	/**
	 * Returns the size of the game board.
	 * 
	 * @return The size of the game board.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the state of a specific cell on the game board.
	 * 
	 * @param row The row index of the cell.
	 * @param col The column index of the cell.
	 * @param val The new state value for the cell.
	 */
	public void setState(int row, int col, int val) {
		this.state[row][col] = val;
	}

	// FOR TESTING ONLY
	/**
	 * Sets the size of the game board. This method is intended for testing
	 * purposes.
	 * 
	 * @param size The new size of the game board.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Sets the board array directly. This method is intended for testing purposes.
	 * 
	 * @param board The new board array to set.
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}
}
