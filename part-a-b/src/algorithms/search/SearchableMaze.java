package algorithms.search;

import algorithms.mazeGenerators.Maze;
import java.util.ArrayList;

/**
 * This class is an Adapter.
 * It takes a Maze (from Part A) and makes it behave like an ISearchable (for Part B).
 * This way, our search algorithms can solve the maze without knowing what a maze is
 */
public class SearchableMaze implements ISearchable {

    // We save the original maze here so we can ask it where the walls and paths are
    private Maze maze;
    private int straightStepCost;
    private int diagonalStepCost;

    /**
     * Constructor that receives only a Maze and sets default costs.
     * Straight step costs 10, diagonal step costs 15.
     */
    public SearchableMaze(Maze maze) {
        this.maze = maze;
        this.straightStepCost = 10;
        this.diagonalStepCost = 15;
    }

    public SearchableMaze(Maze maze, int straightStepCost, int diagonalStepCost) {
        this.maze = maze;
        this.straightStepCost = straightStepCost;
        this.diagonalStepCost = diagonalStepCost;
    }

    @Override
    public AState getStartState() {
        // Translate the maze's start position into a MazeState
        int row = maze.getStartPosition().getRowIndex();
        int col = maze.getStartPosition().getColumnIndex();
        return new MazeState(row, col);
    }

    @Override
    public AState getGoalState() {
        // Translate the maze's goal position into a MazeState
        int row = maze.getGoalPosition().getRowIndex();
        int col = maze.getGoalPosition().getColumnIndex();
        return new MazeState(row, col);
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState state) {
        ArrayList<AState> possibleStates = new ArrayList<>();

        // If the state is not a MazeState, we don't know how to handle it, so return an empty list
        if (!(state instanceof MazeState)) {
            return possibleStates;
        }

        // Get the current row and column from the state
        MazeState current = (MazeState) state;
        int row = current.getRow();
        int col = current.getCol();

        // We define all 8 possible directions (up, down, left, right, and 4 diagonals)
        int[][] directions = {
                {-1, 0},   // Up
                {-1, 1},   // Up-Right (diagonal)
                {0, 1},    // Right
                {1, 1},    // Down-Right (diagonal)
                {1, 0},    // Down
                {1, -1},   // Down-Left (diagonal)
                {0, -1},   // Left
                {-1, -1}   // Up-Left (diagonal)
        };

        //Iterate through all 8 directions to find valid neighbors
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            //check if the new position inside the maze borders
            if (newRow >= 0 && newRow < maze.getRows() && newCol >= 0 && newCol < maze.getCols()) {

                //Is the new position a path (0) and not a wall (1)?
                if (maze.getCellValue(newRow, newCol) == 0) {

                    // Determine if the current movement is diagonal
                    // A movement is diagonal if both the row and the column change
                    boolean isDiagonal = (dir[0] != 0 && dir[1] != 0);

                    // For diagonal movement, check that at least one "L-shaped" path is open
                    if (isDiagonal) {
                        boolean path1 = maze.getCellValue(row + dir[0], col) == 0;
                        boolean path2 = maze.getCellValue(row, col + dir[1]) == 0;
                        if (!path1 && !path2) {
                            continue; // both L-shaped paths are blocked, diagonal not allowed
                        }
                    }

                    MazeState neighbor = new MazeState(newRow, newCol);

                    if (isDiagonal) {
                        // Apply the diagonal step cost
                        neighbor.setCost(current.getCost() + this.diagonalStepCost);
                    } else {
                        // Apply the straight step cost
                        neighbor.setCost(current.getCost() + this.straightStepCost);
                    }
                    // Add the valid neighbor to the list of possible states
                    possibleStates.add(neighbor);
                }
            }
        }

        return possibleStates;
    }
}