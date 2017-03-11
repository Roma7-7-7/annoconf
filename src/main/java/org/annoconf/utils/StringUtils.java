package org.annoconf.utils;

/**
 * Created by roma on 3/8/17.
 */
public class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static long countOf(String str, String search) {
        if (str == null || search == null || str.length() == 0) {
            return 0;
        }

        int result = 0;
        int i = 0;

        do {
            i = str.indexOf(search, i);

            if (i != -1) {
                result++;
                i += search.length();
            }
        } while (i != -1);

        return result;
    }

    public static long countOf(String str, char search) {
        if (str == null) {
            return 0;
        }

        return countOf(str, String.valueOf(search));
    }

}
