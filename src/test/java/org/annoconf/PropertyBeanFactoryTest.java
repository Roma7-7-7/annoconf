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
        PropertyBeanFactory.getBean(NonAnnotated.class);
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void getBeanEmptyAnnotatedClass() {
        PropertyBeanFactory.getBean(EmptyAnnotated.class);
    }

    @Test
    public void getBeanCheckNotNull() {
        assertNotNull(PropertyBeanFactory.getBean(SingleStringPropertyBean.class));
    }

    @Test
    public void getBeanStringProperties() {
        final BeanWithStringArgs bean = PropertyBeanFactory.getBean(BeanWithStringArgs.class);
        assertEquals("str-prop-value", bean.getProp1());
        assertEquals("prop-value-2", bean.getProp2());
    }

    @Test(expected = PropertyBeanBuildException.class)
    public void getBeanWithoutNoArgConstructor() {
        PropertyBeanFactory.getBean(BeanWithoutNoArgsConstructor.class);
    }

    @Test
    public void getBeanSingleProperty() {
        final SingleStringPropertyBean instance = PropertyBeanFactory.getBean(SingleStringPropertyBean.class);

        assertNotNull(instance);
        assertEquals("str-prop-value", instance.getProp());
    }

    @Test
    public void getBeanMultipleStringProperties() {
        final MultipleStringPropertiesBean instance = PropertyBeanFactory.getBean(MultipleStringPropertiesBean.class);

        assertNotNull(instance);
        assertEquals("str-prop-value", instance.getProp1());
        assertEquals("prop-value-2", instance.getProp2());
        assertEquals("p3", instance.getProp3());
    }

    @Test
    public void getBeanWithInvalidPath() {
        try {
            PropertyBeanFactory.getBean(InvalidPath.class);
            fail("PropertyBeanBuildException must be thrown");
        } catch (PropertyBeanBuildException e) {
            assertNotNull(e);
            assertEquals("Failed to load properties from source [fake]. Properties bean class [class org.annoconf.InvalidPath]", e.getMessage());
        }
    }

}

class NonAnnotated {

}

@PropertySource("")
class EmptyAnnotated {

}

@PropertySource("")
class BeanWithoutArgsEmptyPath {

}

@PropertySource("/two-props.properties")
class BeanWithStringArgs {

    @Property("prop1")
    private String prop1;
    @Property("prop2")
    private String prop2;

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }
}

class BeanWithoutNoArgsConstructor {

    private String prop;

    public BeanWithoutNoArgsConstructor(String prop) {
        this.prop = prop;
    }
}

@PropertySource("/single-property.properties")
class SingleStringPropertyBean {

    @Property("str.prop")
    private String prop;

    public String getProp() {
        return prop;
    }
}

@PropertySource("/multiple-string.properties")
class MultipleStringPropertiesBean {

    @Property("str.prop")
    private String prop1;
    @Property("str.prop-2")
    private String prop2;
    @Property("str.p3")
    private String prop3;

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public String getProp3() {
        return prop3;
    }
}

@PropertySource("fake")
class InvalidPath {}