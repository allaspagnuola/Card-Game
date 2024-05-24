import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for loading properties from a properties file.
 * This class provides a mechanism to read properties from a specified properties file
 * and return them as a {@link Properties} object.
 */
public class PropertiesLoader {

    /**
     * Loads properties from the specified properties file.
     *
     * @param propertiesFile The path to the properties file to be loaded. This should be a path relative to the classpath.
     * @return A {@link Properties} object containing the properties loaded from the file. Returns null if an error occurs.
     */
    public static Properties loadPropertiesFile(String propertiesFile) {
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertiesFile)) {

            Properties prop = new Properties();
            // load a properties file
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

