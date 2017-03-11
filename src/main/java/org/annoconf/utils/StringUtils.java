package org.annoconf.utils;

/**
 * Created by roma on 3/8/17.
 */
public class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static long countOf(String str, char search) {
        if (str == null) {
            return 0;
        }

        return str.chars()
                .filter(c -> c == search)
                .count();
    }

}
