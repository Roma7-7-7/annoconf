package org.annoconf.source;

import org.annoconf.PropertyValueSource;

/**
 * Created by roma on 3/19/17.
 */
public class SystemPropertySource implements PropertyValueSource {

    @Override
    public boolean hasValue(String key) {
        return System.getProperty(key) != null;
    }

    @Override
    public String getValue(String key) {
        return System.getProperty(key);
    }
}
