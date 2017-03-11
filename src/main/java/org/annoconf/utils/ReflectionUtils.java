package org.annoconf.utils;

import org.annoconf.Property;
import org.annoconf.PropertyBeanBuildException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by roma on 3/8/17.
 */
public class ReflectionUtils {

    private ReflectionUtils() {}

    public static <T> T newInstance(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new PropertyBeanBuildException(
                    String.format("Property bean class [%s] must contain constructor without arguments", clazz), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PropertyBeanBuildException(String.format("Failed to build instance of bean [%s]", clazz), e);
        }
    }

    public static <A extends Annotation> A getAnnotation(Object obj, Class<A> annotation) {
        Objects.requireNonNull(obj);

        return obj.getClass().getDeclaredAnnotation(annotation);
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotation) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(annotation);

        return clazz.getDeclaredAnnotation(annotation);
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotation) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(annotation);

        return field.getDeclaredAnnotation(annotation);
    }

    public static <T> T setFieldValue(T obj, Field field, Object value) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(field);

        try {
            field.setAccessible(true);
            field.set(obj, value);
            return obj;
        } catch (Exception e) {
            throw new PropertyBeanBuildException(
                    String.format(
                            "Failed to set instance value. Object class: [%s]; field: [%s]; value: [%s]", obj.getClass(), field, value));
        }
    }

    public static List<Field> getPropertyFields(Class<?> clazz) {
        Objects.requireNonNull(clazz);

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> isAnnotated(f, Property.class))
                .collect(Collectors.toList());
    }

    private static <A extends Annotation> boolean isAnnotated(Field field, Class<A> annotation) {
        return field.getDeclaredAnnotation(annotation) != null;
    }

}
