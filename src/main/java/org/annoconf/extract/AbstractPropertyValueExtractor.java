package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.PropertyValueSource;
import org.annoconf.exceptions.PropertyExtractException;
import org.annoconf.utils.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by roma on 3/9/17.
 */
abstract class AbstractPropertyValueExtractor<T> implements PropertyValueExtractor {

    @Override
    public T extract(PropertyValueSource source, Property property) throws PropertyExtractException {
        Objects.requireNonNull(source);
        Objects.requireNonNull(property);

        final String fullPropertyName = property.value();
        if (StringUtils.isBlank(fullPropertyName)) {
            throw new PropertyExtractException(fullPropertyName, "Property name cannot be blank");
        }

        if (!(fullPropertyName.startsWith("${") && fullPropertyName.endsWith("}"))) {
            return convert(fullPropertyName, fullPropertyName);
        }

        final String defaultValueSeparator = property.defaultValueSeparator();
        final String quotedSeparator = Pattern.quote(defaultValueSeparator);

        //Removing "${" and "}"
        final String parsedPropertyName = fullPropertyName.substring(2, fullPropertyName.length() - 1);
        final long countOfDefValSeparators = StringUtils.countOf(parsedPropertyName, defaultValueSeparator);

        if ((countOfDefValSeparators > 1)
                || (countOfDefValSeparators == 1 && property.defaultNull())
                || StringUtils.isBlank(parsedPropertyName.split(quotedSeparator)[0])) {
            throw new PropertyExtractException(parsedPropertyName,
                    "Invalid property name. Please look at org.annoconf.Property#value() javadoc");
        }

        final String propertyKey = parsedPropertyName.split(quotedSeparator)[0];

        if (source.hasValue(propertyKey)) {
            return convert(propertyKey, source.getValue(propertyKey));
        }

        if (countOfDefValSeparators == 0) {
            if (property.defaultNull()) {
                return null;
            }
            throw new PropertyExtractException(parsedPropertyName, "Property not found");
        }

        final String[] keyVal = parsedPropertyName.split(quotedSeparator);
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

    protected abstract T convert(String value) throws Exception;

}
