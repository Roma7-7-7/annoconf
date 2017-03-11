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

    @Test
    public void stringCount() {
        assertEquals(0, StringUtils.countOf(null, "abv"));
        assertEquals(0, StringUtils.countOf(null, null));
        assertEquals(0, StringUtils.countOf(null, ""));
        assertEquals(0, StringUtils.countOf("", null));
        assertEquals(0, StringUtils.countOf("", ""));
        assertEquals(4, StringUtils.countOf("        ", "  "));
        assertEquals(0, StringUtils.countOf("", "abv"));
        assertEquals(0, StringUtils.countOf("", ""));
        assertEquals(0, StringUtils.countOf("abc", ":"));
        assertEquals(1, StringUtils.countOf(":sdg::e#", "::"));
        assertEquals(3, StringUtils.countOf(":sdg::e#", ":"));
        assertEquals(4, StringUtils.countOf("wf:2abcrcd:abcabceraabccg3", "abc"));
    }

}
