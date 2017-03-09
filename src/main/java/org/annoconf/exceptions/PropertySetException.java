package org.annoconf.exceptions;

/**
 * Created by roma on 3/9/17.
 */
public class PropertySetException extends AnnoConfException {

    public PropertySetException(String property, String message) {
        super(String.format("%s. Property name [%s]", message, property));
    }

}
