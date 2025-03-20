package pl.sudoku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;


public class LocaleFromConfig {
    public static Locale readPreferredLocaleFromConfig() {
        Properties properties = new Properties();
        String configFile = "config.properties";

        try (InputStream input = LocaleFromConfig.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input != null) {
                properties.load(input);

                //Read preferred.locale from configuration file
                String preferredLocaleStr = properties.getProperty("preferred.locale", "en_US");
                String[] localeParts = preferredLocaleStr.split("_");

                // Create new Locale object
                if (localeParts.length == 2) {
                    return new Locale(localeParts[0], localeParts[1]);
                }
            } else {
                final Logger logger = LoggerFactory.getLogger(LocaleFromConfig.class);
                logger.error("Unable to find " + configFile);
            }
        } catch (Exception e) {
            final Logger logger = LoggerFactory.getLogger(LocaleFromConfig.class);
            logger.error(e.getMessage(), e.getCause());
        }
        //Return en_US if reading was unsuccessfull
        return new Locale("en", "US");
    }
}
