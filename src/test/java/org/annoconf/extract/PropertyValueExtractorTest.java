package org.annoconf.extract;

import org.annoconf.Property;
import org.annoconf.exceptions.AnnoConfException;
import org.annoconf.exceptions.PropertyExtractException;
import org.annoconf.stub.PropertyValueSourceStub;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by roma on 3/9/17.
 */
public class PropertyValueExtractorTest {

    private PropertyValueSourceStub source = new PropertyValueSourceStub();
    private PropertyValueExtractor<String> extractor;

    @Before
    public void before() {
        final Field field = StringPropertyClass.class.getDeclaredFields()[0];
        this.extractor = PropertyValueExtractorFactory.getExtractor(field);
        source.setProperty("prop1", "value1");
        source.setProperty("prop2", "value2");
        source.setProperty("prop6", "value6");
        source.setProperty("prop7", "value7");
        source.setProperty("p", "v");
    }

    @Test(expected = NullPointerException.class)
    public void extractNullProperties() throws AnnoConfException {
        this.extractor.extract(null, mock(Property.class));
    }

    @Test(expected = NullPointerException.class)
    public void extractNullAnnotation() throws AnnoConfException {
        this.extractor.extract(new PropertyValueSourceStub(), null);
    }

    @Test
    public void extractWithBlankAnnotationValue() {
        try {
            this.extractor.extract(this.source, mockAnnotation("  "));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            assertEquals("Property name cannot be blank. Property name [  ]", e.getMessage());
        }
    }

    @Test
    public void extractWithNonDefinedPropertyName() throws AnnoConfException {
        assertEquals("prop-name", this.extractor.extract(this.source, mockAnnotation("prop-name")));
        assertEquals("${prop-name", this.extractor.extract(this.source, mockAnnotation("${prop-name")));
        assertEquals("prop-name}", this.extractor.extract(this.source, mockAnnotation("prop-name}")));
    }

    @Test
    public void extractWithNotFoundPropertyValue() {
        try {
            this.extractor.extract(this.source, mockAnnotation("${non-defined-property}"));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            assertEquals("Property not found. Property name [non-defined-property]", e.getMessage());
        }
    }

    @Test
    public void extractWthInvalidPropertyName() {
        checkIsInvalid("${name:value:value2}");
        checkIsInvalid("${name::value}");
        checkIsInvalid("${}");
        checkIsInvalid("${:def}");
        checkIsInvalid("${:#null}");
        checkIsInvalid("${   }");
    }

    @Test
    public void extractWithInvalidPropertyConfigurationMustThrowException() {
        try {
            this.extractor.extract(this.source, mockAnnotation("${fake-prop:def}", true));
            fail("PropertyExtractException must be thrown");
        } catch (PropertyExtractException e) {
            assertEquals("Invalid property name. Please look at org.annoconf.Property#value() javadoc. Property name [fake-prop:def]", e.getMessage());
        }
    }

    @Test
    public void extract() throws AnnoConfException {
        assertEquals("value1", this.extractor.extract(this.source, mockAnnotation("${prop1}")));
        assertEquals("value2", this.extractor.extract(this.source, mockAnnotation("${prop2}")));
        assertEquals("default3", this.extractor.extract(this.source, mockAnnotation("${prop3:default3}")));
        assertEquals("default31", this.extractor.extract(this.source, mockAnnotation("${prop3?default31}", "?")));
        assertEquals(null, this.extractor.extract(this.source, mockAnnotation("${prop4}", true)));
        assertEquals("value5", this.extractor.extract(this.source, mockAnnotation("${prop5 value5}", " ")));
        assertEquals("value6", this.extractor.extract(this.source, mockAnnotation("${prop6:default6}")));
        assertEquals("value7", this.extractor.extract(this.source, mockAnnotation("${prop7}", true)));
        assertEquals("v", this.extractor.extract(this.source, mockAnnotation("${p}")));
        assertEquals("d", this.extractor.extract(this.source, mockAnnotation("${d:d}")));
        assertEquals(null, this.extractor.extract(this.source, mockAnnotation("${n}", true)));
        assertEquals("", this.extractor.extract(this.source, mockAnnotation("${e:}")));
        assertEquals("#null", this.extractor.extract(this.source, mockAnnotation("${e:#null}")));
        assertEquals("##null", this.extractor.extract(this.source, mockAnnotation("${e:##null}")));
    }

    private void checkIsInvalid(String name) {
        try {
            this.extractor.extract(this.source, mockAnnotation(name));
            fail("AnnoConfException must be thrown");
        } catch (AnnoConfException e) {
            if (name.startsWith("${") && name.endsWith("}")) {
                name = name.substring(2, name.length() - 1);
            }
            assertEquals(String.format("Invalid property name. Please look at org.annoconf.Property#value() javadoc. Property name [%s]", name), e.getMessage());
        }
    }

    private Property mockAnnotation(String value) {
        return mockAnnotation(value, ":");
    }

    private Property mockAnnotation(String value, boolean defaultNull) {
        return mockAnnotation(value, ":", defaultNull);
    }

    private Property mockAnnotation(String value, String separator) {
        return mockAnnotation(value, separator, false);
    }

    private Property mockAnnotation(String value, String separator, boolean defaultNull) {
        final Property result = mock(Property.class);
        when(result.value()).thenReturn(value);
        when(result.defaultValueSeparator()).thenReturn(separator);
        when(result.defaultNull()).thenReturn(defaultNull);
        return result;
    }

    private static class StringPropertyClass {
        private String property;
    }

}