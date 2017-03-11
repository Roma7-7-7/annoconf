package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.exceptions.PropertyExtractException;

import java.util.Properties;

/**
 * Created by roma on 3/9/17.
 */
public interface PropertyValueExtractor<T> {

    T extract(Properties properties, Property property) throws PropertyExtractException;

}
