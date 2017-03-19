package org.annoconf.source;

import org.annoconf.PropertyValueSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by roma on 3/19/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompositePropertySourceTest {

    @Mock
    private PropertyValueSource sourceFalse;
    @Mock
    private PropertyValueSource sourceValue1;
    @Mock
    private PropertyValueSource sourceValue2;

    @Before
    public void before() {
        mockSource(this.sourceFalse, false, null);
        mockSource(this.sourceValue1, true, "value1");
        mockSource(this.sourceValue2, true, "value2");
    }

    @Test
    public void hasValue() {
        assertFalse(new CompositePropertySource(this.sourceFalse, this.sourceFalse).hasValue("key"));
        assertFalse(new CompositePropertySource(this.sourceFalse).hasValue("key"));
    }

    @Test
    public void getValue() {
        assertEquals("value1", new CompositePropertySource(this.sourceValue1).getValue("key"));
        assertEquals("value1", new CompositePropertySource(this.sourceValue1, this.sourceFalse).getValue("key"));
        assertEquals("value1", new CompositePropertySource(this.sourceFalse, this.sourceValue1, this.sourceFalse).getValue("key"));
        assertEquals("value2", new CompositePropertySource(this.sourceFalse, this.sourceValue1, this.sourceFalse, this.sourceValue2).getValue("key"));
        assertEquals("value1", new CompositePropertySource(this.sourceFalse, this.sourceValue2, this.sourceValue2, this.sourceValue1).getValue("key"));
        assertEquals("value1", new CompositePropertySource(this.sourceValue2, this.sourceValue2, this.sourceValue1).getValue("key"));
    }

    private PropertyValueSource mockSource(PropertyValueSource source, boolean hasValue, String value) {
        when(source.hasValue(anyString())).thenReturn(hasValue);

        if (hasValue) {
            when(source.getValue(anyString())).thenReturn(value);
        }

        return source;
    }

}