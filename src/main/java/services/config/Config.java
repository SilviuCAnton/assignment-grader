package services.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

class Config {

    private static String CONFIG_LOCATION = Objects.requireNonNull(Config.class.getClassLoader()
            .getResource("config.properties")).getFile();

    private static String YEAR_STRUCTURE_LOCATION = Objects.requireNonNull(Config.class.getClassLoader()
            .getResource("yearStructure.properties")).getFile();

    static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(CONFIG_LOCATION));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }

    static Properties getYearStructureProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(YEAR_STRUCTURE_LOCATION));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
