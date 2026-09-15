package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;

import java.io.*;

/**
 * Server strategy for generating mazes.
 * Receives maze dimensions [rows, cols] from the client,
 * generates a maze, compresses it, and sends it back.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {

    @Override
    public void serverStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            // Read the maze dimensions sent by the client as int[2]
            int[] dimensions = (int[]) fromClient.readObject();
            int rows = dimensions[0];
            int cols = dimensions[1];

            // Choose the maze generator based on config.properties
            AMazeGenerator generator = getGeneratorFromConfig();
            Maze maze = generator.generate(rows, cols);

            // Compress the maze byte array using MyCompressorOutputStream
            byte[] rawMazeBytes = maze.toByteArray();
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(byteArrayOut);
            compressor.write(rawMazeBytes);
            compressor.flush();

            // Send the compressed maze bytes back to the client
            byte[] compressedMaze = byteArrayOut.toByteArray();
            toClient.writeObject(compressedMaze);
            toClient.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a maze generator based on the algorithm name in config.properties.
     */
    private AMazeGenerator getGeneratorFromConfig() {
        String algorithmName = Configurations.getInstance().getMazeGeneratingAlgorithm();
        switch (algorithmName) {
            case "SimpleMazeGenerator":
                return new SimpleMazeGenerator();
            case "MyMazeGenerator":
            default:
                return new MyMazeGenerator();
        }
    }
}