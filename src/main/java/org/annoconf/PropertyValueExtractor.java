package org.annoconf;

import org.annoconf.exceptions.AnnoConfException;
import org.annoconf.exceptions.PropertySetException;
import org.annoconf.utils.StringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * Created by roma on 3/9/17.
 */
public class PropertyValueExtractor {

    public static final PropertyValueExtractor INSTANCE = new PropertyValueExtractor();

    public static final char DEFAULT_VALUE_SEPARATOR = ':';
    public static final char DEFAULT_NULL_SEPARATOR = '#';
    public static final String DEFAULT_VALUE_SEPARATOR_STRING = ":";
    public static final String DEFAULT_NULL_SEPARATOR_STRING = "#";

    private PropertyValueExtractor() {}

    public String extract(Properties properties, Property property) throws AnnoConfException {
        Objects.requireNonNull(properties);
        Objects.requireNonNull(property);

        final String fullPropertyName = property.value();
        if (StringUtils.isBlank(fullPropertyName)) {
            throw new PropertySetException(fullPropertyName, "Property name cannot be blank");
        }

        if (!(fullPropertyName.startsWith("${") && fullPropertyName.endsWith("}"))) {
            return fullPropertyName;
        }

        //Removing "${" and "}"
        final String parsedPropertyName = fullPropertyName.substring(2, fullPropertyName.length() - 1);
        final long countOfDefValSeparators = StringUtils.countOf(parsedPropertyName, DEFAULT_VALUE_SEPARATOR);
        final long countOfDefNullSeparators = StringUtils.countOf(parsedPropertyName, DEFAULT_NULL_SEPARATOR);

        validateSeparators(parsedPropertyName, countOfDefValSeparators, countOfDefNullSeparators);
        validateNotBlankName(parsedPropertyName);

        final String propertyKey = parsedPropertyName
                .split(DEFAULT_VALUE_SEPARATOR_STRING)[0]
                .split(DEFAULT_NULL_SEPARATOR_STRING)[0];

        if (properties.containsKey(propertyKey)) {
            return (String) properties.get(propertyKey);
        }

        if (countOfDefValSeparators == 0 && countOfDefNullSeparators == 0) {
            throw new PropertySetException(parsedPropertyName, "Property not found");
        }

        if (countOfDefNullSeparators == 1) {
            return null;
        }

        final String[] keyVal = parsedPropertyName.split(DEFAULT_VALUE_SEPARATOR_STRING);
        //If split result returns 1 element it mean that we have property like "${key:}"
        return keyVal.length > 1 ? keyVal[1] : "";
    }

    private void validateSeparators(String propertyName, long countOfDefValSeparators, long countOfDefNullSeparators) throws PropertySetException {
        if (countOfDefValSeparators > 1 || countOfDefNullSeparators > 1
                || (countOfDefValSeparators > 0 && countOfDefNullSeparators > 0)
                || (countOfDefNullSeparators == 1 && !propertyName.contains("#null"))) {
            throwInvalidPropertyName(propertyName);
        }
    }

    private void validateNotBlankName(String propertyName) throws PropertySetException {
        if (StringUtils.isBlank(propertyName
                .split(DEFAULT_VALUE_SEPARATOR_STRING)[0]
                .split(DEFAULT_NULL_SEPARATOR_STRING)[0])) {
            throwInvalidPropertyName(propertyName);
        }
    }

    private void throwInvalidPropertyName(String propertyName) throws PropertySetException {
        throw new PropertySetException(propertyName, "Invalid property name. Please look at org.annoconf.Property#value() javadoc");
    }

}
