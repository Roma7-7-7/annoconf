package org.annoconf.source;

import org.annoconf.PropertyValueSource;

import java.util.Properties;

/**
 * Created by roma on 3/19/17.
 */
public class BasicPropertiesSource implements PropertyValueSource {

    private Properties properties;

    public BasicPropertiesSource(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean hasValue(String key) {
        if (key == null) {
            return false;
        }
        return this.properties.containsKey(key);
    }

    @Override
    public String getValue(String key) {
        if (key == null) {
            return null;
        }
        return this.properties.getProperty(key);
    }

}
