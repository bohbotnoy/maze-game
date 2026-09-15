package algorithms.search;

/**
 * This interface represents a general searching algorithm.
 */
public interface ISearchingAlgorithm {

    /**
     * The main method that runs the algorithm.
     * It takes a searchable problem (like our maze) and returns a solution.
     */
    Solution solve(ISearchable domain);

    /**
     * Returns the name of the algorithm
     */
    String getName();

    /**
     * Returns the total number of states the algorithm visited during the search.
     * This is useful to compare which algorithm is more efficient
     */
    int getNumberOfNodesEvaluated();
}