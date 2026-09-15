package algorithms.mazeGenerators;

/**
 * Represents a specific location (row and column) within a maze.
 */
public class Position implements java.io.Serializable{
    private int rowIndex;
    private int columnIndex;

    /**
     * Constructor to initialize the position.
     *
     * @param rowIndex The row index of the position.
     * @param columnIndex The column index of the position.
     */
    public Position(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    /**
     * @return The row index.
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @return The column index.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Returns a string representation of the position in the format "{row,column}".
     *
     * @return String representing the position.
     */
    @Override
    public String toString() {
        return "{" + rowIndex + "," + columnIndex + "}";
    }
}
