package org.annoconf.utils;

import org.annoconf.Property;
import org.annoconf.PropertyBeanBuildException;
import org.annoconf.PropertySource;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by roma on 3/8/17.
 */
public class ReflectionUtilsTest {

    @Test(expected = NullPointerException.class)
    public void newInstanceNullArg() {
        ReflectionUtils.newInstance(null);
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void newInstanceNoDefaulConstructor() {
        ReflectionUtils.newInstance(ClassWithNonDefConstructor.class);
    }

    @Test
    public void newInstance() {
        assertNotNull(ReflectionUtils.newInstance(ClassWithultipleConstructorss.class));
    }

    @Test
    public void setFieldValue() {
        final BaseClass instance = new BaseClass("value1");

        assertTrue(instance == ReflectionUtils.setFieldValue(
                instance, BaseClass.class.getDeclaredFields()[0], "value2"));
        assertEquals("value2", instance.getField());
    }

    @Test(expected = NullPointerException.class)
    public void getFieldAnnotationNullField() {
        ReflectionUtils.getAnnotation((Field)null, null);
    }

    @Test(expected = NullPointerException.class)
    public void getFieldAnnotationNullAnnotation() {
        ReflectionUtils.getAnnotation(BaseClass.class.getDeclaredFields()[0], null);
    }

    @Test
    public void getFieldAnnotation() {
        class SomeClass {
            private String field1;
            @Property("value-2")
            private Objects field2;
            @Deprecated
            private Boolean field3;
        }
        final Field[] fields = SomeClass.class.getDeclaredFields();

        assertNull(ReflectionUtils.getAnnotation(fields[0], Property.class));
        assertEquals("value-2", ReflectionUtils.getAnnotation(fields[1], Property.class).value());
        assertNull(ReflectionUtils.getAnnotation(fields[2], Property.class));
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotationWithNullObject() {
        ReflectionUtils.getAnnotation((Object) null, Property.class);
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotationWithNullAnnotation() {
        ReflectionUtils.getAnnotation(new Object(), null);
    }

    @Test
    public void getAnnotationNonAnnotatedClass() {
        assertNull(ReflectionUtils.getAnnotation(new Object(), PropertySource.class));
    }

    @Test
    public void getAnnotation() {
        final PropertySource annotation = ReflectionUtils.getAnnotation(new AnnotatedClass(), PropertySource.class);

        assertNotNull(annotation);
        assertEquals("annotation-value", annotation.value());
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotationWithNullObjectClass() {
        ReflectionUtils.getAnnotation((Class)null, Property.class);
    }

    @Test(expected = NullPointerException.class)
    public void getAnnotationWithNullAnnotation2() {
        ReflectionUtils.getAnnotation(Object.class, null);
    }

    @Test
    public void getAnnotationNonAnnotatedClass2() {
        assertNull(ReflectionUtils.getAnnotation(Object.class, PropertySource.class));
    }

    @Test
    public void getAnnotation2() {
        final PropertySource annotation = ReflectionUtils.getAnnotation(AnnotatedClass.class, PropertySource.class);

        assertNotNull(annotation);
        assertEquals("annotation-value", annotation.value());
    }

    @Test(expected = NullPointerException.class)
    public void getPropertyFieldsNullArgument() {
        ReflectionUtils.getPropertyFields(null);
    }

    @Test
    public void getPropertyFieldsEmptyClass() {
        assertEquals(0, ReflectionUtils.getPropertyFields(EmptyClass.class).size());
    }

    @Test
    public void getPropertyFields() {
        class SomeClass {

            @Property("")
            private Object property1;
            private String property2;
            @Property("")
            private String property3;
            private Boolean property4;
            @Property("")
            private LocalDateTime property5;
            private Class<?> clazz;
        }

        final List<Field> fields = ReflectionUtils.getPropertyFields(SomeClass.class);
        assertEquals(3, fields.size());
        assertEquals("property1", fields.get(0).getName());
        assertEquals("property3", fields.get(1).getName());
        assertEquals("property5", fields.get(2).getName());
    }

}

class BaseClass {

    private String field;

    public BaseClass(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

@PropertySource("annotation-value")
class AnnotatedClass {

}

class ClassWithultipleConstructorss {
    private ClassWithultipleConstructorss() {}
    private ClassWithultipleConstructorss(String s) {}
}

class ClassWithNonDefConstructor {
    private ClassWithNonDefConstructor(String s) {}
}

class EmptyClass {}