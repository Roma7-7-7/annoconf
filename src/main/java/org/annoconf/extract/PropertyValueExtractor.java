package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.PropertyValueSource;
import org.annoconf.exceptions.PropertyExtractException;

/**
 * Created by roma on 3/9/17.
 */
public interface PropertyValueExtractor<T> {

    T extract(PropertyValueSource source, Property property) throws PropertyExtractException;

}
