package algorithms.mazeGenerators;

import java.util.Random;

/**
 * A simple maze generator that randomly scatters walls.
 * It ensures at least one valid path exists from start to goal.
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    private Random rand = new Random();

    /**
     * Generates a maze by randomly placing walls and carving a guaranteed path.
     *
     * @param rows The number of rows for the maze.
     * @param cols The number of columns for the maze.
     * @return A randomly generated Maze object.
     */
    @Override
    public Maze generate(int rows, int cols) {
        //Default maze size.
        if (rows < 2) rows = 2;
        if (cols < 2) cols = 2;

        int[][] map = new int[rows][cols];

        // Step 1: Randomly scatter walls (30% chance for a wall, 70% for a path)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = (rand.nextInt(10) < 3) ? 1 : 0;
            }
        }

        // Step 2: Carve a guaranteed path from start to goal
        int row = 0;
        int col = 0;
        map[row][col] = 0; // Ensure start is open

        // Random walk towards the bottom-right corner
        while (row != rows - 1 || col != cols - 1) {
            // Decide whether to move down.
            // We must move down if we reached the rightmost column.
            // We cannot move down if we reached the bottom row.
            boolean goDown = (row < rows - 1) &&
                    (col == cols - 1 || rand.nextBoolean());

            if (goDown)
                row++;
            else
                col++;

            // Clear the cell on our guaranteed path
            map[row][col] = 0;
        }

        map[0][0] = 0;
        map[rows - 1][cols - 1] = 0;

        // Define the start and goal positions
        Position start = new Position(0, 0);
        Position goal = new Position(rows - 1, cols - 1);

        return new Maze(map, start, goal);
    }
}