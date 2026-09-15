package algorithms.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;


/**
 * This class implements the Best First Search algorithm.
 * It extends BFS but uses a Priority Queue to evaluate the cheapest states first.
 */
public class BestFirstSearch extends BreadthFirstSearch {

    @Override
    public String getName() {
        return "Best First Search";
    }

    /**
     * Overrides the queue creation from BFS.
     * Instead of a regular queue, it returns a PriorityQueue that orders states by their cost.
     */
    @Override
    protected Queue<AState> createQueue() {
        // Creates a priority queue that sorts the states based on their cost (lowest cost first)
        return new PriorityQueue<>(Comparator.comparingDouble(AState::getCost));
    }

    @Override
    public Solution solve(ISearchable domain) {
        if (domain == null) return new Solution();

        this.visitedNodes = 0;

        Queue<AState> queue = createQueue();
        HashMap<String, AState> visited = new HashMap<>();

        AState startState = domain.getStartState();
        AState goalState = domain.getGoalState();

        queue.add(startState);
        visited.put(startState.getState(), startState);

        while (!queue.isEmpty()) {
            AState currentState = queue.poll();
            this.visitedNodes++;

            if (currentState.equals(goalState)) {
                return createSolution(currentState);
            }

            ArrayList<AState> neighbors = domain.getAllPossibleStates(currentState);

            for (AState neighbor : neighbors) {
                String key = neighbor.getState();

                if (!visited.containsKey(key)) {
                    neighbor.setCameFrom(currentState);
                    visited.put(key, neighbor);
                    queue.add(neighbor);
                } else {
                    AState existingState = visited.get(key);
                    if (neighbor.getCost() < existingState.getCost()) {
                        existingState.setCameFrom(currentState);
                        existingState.setCost(neighbor.getCost());
                        if (!queue.contains(existingState)) {
                            queue.add(existingState);
                        }
                    }
                }
            }
        }

        return new Solution();
    }
}