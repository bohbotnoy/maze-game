package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;

/**
 * Server strategy for solving mazes.
 * Receives a Maze object from the client, solves it using the configured
 * search algorithm, caches the solution to disk, and returns the Solution.
 * If the same maze was already solved before, the cached solution is returned.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            // Read the Maze object sent by the client
            Maze maze = (Maze) fromClient.readObject();

            // Use the maze hashCode as a unique cache filename
            String cacheFileName = System.getProperty("java.io.tmpdir")
                    + File.separator + maze.hashCode() + ".solution";

            Solution solution;

            File cacheFile = new File(cacheFileName);
            if (cacheFile.exists()) {
                // Cache hit - load solution from disk instead of recomputing
                solution = loadSolutionFromFile(cacheFileName);
                System.out.println("Cache hit - loaded solution from: " + cacheFileName);
            } else {
                // Cache miss - solve the maze and save the solution to disk
                solution = solveMaze(maze);
                saveSolutionToFile(solution, cacheFileName);
                System.out.println("Solved and cached solution to: " + cacheFileName);
            }

            // Send the solution back to the client
            toClient.writeObject(solution);
            toClient.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Solves the given maze using the algorithm specified in config.properties.
     */
    private Solution solveMaze(Maze maze) {
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        ASearchingAlgorithm searcher = getSearcherFromConfig();
        return searcher.solve(searchableMaze);
    }

    /**
     * Returns a search algorithm based on the algorithm name in config.properties.
     */
    private ASearchingAlgorithm getSearcherFromConfig() {
        String algorithmName = Configurations.getInstance().getMazeSearchingAlgorithm();
        switch (algorithmName) {
            case "BreadthFirstSearch":
                return new BreadthFirstSearch();
            case "DepthFirstSearch":
                return new DepthFirstSearch();
            case "BestFirstSearch":
            default:
                return new BestFirstSearch();
        }
    }

    /**
     * Saves a Solution object to a file using Java serialization.
     */
    private void saveSolutionToFile(Solution solution, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            oos.writeObject(solution);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Solution object from a file using Java deserialization.
     */
    private Solution loadSolutionFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileName))) {
            return (Solution) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}