package algorithms.mazeGenerators;

/**
 * Interface defining the required functionality for any maze generator.
 */
public interface IMazeGenerator {

    /**
     * Generates a new Maze.
     *
     * @param rows The number of rows for the maze.
     * @param cols The number of columns for the maze.
     * @return A generated Maze object.
     */
    Maze generate(int rows, int cols);

    /**
     * Measures the time it takes to generate a maze.
     *
     * @param rows The number of rows for the maze.
     * @param cols The number of columns for the maze.
     * @return The generation time in milliseconds.
     */
    long measureAlgorithmTimeMillis(int rows, int cols);

}
