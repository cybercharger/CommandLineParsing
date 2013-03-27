package commandline.parsing.parser;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.FlagSwitch;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.FlagSwitchInfo;
import commandline.parsing.datastructure.ParameterSwitchInfo;
import commandline.parsing.datastructure.SwitchInfo;
import commandline.parsing.datastructure.SwitchSet;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 1:39 AM
 */

public class SwitchSetParser<T extends SwitchSet> {
    private HashMap<String, SwitchInfo> map = new HashMap<String, SwitchInfo>();

    public SwitchSetParser(T set) throws ParsingException {
        if (set == null) throw new IllegalArgumentException("set is null");
        for (Method method : set.getClass().getDeclaredMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            ParamSwitch param = getAnnotation(ParamSwitch.class, annotations);
            Class<?> type = method.getReturnType();
            if (param != null) {
                String key = getKey(param);
                if (map.containsKey(key)) throw new ParsingException(ParsingException.Error.DuplicateSwitches);
                map.put(key, new ParameterSwitchInfo(param.shortName(), param.longName(), param.description(), param.required(),
                        parseValue(param.defaultValue(), type), type));
                break;
            }
            FlagSwitch flag = getAnnotation(FlagSwitch.class, annotations);
            if (flag != null) {
                String key = getKey(flag);
                if (map.containsKey(key)) throw new ParsingException(ParsingException.Error.DuplicateSwitches);
                map.put(key, new FlagSwitchInfo(flag.shortName(), flag.longName(), flag.description()));
                break;
            }
        }

    }

    public T parse(String[] args) {
        return null;
    }

    static <T extends Annotation> T getAnnotation(Class<T> expected, Annotation[] annotations) {
        if (expected == null || annotations == null || annotations.length <= 0) return null;
        for (Annotation a : annotations) {
            if (expected.isInstance(a)) return expected.cast(a);
        }
        return null;
    }

    static String getKey(ParamSwitch s) {
        if (StringUtils.isNotBlank(s.longName())) return s.longName();
        return s.shortName();
    }

    static String getKey(FlagSwitch f) {
        if (StringUtils.isNotBlank(f.longName())) return f.longName();
        return f.shortName();
    }

    static <T> T parseValue(String value, Class<T> clazz) {
        if (clazz.isAssignableFrom(String.class)) return clazz.cast(value);
        if (clazz.isAssignableFrom(Short.class)) return clazz.cast(Short.parseShort(value));
        if (clazz.isAssignableFrom(Integer.class)) return clazz.cast(Integer.parseInt(value));
        if (clazz.isAssignableFrom(Long.class)) return clazz.cast(Long.parseLong(value));
        if (clazz.isAssignableFrom(Boolean.class)) return clazz.cast(Boolean.parseBoolean(value));
        throw new RuntimeException("Unsupported type " + clazz.getCanonicalName());
    }
}
