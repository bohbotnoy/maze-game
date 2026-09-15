package algorithms.mazeGenerators;

/**
 * Abstract class for maze generators.
 */
public abstract class AMazeGenerator implements IMazeGenerator {

    /**
     * Generates a maze using the Iterative DFS algorithm
     * @param rows The number of rows in the maze
     * @param cols The number of columns in the maze
     * @return A new generated Maze object
     */
    @Override
    public abstract Maze generate(int rows, int cols);

    /**
     * Measures the time taken to generate a maze using System.currentTimeMillis().
     *
     * @param rows The number of rows for the maze.
     * @param cols The number of columns for the maze.
     * @return The time taken to generate the maze in milliseconds.
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int cols) {
        long startTime = System.currentTimeMillis();
        generate(rows, cols);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}