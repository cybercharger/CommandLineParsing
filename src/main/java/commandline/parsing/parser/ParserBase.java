package commandline.parsing.parser;

import commandline.parsing.datastructure.Converter;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 3:25 PM
 */
public abstract class ParserBase {
    protected class TypeConverter implements Converter {
        @Override
        public <T> T convert(String value, Class<T> clazz) {
            if (clazz.isPrimitive()) return convertToPrimitive(value, clazz);
            return convertReference(value, clazz);
        }

        private <T> T convertReference(String value, Class<T> clazz) {
            if (clazz.isAssignableFrom(String.class)) return clazz.cast(value);

            if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class))
                return clazz.cast(Short.parseShort(value));

            if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class))
                return clazz.cast(Integer.parseInt(value));

            if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(long.class))
                return clazz.cast(Long.parseLong(value));

            if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class))
                return clazz.cast(Boolean.parseBoolean(value));

            throw new RuntimeException("Unsupported type " + clazz.getCanonicalName());
        }

        private <T> T convertToPrimitive(String value, Class<T> clazz) {
            if (clazz.isAssignableFrom(String.class)) return (T)value;

            if (clazz.isAssignableFrom(byte.class)) return (T)((Object)Byte.parseByte(value));

            if (clazz.isAssignableFrom(short.class)) return (T)((Object)Short.parseShort(value));

            if (clazz.isAssignableFrom(int.class)) return (T)((Object)Integer.parseInt(value));

            if (clazz.isAssignableFrom(long.class)) return (T)((Object)Long.parseLong(value));

            if (clazz.isAssignableFrom(float.class)) return (T)((Object)Float.parseFloat(value));

            if (clazz.isAssignableFrom(double.class)) return (T)((Object)Double.parseDouble(value));

            if (clazz.isAssignableFrom(boolean.class)) return (T)((Object)Boolean.parseBoolean(value));

            throw new RuntimeException("Unsupported type " + clazz.getCanonicalName());
        }
    }

    private Converter converter;
    protected ParserBase() {
        this(null);
    }

    protected ParserBase(Converter converter) {
        this.converter = new TypeConverter();
    }

    public Converter getConverter() {
        return this.converter;
    }
}
