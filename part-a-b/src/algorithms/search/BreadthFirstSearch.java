package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements the Breadth First Search (BFS) algorithm.
 * It explores the problem level by level to find the shortest path.
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() {
        return "Breadth First Search";
    }

    @Override
    public Solution solve(ISearchable domain) {
        if (domain == null) return new Solution();

        //Reset the counter from the parent class before starting a new search
        this.visitedNodes = 0;

        //A standard FIFO queue for BFS
        Queue<AState> queue =createQueue();

        //A fast set to keep track of the states we already visited
        HashSet<AState> visited = new HashSet<>();

        // Get the start and goal states from the problem domain
        AState startState = domain.getStartState();
        AState goalState = domain.getGoalState();

        // Add the start state to both the queue and the visited set
        queue.add(startState);
        visited.add(startState);

        // Keep searching as long as there are states in the queue
        while (!queue.isEmpty()) {
            // Remove the first state from the queue
            AState currentState = queue.poll();

            //Update our visited nodes counter
            this.visitedNodes++;

            // If we reached the goal, build and return the solution
            if (currentState.equals(goalState)) {
                return createSolution(currentState);
            }

            //Get all valid neighbors from the current state
            ArrayList<AState> neighbors = domain.getAllPossibleStates(currentState);

            //Check each neighbor
            for (AState neighbor : neighbors) {
                //If we haven't visited this neighbor yet
                if (!visited.contains(neighbor)) {
                    // Save the path history
                    neighbor.setCameFrom(currentState);
                    // Mark as visited
                    visited.add(neighbor);
                    // Add to the queue for future exploration
                    queue.add(neighbor);
                }
            }
        }

        // If the queue is empty and no path was found, return an empty solution
        return new Solution();
    }

    /**
     * Creates the queue for the search algorithm.
     * BFS uses a standard Queue (FIFO).
     */
    protected Queue<AState> createQueue() {
        return new LinkedList<>();
    }

}