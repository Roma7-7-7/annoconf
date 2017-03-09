package org.annoconf.utils;

import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void count() {
        assertEquals(0, StringUtils.countOf(null, 'a'));
        assertEquals(0, StringUtils.countOf("abc", ':'));
        assertEquals(1, StringUtils.countOf(":sdge#", ':'));
        assertEquals(2, StringUtils.countOf("wf:2rcd:erg3", ':'));
    }

}
