package algorithms.search;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This abstract class represents the common functionality for searching algorithms.
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    // This variable counts how many states the algorithm visited
    protected int visitedNodes;

    public ASearchingAlgorithm() {
        this.visitedNodes = 0;
    }

    /**
     * Returns the total number of states the algorithm visited.
     * We return it as a String because the interface defined it that way.
     */
    @Override
    public int getNumberOfNodesEvaluated() {
        return visitedNodes;
    }

    /**
     * Returns the name of the algorithm.
     * Every specific algorithm will have to provide its own name.
     */
    @Override
    public abstract String getName();

    /**
     * A protected helper method to reconstruct the path from the goal back to the start.
     */
    protected Solution createSolution(AState goalState) {
        Solution solution = new Solution();
        ArrayList<AState> path = new ArrayList<>();
        AState currentState = goalState;

        while (currentState != null) {
            path.add(currentState);
            currentState = currentState.getCameFrom();
        }

        Collections.reverse(path);
        solution.setSolutionPath(path);

        return solution;
    }
}