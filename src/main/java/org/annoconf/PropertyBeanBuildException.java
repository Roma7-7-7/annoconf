package org.annoconf;

/**
 * Created by roma on 3/8/17.
 */
public class PropertyBeanBuildException extends RuntimeException {

    public PropertyBeanBuildException() {
    }

    public PropertyBeanBuildException(String message) {
        super(message);
    }

    public PropertyBeanBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyBeanBuildException(Throwable cause) {
        super(cause);
    }

    public PropertyBeanBuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
