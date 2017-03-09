package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.exceptions.AnnoConfException;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by roma on 3/9/17.
 */
public class PropertyValueExtractorTest {

    private Properties properties = new Properties();
    private PropertyValueExtractor<String> extractor = PropertyValueExtractorFactory.getExtractor(String.class);

    @Before
    public void before() {
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");
        properties.setProperty("prop6", "value6");
        properties.setProperty("prop7", "value7");
        properties.setProperty("p", "v");
    }

    @Test(expected = NullPointerException.class)
    public void extractNullProperties() throws AnnoConfException {
        this.extractor.extract(null, mock(Property.class));
    }

    @Test(expected = NullPointerException.class)
    public void extractNullAnnotation() throws AnnoConfException {
        this.extractor.extract(new Properties(), null);
    }

    @Test
    public void extractWithBlankAnnotationValue() {
        try {
            this.extractor.extract(this.properties, mockAnnotation("  "));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            assertEquals("Property name cannot be blank. Property name [  ]", e.getMessage());
        }
    }

    @Test
    public void extractWithNonDefinedPropertyName() throws AnnoConfException {
        assertEquals("prop-name", this.extractor.extract(this.properties, mockAnnotation("prop-name")));
        assertEquals("${prop-name", this.extractor.extract(this.properties, mockAnnotation("${prop-name")));
        assertEquals("prop-name}", this.extractor.extract(this.properties, mockAnnotation("prop-name}")));
    }

    @Test
    public void extractWithNotFoundPropertyValue() {
        try {
            this.extractor.extract(this.properties, mockAnnotation("${non-defined-property}"));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            assertEquals("Property not found. Property name [non-defined-property]", e.getMessage());
        }
    }

    @Test
    public void extractWthInvalidPropertyName() {
        checkIsInvalid("${name:value:value2}");
        checkIsInvalid("${name::value}");
        checkIsInvalid("${name:##value}");
        checkIsInvalid("${name:#value1#value}");
        checkIsInvalid("${name:#a}");
        checkIsInvalid("${name:#value}");
        checkIsInvalid("${name:#null}");
        checkIsInvalid("${}");
        checkIsInvalid("${:def}");
        checkIsInvalid("${#wfef}");
        checkIsInvalid("${#null}");
        checkIsInvalid("${:#null}");
        checkIsInvalid("${   }");
    }

    @Test
    public void extract() throws AnnoConfException {
        assertEquals("value1", this.extractor.extract(this.properties, mockAnnotation("${prop1}")));
        assertEquals("value2", this.extractor.extract(this.properties, mockAnnotation("${prop2}")));
        assertEquals("default3", this.extractor.extract(this.properties, mockAnnotation("${prop3:default3}")));
        assertEquals(null, this.extractor.extract(this.properties, mockAnnotation("${prop4#null}")));
        assertEquals("value5", this.extractor.extract(this.properties, mockAnnotation("${prop5:value5}")));
        assertEquals("value6", this.extractor.extract(this.properties, mockAnnotation("${prop6:default6}")));
        assertEquals("value7", this.extractor.extract(this.properties, mockAnnotation("${prop7#null}")));
        assertEquals("v", this.extractor.extract(this.properties, mockAnnotation("${p}")));
        assertEquals("d", this.extractor.extract(this.properties, mockAnnotation("${d:d}")));
        assertEquals(null, this.extractor.extract(this.properties, mockAnnotation("${n#null}")));
        assertEquals("", this.extractor.extract(this.properties, mockAnnotation("${e:}")));
    }

    private void checkIsInvalid(String name) {
        try {
            this.extractor.extract(this.properties, mockAnnotation(name));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            if (name.startsWith("${") && name.endsWith("}")) {
                name = name.substring(2, name.length() - 1);
            }
            assertEquals(String.format("Invalid property name. Please look at org.annoconf.Property#value() javadoc. Property name [%s]", name), e.getMessage());
        }
    }

    private Property mockAnnotation(String value) {
        final Property result = mock(Property.class);
        when(result.value()).thenReturn(value);
        return result;
    }

}