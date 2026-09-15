package Client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface defining the strategy the client applies when connected to a server.
 */
public interface IClientStrategy {
    void clientStrategy(InputStream inFromServer, OutputStream outToServer);
}