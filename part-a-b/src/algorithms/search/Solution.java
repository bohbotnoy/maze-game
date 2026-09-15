package algorithms.search;

import java.util.ArrayList;

/**
 * This class represents the final solution to our search problem.
 * It holds the path of states from the start state to the goal state.
 */
public class Solution implements java.io.Serializable{
    private ArrayList<AState> solutionPath;

    public Solution() {
        this.solutionPath = new ArrayList<>();
    }

    /**
     * Returns the path of the solution.
     */
    public ArrayList<AState> getSolutionPath() {
        return solutionPath;
    }

    /**
     * Sets the path of the solution.
     */
    public void setSolutionPath(ArrayList<AState> solutionPath) {
        this.solutionPath = solutionPath;
    }
}