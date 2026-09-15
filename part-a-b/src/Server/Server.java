package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A generic multithreaded server that listens on a given port and handles
 * each client connection using a pluggable IServerStrategy (Strategy Pattern).
 * Uses a thread pool to handle multiple clients concurrently.
 */
public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private volatile boolean stop; // volatile ensures visibility across threads
    private ExecutorService threadPool;

    /**
     * @param port                The port the server listens on
     * @param listeningIntervalMS Socket timeout in milliseconds (allows checking stop flag)
     * @param strategy            The strategy to apply for each connected client
     */
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        // Thread pool size is read from config.properties
        this.threadPool = Executors.newFixedThreadPool(
                Configurations.getInstance().getThreadPoolSize()
        );
    }

    /**
     * Starts the server in a new background thread.
     * The server keeps accepting clients until stop() is called.
     */
    public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                // Timeout allows the loop to check the stop flag periodically
                serverSocket.setSoTimeout(listeningIntervalMS);
                System.out.println("Server started on port " + port);

                while (!stop) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        // Submit client handling to the thread pool
                        threadPool.submit(() -> handleClient(clientSocket));
                    } catch (IOException e) {
                        // accept() timed out - loop again to check stop flag
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server on port " + port + " stopped.");
        }).start();
    }

    /**
     * Handles a single client connection by applying the server strategy.
     * Closes the socket when done.
     *
     * @param clientSocket The connected client socket
     */
    private void handleClient(Socket clientSocket) {
        try {
            strategy.serverStrategy(
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream()
            );
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Signals the server to stop accepting new clients and shuts down the thread pool.
     */
    public void stop() {
        stop = true;
        threadPool.shutdown();
    }
}