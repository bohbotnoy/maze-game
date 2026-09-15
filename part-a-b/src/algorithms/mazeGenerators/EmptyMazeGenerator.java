package algorithms.mazeGenerators;

/**
 * This class generates an empty maze without any walls.
 */
public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     * Generates an empty maze.
     *
     * @param rows The number of rows for the maze.
     * @param cols The number of columns for the maze.
     * @return A Maze object filled with 0s (no walls).
     */
    @Override
    public Maze generate(int rows, int cols) {
        //Default maze size.
        if (rows < 2) rows = 2;
        if (cols < 2) cols = 2;
        // In Java, an int array is automatically initialized with 0s.
        // Therefore, this array already represents an empty maze.
        int[][] map = new int[rows][cols];

        // Define the start position (top-left) and goal position (bottom-right).
        Position start = new Position(0, 0);
        Position goal = new Position(rows - 1, cols - 1);

        return new Maze(map, start, goal);
    }
}