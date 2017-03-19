package org.annoconf.source;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by roma on 3/19/17.
 */
public class BasicPropertiesSourceTest {

    private Properties properties = new Properties();
    private BasicPropertiesSource source;

    @Before
    public void before() {
        this.properties.put("key", "value");

        this.source = new BasicPropertiesSource(this.properties);
    }

    @Test
    public void hasValue() throws Exception {
        assertFalse(this.source.hasValue(null));
        assertFalse(this.source.hasValue(""));
        assertFalse(this.source.hasValue("  "));
        assertTrue(this.source.hasValue("key"));
    }

    @Test
    public void getValue() throws Exception {
        assertNull(this.source.getValue(null));
        assertNull(this.source.getValue(""));
        assertNull(this.source.getValue("  "));
        assertEquals("value", this.source.getValue("key"));
    }

}