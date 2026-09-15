package algorithms.search;

import java.util.ArrayList;

/**
 * This interface represents a problem that can be solved by a search algorithm.
 */
public interface ISearchable {

    //Returns the starting point of the problem
    AState getStartState();

    //Returns the target or destination point we want to reach
    AState getGoalState();

    //Returns a list of all valid moves we can make from a specific state
    ArrayList<AState> getAllPossibleStates(AState s);
}