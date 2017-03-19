package org.annoconf.stub;

import org.annoconf.PropertyValueSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roma on 3/19/17.
 */
public class PropertyValueSourceStub implements PropertyValueSource {

    private Map<String, String> properties = new HashMap<>();

    @Override
    public boolean hasValue(String key) {
        return properties.containsKey(key);
    }

    @Override
    public String getValue(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
