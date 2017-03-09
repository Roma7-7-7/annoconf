package org.annoconf;

import org.annoconf.exceptions.AnnoConfException;
import org.annoconf.exceptions.PropertiesLoadException;
import org.annoconf.utils.ReflectionUtils;
import org.annoconf.utils.StringUtils;

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
            return PropertiesProvider.loadFrom(path);
        } catch (PropertiesLoadException e) {
            throw new PropertyBeanBuildException(String.format("Failed to build instance of property bean class [%s]", clazz), e);
        }
    }

    private static void setPropertyValue(Object obj, Field field, Properties properties) {
        try {
            final Property annotation = ReflectionUtils.getAnnotation(field, Property.class);
            final String value = PropertyValueExtractor.INSTANCE.extract(properties, annotation);
            ReflectionUtils.setFieldValue(obj, field, value);
        } catch (AnnoConfException e) {
            throw new PropertyBeanBuildException(String.format("Failed to build instance of property bean class [%s]. Cannot set field [%s]", obj.getClass().getName(), field.getName()), e);
        }

    }

}