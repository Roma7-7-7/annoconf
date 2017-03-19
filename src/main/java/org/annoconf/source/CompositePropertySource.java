package org.annoconf.source;

import org.annoconf.PropertyValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by roma on 3/19/17.
 */
public class CompositePropertySource implements PropertyValueSource {

    private List<PropertyValueSource> sources = new ArrayList<>();

    public CompositePropertySource(PropertyValueSource ... sources) {
        add(sources);
    }

    public void add(PropertyValueSource ... source) {
        this.sources.addAll(Arrays.asList(source));
    }

    @Override
    public boolean hasValue(String key) {
        return this.sources.stream().filter(s -> s.hasValue(key)).findFirst().isPresent();
    }

    @Override
    public String getValue(String key) {
        String result = null;

        for (PropertyValueSource source : this.sources) {
            if (source.hasValue(key)) {
                result = source.getValue(key);
            }
        }

        return result;
    }
}
