package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A generic client that connects to a server and communicates using
 * a pluggable IClientStrategy (Strategy Pattern).
 */
public class Client {
    private InetAddress serverIP;
    private int serverPort;
    private IClientStrategy strategy;

    /**
     * @param serverIP   The IP address of the server to connect to
     * @param serverPort The port the server is listening on
     * @param strategy   The strategy that defines how the client communicates
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    /**
     * Connects to the server, applies the client strategy, and closes the connection.
     */
    public void communicateWithServer() {
        try (Socket serverSocket = new Socket(serverIP, serverPort)) {
            System.out.println("Connected to server - IP = " + serverIP + ", Port = " + serverPort);
            strategy.clientStrategy(
                    serverSocket.getInputStream(),
                    serverSocket.getOutputStream()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}