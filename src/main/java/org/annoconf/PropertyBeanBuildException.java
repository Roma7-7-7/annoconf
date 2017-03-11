package org.annoconf;

import org.annoconf.exceptions.AnnoConfException;

/**
 * Created by roma on 3/8/17.
 */
public class PropertyBeanBuildException extends RuntimeException {

    public PropertyBeanBuildException(String message) {
        super(message);
    }

    public PropertyBeanBuildException(String message, Exception e) {
        super(message, e);
    }

    public PropertyBeanBuildException(String message, AnnoConfException e) {
        super(String.format("%s. %s", message, e.getMessage()), e);
    }
}
