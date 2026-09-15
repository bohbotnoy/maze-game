package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BestFirstSearchTest {

    /**
     * Test 1: Null verification
     * Checks how the algorithm handles a completely null domain.
     */
    @Test
    public void testSolveWithNull() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        Solution solution = bestFirstSearch.solve(null);

        assertNotNull(solution, "The algorithm should return an empty Solution, not null.");
        assertTrue(solution.getSolutionPath().isEmpty(), "The solution path should be empty when domain is null.");
    }

    /**
     * Test 2: Name verification
     * Checks if the getName() method returns the correct algorithm name.
     */
    @Test
    public void testGetName() {
        BestFirstSearch bestFirstSearch = new BestFirstSearch();
        assertEquals("Best First Search", bestFirstSearch.getName(), "The algorithm name is incorrect.");
    }

    /**
     * Test 3: Valid maze bounds verification
     * Checks if the algorithm can successfully solve a completely valid, small maze.
     */
    @Test
    public void testValidMazeSolution() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(10, 10);
        SearchableMaze searchableMaze = new SearchableMaze(maze, 10, 15);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Solution solution = bestFirstSearch.solve(searchableMaze);

        assertNotNull(solution, "The algorithm failed to return a valid solution for a solvable maze.");
        assertFalse(solution.getSolutionPath().isEmpty(), "The solution path should not be empty for a solvable maze.");
    }

    /**
     * Test 4: Execution time constraint
     * Ensures that the algorithm doesn't take too long to evaluate a standard problem.
     */
    @Test
    public void testExecutionTime() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(50, 50);
        SearchableMaze searchableMaze = new SearchableMaze(maze, 10, 15);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        // Asserts that the solution is found within 1 minute (60 seconds)
        assertTimeoutPreemptively(java.time.Duration.ofMinutes(1), () -> {
            bestFirstSearch.solve(searchableMaze);
        }, "The algorithm exceeded the 1-minute time limit.");
    }

    /**
     * Test 5: Start equals Goal
     * Checks behavior when start and goal are the same cell.
     */
    @Test
    public void testStartEqualsGoal() {
        int[][] map = {
                {0, 1},
                {1, 0}
        };
        Maze maze = new Maze(map, new algorithms.mazeGenerators.Position(0, 0), new algorithms.mazeGenerators.Position(0, 0));
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        Solution solution = bestFirstSearch.solve(searchableMaze);

        assertNotNull(solution);
        assertEquals(1, solution.getSolutionPath().size(), "When start equals goal, path should contain only one state.");
    }

    /**
     * Test 6: Large maze performance
     * Checks that the algorithm handles a 1000x1000 maze within time limit.
     */
    @Test
    public void testLargeMaze() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(1000, 1000);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        assertTimeoutPreemptively(java.time.Duration.ofMinutes(1), () -> {
            Solution solution = bestFirstSearch.solve(searchableMaze);
            assertNotNull(solution);
            assertFalse(solution.getSolutionPath().isEmpty());
        }, "The algorithm exceeded the 1-minute time limit for 1000x1000 maze.");
    }

    /**
     * Test 7: Nodes evaluated counter
     * Checks that the algorithm actually counts visited nodes.
     */
    @Test
    public void testNodesEvaluated() {
        MyMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(10, 10);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        BestFirstSearch bestFirstSearch = new BestFirstSearch();

        bestFirstSearch.solve(searchableMaze);

        int nodes = bestFirstSearch.getNumberOfNodesEvaluated();
        assertTrue(nodes > 0, "The algorithm should evaluate at least one node.");
    }
}