package commandline.parsing.datastructure;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 3:23 PM
 */
public interface Converter {
    <T> T convert(String value, Class<T> clazz);
}
