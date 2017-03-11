package org.annoconf;

import org.annoconf.exceptions.PropertiesLoadException;
import org.annoconf.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by roma on 3/9/17.
 */
public class PropertiesProvider {

    private static final String CLASSPATH_KEY = "classpath:";
    private static final String FILE_KEY = "file:";
    private static final int CLASSPATH_KEY_LENGTH = CLASSPATH_KEY.length();
    private static final int FILE_KEY_LENGTH = FILE_KEY.length();

    private PropertiesProvider() {}

    public static Properties loadFrom(String path) throws PropertiesLoadException {
        if (StringUtils.isBlank(path)) {
            throw new PropertiesLoadException("Properties path can not be blank");
        }

        try {
            if (path.startsWith(FILE_KEY)) {
                return loadAndClose(new FileInputStream(new File(path.substring(FILE_KEY_LENGTH))));
            }
            if (path.startsWith(CLASSPATH_KEY)) {
                path = path.substring(CLASSPATH_KEY_LENGTH);
            }

            return loadAndClose(PropertiesProvider.class.getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            throw new PropertiesLoadException(String.format("Failed to load properties from path [%s]", path));
        }
    }

    private static Properties loadAndClose(InputStream stream) throws IOException {
        final Properties result = new Properties();
        result.load(stream);
        close(stream);
        return result;
    }

    private static void close(InputStream stream) {
        try {
            stream.close();
        } catch (Exception e) {
            //Ignore
        }
    }

}
