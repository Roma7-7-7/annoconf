package org.annoconf.source;

import org.annoconf.PropertyValueSource;

/**
 * Created by roma on 3/19/17.
 */
public class EnvironmentPropertSource implements PropertyValueSource {
    @Override
    public boolean hasValue(String key) {
        return System.getenv(key) != null;
    }

    @Override
    public String getValue(String key) {
        return System.getenv(key);
    }
}
