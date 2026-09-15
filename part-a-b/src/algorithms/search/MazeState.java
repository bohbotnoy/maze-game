package algorithms.search;

import java.util.Objects;

/**
 * This class represents a specific state (a cell) within a maze.
 */
public class MazeState extends AState {

    private int row;
    private int col;

    public MazeState(int row, int col) {
        //Call the parent's constructor and pass the state as a String
        super("{" + row + "," + col + "}");

        //Initialize our own specific variables
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Two MazeStates are equal if they have the exact same row and column.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeState mazeState = (MazeState) o;
        return row == mazeState.row && col == mazeState.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "{" + row + "," + col + "}";
    }
}