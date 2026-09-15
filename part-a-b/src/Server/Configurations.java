package Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class that manages application configurations.
 * Reads settings from the config.properties file.
 */
public class Configurations {

    // The single instance of this class (Singleton pattern)
    private static Configurations instance = null;

    // Java's built-in class for reading .properties files
    private Properties properties;

    /**
     * Private constructor - only this class can create an instance.
     * Loads the config.properties file from the resources folder.
     */
    private Configurations() {
        properties = new Properties();
        try {
            // Load the properties file from the resources folder
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (input != null) {
                properties.load(input);
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the single instance of Configurations.
     * Creates it if it doesn't exist yet (lazy initialization).
     * Synchronized to prevent issues when multiple threads call this at the same time.
     */
    public static synchronized Configurations getInstance() {
        if (instance == null) {
            instance = new Configurations();
        }
        return instance;
    }

    /**
     * Returns the thread pool size from the config file.
     * Default is 5 if not specified.
     */
    public int getThreadPoolSize() {
        return Integer.parseInt(properties.getProperty("threadPoolSize", "5"));
    }

    /**
     * Returns the maze generating algorithm name from the config file.
     * Default is MyMazeGenerator if not specified.
     */
    public String getMazeGeneratingAlgorithm() {
        return properties.getProperty("mazeGeneratingAlgorithm", "MyMazeGenerator");
    }

    /**
     * Returns the maze searching algorithm name from the config file.
     * Default is BestFirstSearch if not specified.
     */
    public String getMazeSearchingAlgorithm() {
        return properties.getProperty("mazeSearchingAlgorithm", "BestFirstSearch");
    }
}