package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.exceptions.PropertyExtractException;
import org.annoconf.utils.StringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * Created by roma on 3/9/17.
 */
abstract class AbstractPropertyValueExtractor<T> implements PropertyValueExtractor {

    static final char DEFAULT_VALUE_SEPARATOR = ':';
    static final char DEFAULT_NULL_SEPARATOR = '#';
    static final String DEFAULT_VALUE_SEPARATOR_STRING = ":";
    static final String DEFAULT_NULL_SEPARATOR_STRING = "#";

    @Override
    public T extract(Properties properties, Property property) throws PropertyExtractException {
        Objects.requireNonNull(properties);
        Objects.requireNonNull(property);

        final String fullPropertyName = property.value();
        if (StringUtils.isBlank(fullPropertyName)) {
            throw new PropertyExtractException(fullPropertyName, "Property name cannot be blank");
        }

        if (!(fullPropertyName.startsWith("${") && fullPropertyName.endsWith("}"))) {
            return convert(fullPropertyName);
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
            return convert(propertyKey, properties.get(propertyKey));
        }

        if (countOfDefValSeparators == 0 && countOfDefNullSeparators == 0) {
            throw new PropertyExtractException(parsedPropertyName, "Property not found");
        }

        if (countOfDefNullSeparators == 1) {
            return null;
        }

        final String[] keyVal = parsedPropertyName.split(DEFAULT_VALUE_SEPARATOR_STRING);
        //If split result returns 1 element it mean that we have property like "${key:}"
        return convert(keyVal[0], keyVal.length > 1 ? keyVal[1] : "");
    }

    private T convert(String propertyName, Object value) throws PropertyExtractException {
        try {
            return convert((String)value);
        } catch (Exception e) {
            throw new PropertyExtractException(propertyName, String.format("Failed to parse property value [%s]", value), e);
        }
    }

    protected abstract T convert(String value);

    private void validateSeparators(String propertyName, long countOfDefValSeparators, long countOfDefNullSeparators) throws PropertyExtractException {
        if (countOfDefValSeparators > 1 || countOfDefNullSeparators > 1
                || (countOfDefValSeparators > 0 && countOfDefNullSeparators > 0)
                || (countOfDefNullSeparators == 1 && !propertyName.contains("#null"))) {
            throwInvalidPropertyName(propertyName);
        }
    }

    private void validateNotBlankName(String propertyName) throws PropertyExtractException {
        if (StringUtils.isBlank(propertyName
                .split(DEFAULT_VALUE_SEPARATOR_STRING)[0]
                .split(DEFAULT_NULL_SEPARATOR_STRING)[0])) {
            throwInvalidPropertyName(propertyName);
        }
    }

    private void throwInvalidPropertyName(String propertyName) throws PropertyExtractException {
        throw new PropertyExtractException(propertyName, "Invalid property name. Please look at org.annoconf.Property#value() javadoc");
    }

}
