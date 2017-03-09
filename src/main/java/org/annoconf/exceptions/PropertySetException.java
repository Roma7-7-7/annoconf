package org.annoconf.exceptions;

/**
 * Created by roma on 3/9/17.
 */
public class PropertySetException extends AnnoConfException {

    private String property;

    public PropertySetException(String property, String message) {
        super(message);
        this.property = property;
    }

    @Override
    public String getMessage() {
        return String.format("%s. Property name [%s]", super.getMessage(), this.property);
    }
}
