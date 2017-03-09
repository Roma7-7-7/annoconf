package org.annoconf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by roma on 3/8/17.
 */
public class PropertyBeanFactoryTest {

    @Test(expected = NullPointerException.class)
    public void getBeanWithNullArgument() {
        PropertyBeanFactory.getBean(null);
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void getBeanWithNonAnnotatedClass() {
        PropertyBeanFactory.getBean(NonAnnotatedClass.class);
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void getBeanEmptyAnnotatedClass() {
        PropertyBeanFactory.getBean(AnnotatedWithEmptyPath.class);
    }

    @Test
    public void getBeanCheckNotNull() {
        assertNotNull(PropertyBeanFactory.getBean(OneStringProperty.class));
    }

    @Test
    public void getBeanStringProperties() {
        final TwoStringProperties bean = PropertyBeanFactory.getBean(TwoStringProperties.class);
        assertEquals("str-prop-value", bean.getProp1());
        assertEquals("prop-value-2", bean.getProp2());
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void getBeanWithoutNoArgConstructor() {
        PropertyBeanFactory.getBean(BeanWithoutNoArgsConstructor.class);
    }

    @Test
    public void getBeanSingleProperty() {
        final OneStringProperty instance = PropertyBeanFactory.getBean(OneStringProperty.class);

        assertNotNull(instance);
        assertEquals("str-prop-value", instance.getProp());
    }

    @Test
    public void getBeanMultipleStringProperties() {
        final ThreeStringProperties instance = PropertyBeanFactory.getBean(ThreeStringProperties.class);

        assertNotNull(instance);
        assertEquals("str-prop-value", instance.getProp1());
        assertEquals("prop-value-2", instance.getProp2());
        assertEquals("p3", instance.getProp3());
        assertEquals("default-val", instance.getProp4());
        assertEquals(null, instance.getProp5());
        assertEquals("${prop6#null", instance.getProp6());
    }

    @Test
    public void getBeanWithInvalidPath() {
        try {
            PropertyBeanFactory.getBean(AnnotatedWithInvalidPath.class);
            fail("PropertyBeanBuildException must be thrown");
        } catch (PropertyBeanBuildException e) {
            assertNotNull(e);
            assertEquals("Failed to load properties from source [fake]. Properties bean class [class org.annoconf.AnnotatedWithInvalidPath]", e.getMessage());
        }
    }

    @Test
    public void getBeanWithInvalidPropertyName() {
        try {
            PropertyBeanFactory.getBean(InvalidPropertyName.class);
            fail("PropertyBeanBuildException must be thrown");
        } catch (PropertyBeanBuildException e) {
            assertEquals("Failed to build instance of property bean class [org.annoconf.InvalidPropertyName]. Cannot set field [invalidProperty]. Property not found. Property name [invalid]", e.getMessage());
        }
    }

}

class NonAnnotatedClass {}

@PropertySource("")
class AnnotatedWithEmptyPath {}

@PropertySource("fake")
class AnnotatedWithInvalidPath {}

class BeanWithoutNoArgsConstructor {
    public BeanWithoutNoArgsConstructor(String prop) {}
}

@PropertySource("/single.properties")
class InvalidPropertyName {

    @Property("${invalid}")
    private String invalidProperty;

}

@PropertySource("/single.properties")
class OneStringProperty {

    @Property("${str.prop}")
    private String prop;

    public String getProp() {
        return prop;
    }
}

@PropertySource("/two.properties")
class TwoStringProperties {

    @Property("${prop1}")
    private String prop1;
    @Property("${prop2}")
    private String prop2;

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }
}

@PropertySource("/three.properties")
class ThreeStringProperties {

    @Property("${str.prop}")
    private String prop1;
    @Property("${str.prop-2}")
    private String prop2;
    @Property("${str.p3}")
    private String prop3;
    @Property("${prop4:default-val}")
    private String prop4;
    @Property("${prop5#null}")
    private String prop5;
    @Property("${prop6#null")
    private String prop6;

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public String getProp3() {
        return prop3;
    }

    public String getProp4() {
        return prop4;
    }

    public String getProp5() {
        return prop5;
    }

    public String getProp6() {
        return prop6;
    }
}