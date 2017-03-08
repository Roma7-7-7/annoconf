package org.annoconf.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by roma on 3/8/17.
 */
public class StringUtilsTest {

    @Test
    public void isBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank("   "));
        assertFalse(StringUtils.isBlank(".   "));
        assertFalse(StringUtils.isBlank("   /"));
        assertFalse(StringUtils.isBlank("  s "));
    }

}
