package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface defining the strategy the server applies when a client connects.
 */
public interface IServerStrategy {
    void serverStrategy(InputStream inFromClient, OutputStream outToClient);
}