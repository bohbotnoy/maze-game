package algorithms.mazeGenerators;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * Generates a maze using the Iterative DFS algorithm.
 */
public class MyMazeGenerator extends AMazeGenerator {

    private Random rand = new Random();

    /**
     * Generates a maze using Iterative DFS.
     *
     * @param rows The exact number of rows for the maze.
     * @param cols The exact number of columns for the maze.
     * @return A randomly generated Maze object.
     */
    @Override
    public Maze generate(int rows, int cols) {
        //Default maze size.
        if (rows < 2) rows = 2;
        if (cols < 2) cols = 2;

        // Step 1: Initialize the maze - all cells are walls (1)
        int[][] map = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                map[i][j] = 1;

        // Step 2: Always start from (0,0) - ensures all "rooms" are at even indices
        // This guarantees start (0,0) and goal (rows-1, cols-1) are reachable
        map[0][0] = 0;

        // Step 3: Push starting cell onto the stack
        Deque<Position> stack = new ArrayDeque<>();
        stack.push(new Position(0, 0));

        // Step 4: Main loop - iterative DFS
        while (!stack.isEmpty()) {
            Position current = stack.peek();
            int row = current.getRowIndex();
            int col = current.getColumnIndex();

            // Find all unvisited neighbors at distance 2 that are still walls
            Position[] neighbors = getUnvisitedNeighbors(row, col, rows, cols, map);

            if (neighbors.length > 0) {
                // Choose a random unvisited neighbor
                Position chosen = neighbors[rand.nextInt(neighbors.length)];

                // Break the wall between current and chosen (the cell between them)
                int wallRow = (row + chosen.getRowIndex()) / 2;
                int wallCol = (col + chosen.getColumnIndex()) / 2;
                map[wallRow][wallCol] = 0;

                // Mark chosen cell as open
                map[chosen.getRowIndex()][chosen.getColumnIndex()] = 0;

                // Push chosen onto the stack
                stack.push(chosen);

            } else {
                // No unvisited neighbors - backtrack
                stack.pop();
            }
        }

        // Step 5: Handle the goal position
        // Carve a path from the nearest even cell to the goal
        int lastRow = rows - 1;
        int lastCol = cols - 1;
        map[lastRow][lastCol] = 0;

        // Connect goal to the nearest reachable even cell
        // If lastRow is odd - connect upward (even row above)
        if (lastRow % 2 == 1)
            map[lastRow - 1][lastCol] = 0;

        // If lastCol is odd - connect leftward (even col to the left)
        if (lastCol % 2 == 1)
            map[lastRow][lastCol - 1] = 0;

        Position start = new Position(0, 0);
        Position goal = new Position(lastRow, lastCol);

        return new Maze(map, start, goal);
    }


    /**
     * Finds neighbors that are exactly 2 steps away and are still walls (unvisited).
     * @param row
     * @param col
     * @param rows
     * @param cols
     * @param map
     * @return unvisited neighbors at distance 2 that are still walls (value = 1)
     * Distance 2 ensures there is a wall cell between current and neighbor
     */
    private Position[] getUnvisitedNeighbors(
            int row, int col, int rows, int cols, int[][] map) {

        Position[] neighbors = new Position[4];
        int count = 0;

        // Up
        if (row - 2 >= 0 && map[row - 2][col] == 1)
            neighbors[count++] = new Position(row - 2, col);
        // Down
        if (row + 2 < rows && map[row + 2][col] == 1)
            neighbors[count++] = new Position(row + 2, col);
        // Left
        if (col - 2 >= 0 && map[row][col - 2] == 1)
            neighbors[count++] = new Position(row, col - 2);
        // Right
        if (col + 2 < cols && map[row][col + 2] == 1)
            neighbors[count++] = new Position(row, col + 2);

        return java.util.Arrays.copyOf(neighbors, count);
    }
}