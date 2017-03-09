package org.annoconf.exceptions;

/**
 * Created by roma on 3/9/17.
 */
public class PropertyExtractException extends AnnoConfException {

    public PropertyExtractException(String property, String message) {
        super(formatMessage(property, message));
    }

    public PropertyExtractException(String property, String message, Exception e) {
        super(formatMessage(property, message), e);
    }

    private static String formatMessage(String property, String message) {
        return String.format("%s. Property name [%s]", message, property);
    }

}
