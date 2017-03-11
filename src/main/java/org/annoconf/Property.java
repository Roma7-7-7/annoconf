package org.annoconf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by roma on 3/8/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Property {

    String value();

    String defaultValueSeparator() default ":";

    boolean defaultNull() default false;

}
