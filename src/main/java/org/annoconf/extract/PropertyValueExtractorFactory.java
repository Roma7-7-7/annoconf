package org.annoconf.extract;

import java.util.Objects;

/**
 * Created by roma on 3/9/17.
 */
public class PropertyValueExtractorFactory {

    private static PropertyValueExtractor<String> STRING_EXTRACTOR = new StringExtractor();
    private static PropertyValueExtractor<Integer> INTEGER_EXTRACTOR = new IntegerExtractor();
    private static PropertyValueExtractor<Long> LONG_EXTRACTOR = new LongExtractor();
    private static PropertyValueExtractor<Float> FLOAT_EXTRACTOR = new FloatExtractor();
    private static PropertyValueExtractor<Double> DOUBLE_EXTRACTOR = new DoubleExtractor();
    private static PropertyValueExtractor<Double> BOOLEAN_EXTRACTOR = new BooleanExtractor();

    public static <T> PropertyValueExtractor<T> getExtractor(Class<T> clazz) {
        Objects.requireNonNull(clazz);

        if (String.class == clazz) {
            return (PropertyValueExtractor<T>) STRING_EXTRACTOR;
        }
        if (Integer.class == clazz || int.class == clazz) {
            return (PropertyValueExtractor<T>) INTEGER_EXTRACTOR;
        }
        if (Long.class == clazz || long.class == clazz) {
            return (PropertyValueExtractor<T>) LONG_EXTRACTOR;
        }
        if (Float.class == clazz || float.class == clazz) {
            return (PropertyValueExtractor<T>) FLOAT_EXTRACTOR;
        }
        if (Double.class == clazz || double.class == clazz) {
            return (PropertyValueExtractor<T>) DOUBLE_EXTRACTOR;
        }
        if (Boolean.class == clazz || boolean.class == clazz) {
            return (PropertyValueExtractor<T>) BOOLEAN_EXTRACTOR;
        }

        throw new IllegalArgumentException(String.format("Property class [%s] is not supported", clazz.getName()));
    }

    private static class StringExtractor extends AbstractPropertyValueExtractor<String>{

        @Override
        protected String convert(String value) {
            return value;
        }
    }

    private static class IntegerExtractor extends AbstractPropertyValueExtractor<Integer>{

        @Override
        protected Integer convert(String value) {
            return Integer.valueOf(value);
        }

    }

    private static class LongExtractor extends AbstractPropertyValueExtractor<Long>{

        @Override
        protected Long convert(String value) {
            return Long.valueOf(value);
        }

    }

    private static class FloatExtractor extends AbstractPropertyValueExtractor<Float>{

        @Override
        protected Float convert(String value) {
            return Float.valueOf(value);
        }

    }

    private static class DoubleExtractor extends AbstractPropertyValueExtractor<Double>{

        @Override
        protected Double convert(String value) {
            return Double.valueOf(value);
        }

    }

    private static class BooleanExtractor extends AbstractPropertyValueExtractor<Boolean>{

        @Override
        protected Boolean convert(String value) {
            return Boolean.valueOf(value);
        }

    }

}
