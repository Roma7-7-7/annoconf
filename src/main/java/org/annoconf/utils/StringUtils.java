package org.annoconf.utils;

/**
 * Created by roma on 3/8/17.
 */
public class StringUtils {

    private StringUtils() {};

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().length() == 0;
    }

}
