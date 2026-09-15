package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * This class implements the Depth First Search (DFS) algorithm.
 * It uses a Stack (LIFO) to explore as deep as possible along each branch before backtracking.
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() {
        return "Depth First Search";
    }

    @Override
    public Solution solve(ISearchable domain) {
        if (domain == null) return new Solution();

        // Reset the counter from the parent class
        this.visitedNodes = 0;

        Stack<AState> stack = new Stack<>();

        //keep track of the states we already visited
        HashSet<AState> visited = new HashSet<>();

        AState startState = domain.getStartState();
        AState goalState = domain.getGoalState();

        // Push the start state to the top of the stack
        stack.push(startState);
        visited.add(startState);

        while (!stack.isEmpty()) {
            //removes and returns the state at the TOP of the stack (the deepest one)
            AState currentState = stack.pop();

            this.visitedNodes++;

            if (currentState.equals(goalState)) {
                return createSolution(currentState);
            }

            ArrayList<AState> neighbors = domain.getAllPossibleStates(currentState);

            for (AState neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    neighbor.setCameFrom(currentState);
                    visited.add(neighbor);
                    // Push neighbor to the top of the stack
                    stack.push(neighbor);
                }
            }
        }

        return new Solution();
    }
}