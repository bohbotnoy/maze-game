package algorithms.search;


/**
 * This class represents a single position or state in our problem.
 * For example: A specific point in a maze.
 */
public abstract class AState implements java.io.Serializable{
    //The name or description of the state (like "1,1")
    private String state;

    //The cost or distance it took to get here
    private double cost;

    //The previous state we came from (helps to find the final path)
    private AState cameFrom;

    public AState(String state) {
        this.state = state;
    }

    //get & set
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public AState getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(AState cameFrom) {
        this.cameFrom = cameFrom;
    }

    //Checks if two states are exactly the same
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AState aState = (AState) o;
        return state != null ? state.equals(aState.state) : aState.state == null;
    }

    //Creates a unique ID for the state
    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }
}