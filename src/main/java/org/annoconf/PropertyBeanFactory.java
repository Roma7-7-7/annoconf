package org.annoconf;

import org.annoconf.exceptions.AnnoConfException;
import org.annoconf.utils.ReflectionUtils;
import org.annoconf.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by roma on 3/8/17.
 */
public class PropertyBeanFactory {

    public static <T> T getBean(Class<T> clazz) {
        Objects.requireNonNull(clazz);

        final PropertySource annotation = ReflectionUtils.getAnnotation(clazz, PropertySource.class);
        if (annotation == null) {
            throw new PropertyBeanBuildException(String.format("Class [%s] is not annotated org.annoconf.PropertySource annotation", clazz));
        }
        if (StringUtils.isBlank(annotation.value())) {
            throw new PropertyBeanBuildException(String.format("PropertySource value of class [%s] is blank", clazz));
        }

        final T result = ReflectionUtils.newInstance(clazz);
        final Properties properties = loadProperties(annotation.value(), clazz);

        ReflectionUtils.getPropertyFields(clazz)
                .forEach(f -> setPropertyValue(result, f, properties));

        return result;
    }

    private static Properties loadProperties(String path, Class<?> clazz) {
        try {
            final Properties result = new Properties();
            result.load(getResource(path, clazz));
            return result;
        } catch (IOException e) {
            throw new PropertyBeanBuildException(String.format("Failed to load properties from source [%s]. Properties bean class [%s]", path, clazz));
        }
    }

    private static InputStream getResource(String path, Class<?> clazz) {
        final InputStream result = PropertyBeanFactory.class.getResourceAsStream(path);

        if (result == null) {
            throw new PropertyBeanBuildException(String.format("Failed to load properties from source [%s]. Properties bean class [%s]", path, clazz));
        }

        return result;
    }

    private static void setPropertyValue(Object obj, Field field, Properties properties) {
        try {
            final Property annotation = ReflectionUtils.getAnnotation(field, Property.class);
            final String value = PropertyValueExtractor.INSTANCE.extract(properties, annotation);
            ReflectionUtils.setFieldValue(obj, field, value);
        } catch (AnnoConfException e) {
            throw new PropertyBeanBuildException(String.format("Failed to build instance of property bean class [%s]. Cannot set field [%s]. %s", obj.getClass().getName(), field.getName(), e.getMessage()));
        }

    }

}