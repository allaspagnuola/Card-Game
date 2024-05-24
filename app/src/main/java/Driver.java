import java.util.Properties;

/**
 * The main driver class for the game application.
 * This class initializes the game properties and starts the game.
 */
public class Driver {

    /**
     * The default path to the properties file used to configure the game.
     */
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game2.properties";

    /**
     * The main entry point for the game application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Load the game properties from the default properties file
        final Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);

        // Start the game and retrieve the game's log result
        String logResult = new CountingUpGame(properties).runApp();

        // Print the game's log result to the console
        System.out.println("logResult = " + logResult);
    }
}
