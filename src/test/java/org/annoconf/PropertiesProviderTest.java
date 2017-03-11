package org.annoconf;

import org.annoconf.exceptions.PropertiesLoadException;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by roma on 3/9/17.
 */
public class PropertiesProviderTest {

    @Test(expected = PropertiesLoadException.class)
    public void loadFromBlankPath() throws PropertiesLoadException {
        PropertiesProvider.loadFrom("   ");
    }

    @Test(expected = PropertiesLoadException.class)
    public void loadFromClassPathNotFound() throws PropertiesLoadException {
        PropertiesProvider.loadFrom("classpath:/fakepath");
    }

    @Test(expected = PropertiesLoadException.class)
    public void loadFromFileNotFound() throws PropertiesLoadException {
        PropertiesProvider.loadFrom("file:/fakepath");
    }

    @Test
    public void loadFromClassPath() throws PropertiesLoadException {
        final Properties properties = PropertiesProvider.loadFrom("classpath:single.properties");
        assertNotNull(properties);
        assertEquals(1, properties.size());
    }

    @Test
    public void loadFromFile() throws PropertiesLoadException {
        final Properties properties = PropertiesProvider.loadFrom("file:src/test/resources/two.properties");
        assertNotNull(properties);
        assertEquals(2, properties.size());
    }

    @Test
    public void loadFromRelativePath() throws PropertiesLoadException {
        final Properties properties = PropertiesProvider.loadFrom("three.properties");
        assertNotNull(properties);
        assertEquals(3, properties.size());
    }

}