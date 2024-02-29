package agents;
/**
 * Represents a cell on a game board with a specific row and column position.
 * This class encapsulates the basic information necessary to identify
 * individual cells within a grid-based game, such as a puzzle or a board game,
 * by their row and column indices.
 */
public class Cell {
    int row;
    int col;

    /**
     * Constructs a Cell instance with specified row and column indices.
     * 
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns a string representation of the cell's position in the format "(row,
     * col)". This method provides a human-readable form of the cell's location on
     * the game board, making it easier to identify and reference specific cells in
     * textual output or debugging information.
     * 
     * @return A string representing the cell's row and column indices.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}