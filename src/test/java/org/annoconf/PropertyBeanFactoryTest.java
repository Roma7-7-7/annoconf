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
            assertEquals("Failed to build instance of property bean class [class org.annoconf.AnnotatedWithInvalidPath]. Failed to load properties from path [fake]", e.getMessage());
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

    @Test
    public void getBeanWithDifferentPropertyTypes() {
        final DifferentProperties properties = PropertyBeanFactory.getBean(DifferentProperties.class);

        assertEquals("str-value", properties.getStr1());
        assertEquals("${str-two", properties.getStr2());
        assertEquals("str-three}", properties.getStr3());
        assertEquals("str-four", properties.getStr4());
        assertEquals("str-value-5", properties.getStr5());
        assertEquals(null, properties.getStr6());

        assertEquals(Integer.valueOf(63), properties.getInt1());
        assertEquals(Integer.valueOf(2), properties.getInt2());
        assertEquals(null, properties.getInt3());
        assertEquals(4, properties.getInt4());

        assertEquals(Long.valueOf(83), properties.getLong1());
        assertEquals(Long.valueOf(2), properties.getLong2());
        assertEquals(null, properties.getLong3());
        assertEquals(4, properties.getLong4());

        assertEquals(Float.valueOf(52.7f), properties.getFloat1());
        assertEquals(Float.valueOf(2.2f), properties.getFloat2());
        assertEquals(null, properties.getFloat3());
        assertEquals(4.4f, properties.getFloat4(), 0);

        assertEquals(Double.valueOf(746.25), properties.getDouble1());
        assertEquals(Double.valueOf(22.22), properties.getDouble2());
        assertEquals(null, properties.getDouble3());
        assertEquals(44.44, properties.getDouble4(), 0);

        assertTrue(properties.getBoolean1());
        assertFalse(properties.getBoolean2());
        assertFalse(properties.getBoolean3());
        assertTrue(properties.getBoolean4());
        assertFalse(properties.getBoolean5());
        assertNull(properties.getBoolean6());
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

@PropertySource("single.properties")
class InvalidPropertyName {

    @Property("${invalid}")
    private String invalidProperty;

}

@PropertySource("single.properties")
class OneStringProperty {

    @Property("${str.prop}")
    private String prop;

    public String getProp() {
        return prop;
    }
}

@PropertySource("classpath:two.properties")
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

@PropertySource("three.properties")
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

@PropertySource("different-types.properties")
class DifferentProperties {

    @Property("${str-one}")
    private String str1;
    @Property("${str-two")
    private String str2;
    @Property("str-three}")
    private String str3;
    @Property("str-four")
    private String str4;
    @Property("${str-five:str-value-5}")
    private String str5;
    @Property("${str-six#null}")
    private String str6;

    @Property("${int-one}")
    private Integer int1;
    @Property("${int-two:2}")
    private Integer int2;
    @Property("${int-three#null}")
    private Integer int3;
    @Property("${int-four:4}")
    private int int4;

    @Property("${long-one}")
    private Long long1;
    @Property("${long-two:2}")
    private Long long2;
    @Property("${long-three#null}")
    private Long long3;
    @Property("${long-four:4}")
    private long long4;

    @Property("${float-one}")
    private Float float1;
    @Property("${float-two:2.2}")
    private Float float2;
    @Property("${float-three#null}")
    private Float float3;
    @Property("${float-four:4.4}")
    private float float4;

    @Property("${double-one}")
    private Double double1;
    @Property("${double-two:22.22}")
    private Double double2;
    @Property("${double-three#null}")
    private Double double3;
    @Property("${double-four:44.44}")
    private double double4;

    @Property("${boolean-one}")
    private Boolean boolean1;
    @Property("${boolean-two}")
    private Boolean boolean2;
    @Property("${boolean-three:false}")
    private Boolean boolean3;
    @Property("${boolean-four:true}")
    private Boolean boolean4;
    @Property("${boolean-five:false}")
    private boolean boolean5;
    @Property("${boolean-six#null}")
    private Boolean boolean6;

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    public String getStr3() {
        return str3;
    }

    public String getStr4() {
        return str4;
    }

    public String getStr5() {
        return str5;
    }

    public String getStr6() {
        return str6;
    }

    public Integer getInt1() {
        return int1;
    }

    public Integer getInt2() {
        return int2;
    }

    public Integer getInt3() {
        return int3;
    }

    public int getInt4() {
        return int4;
    }

    public Long getLong1() {
        return long1;
    }

    public Long getLong2() {
        return long2;
    }

    public Long getLong3() {
        return long3;
    }

    public long getLong4() {
        return long4;
    }

    public Float getFloat1() {
        return float1;
    }

    public Float getFloat2() {
        return float2;
    }

    public Float getFloat3() {
        return float3;
    }

    public float getFloat4() {
        return float4;
    }

    public Double getDouble1() {
        return double1;
    }

    public Double getDouble2() {
        return double2;
    }

    public Double getDouble3() {
        return double3;
    }

    public double getDouble4() {
        return double4;
    }

    public Boolean getBoolean1() {
        return boolean1;
    }

    public Boolean getBoolean2() {
        return boolean2;
    }

    public Boolean getBoolean3() {
        return boolean3;
    }

    public Boolean getBoolean4() {
        return boolean4;
    }

    public boolean getBoolean5() {
        return boolean5;
    }

    public Boolean getBoolean6() {
        return boolean6;
    }
}