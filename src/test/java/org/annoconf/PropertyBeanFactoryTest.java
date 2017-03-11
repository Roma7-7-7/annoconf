package org.annoconf;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

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
    public void getBeanWithDifferentPropertyTypes() throws ParseException {
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

        checkDate("yyyy-MM-dd", "2017-01-01", properties.getDate1());
        checkDate("yyyy-MM-dd HH:mm:ss", "2018-02-02 02:02:03", properties.getDate2());
        checkDate("yyyy-MM-dd", "2019-07-03", properties.getDate3());
        assertNull(properties.getDate4());

        assertEquals(LocalDate.of(2017, Month.FEBRUARY, 17), properties.getLocalDate1());
        assertEquals(LocalDate.of(2018, Month.FEBRUARY, 3), properties.getLocalDate2());
        assertNull(properties.getLocalDate3());

        assertEquals(LocalTime.of(2, 3, 7), properties.getLocalTime1());
        assertEquals(LocalTime.of(1, 2, 3), properties.getLocalTime2());
        assertEquals(LocalTime.of(2, 3, 4), properties.getLocalTime3());
        assertNull(properties.getLocalTime4());

        assertEquals(LocalDateTime.of(2017, Month.JULY, 31, 17, 7), properties.getLocalDateTime1());
        assertEquals(LocalDateTime.of(2007, Month.FEBRUARY, 1, 1, 3, 7), properties.getLocalDateTime2());
        assertNull(properties.getLocalDateTime3());
    }

    private void checkDate(String pattern, String expected, Date value) throws ParseException {
        assertEquals(new SimpleDateFormat(pattern).parse(expected), value);
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
    @Property(value = "${prop5}", defaultNull = true)
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
    @Property(value = "${str-six}", defaultNull = true)
    private String str6;

    @Property("${int-one}")
    private Integer int1;
    @Property("${int-two:2}")
    private Integer int2;
    @Property(value = "${int-three}", defaultNull = true)
    private Integer int3;
    @Property("${int-four:4}")
    private int int4;

    @Property("${long-one}")
    private Long long1;
    @Property("${long-two:2}")
    private Long long2;
    @Property(value = "${long-three}", defaultNull = true)
    private Long long3;
    @Property("${long-four:4}")
    private long long4;

    @Property("${float-one}")
    private Float float1;
    @Property("${float-two:2.2}")
    private Float float2;
    @Property(value = "${float-three}", defaultNull = true)
    private Float float3;
    @Property("${float-four:4.4}")
    private float float4;

    @Property("${double-one}")
    private Double double1;
    @Property("${double-two:22.22}")
    private Double double2;
    @Property(value = "${double-three}", defaultNull = true)
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
    @Property(value = "${boolean-six}", defaultNull = true)
    private Boolean boolean6;

    @Property("${date-one}")
    @PropertyDateTimeFormat("yyyy-MM-dd")
    private Date date1;
    @Property("${date-two}")
    @PropertyDateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date date2;
    @Property("${date-three:2019/07/03}")
    @PropertyDateTimeFormat("yyyy/MM/dd")
    private Date date3;
    @Property(value = "${date-four}", defaultNull = true)
    @PropertyDateTimeFormat("yyyy/MM/dd")
    private Date date4;

    @Property("${local-date-one}")
    @PropertyDateTimeFormat("yyyy/dd/MM")
    private LocalDate localDate1;
    @Property("${local-date-two:2018/03/02}")
    @PropertyDateTimeFormat("yyyy/dd/MM")
    private LocalDate localDate2;
    @Property(value = "${local-date-three}", defaultNull = true)
    @PropertyDateTimeFormat("yyyy/dd/MM")
    private LocalDate localDate3;

    @Property("${local-time-one}")
    @PropertyDateTimeFormat("HH:mm:ss")
    private LocalTime localTime1;
    @Property("${local-time-two:01 02 03}")
    @PropertyDateTimeFormat("HH mm ss")
    private LocalTime localTime2;
    @Property(value = "${local-time-three#02:03-04}", defaultValueSeparator = "#")
    @PropertyDateTimeFormat("HH:mm-ss")
    private LocalTime localTime3;
    @Property(value = "${local-time-four}", defaultNull = true)
    @PropertyDateTimeFormat("$HH:mm:ss")
    private LocalTime localTime4;

    @Property("${local-date-time-one}")
    @PropertyDateTimeFormat("dd/MM/yyyy HH:mm")
    private LocalDateTime localDateTime1;
    @Property(value = "${local-date-time-two?01/02/2007 01-03^07}", defaultValueSeparator = "?")
    @PropertyDateTimeFormat("dd/MM/yyyy HH-mm^ss")
    private LocalDateTime localDateTime2;
    @Property(value = "${local-date-time-three}", defaultNull = true)
    @PropertyDateTimeFormat("dd/MM/yyyy HH:ss")
    private LocalDateTime localDateTime3;

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

    public Date getDate1() {
        return date1;
    }

    public Date getDate2() {
        return date2;
    }

    public Date getDate3() {
        return date3;
    }

    public Date getDate4() {
        return date4;
    }

    public LocalDate getLocalDate1() {
        return localDate1;
    }

    public LocalDate getLocalDate2() {
        return localDate2;
    }

    public LocalDate getLocalDate3() {
        return localDate3;
    }

    public LocalTime getLocalTime1() {
        return localTime1;
    }

    public LocalTime getLocalTime2() {
        return localTime2;
    }

    public LocalTime getLocalTime3() {
        return localTime3;
    }

    public LocalTime getLocalTime4() {
        return localTime4;
    }

    public LocalDateTime getLocalDateTime1() {
        return localDateTime1;
    }

    public LocalDateTime getLocalDateTime2() {
        return localDateTime2;
    }

    public LocalDateTime getLocalDateTime3() {
        return localDateTime3;
    }
}