package commandline.parsing.parser;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.FlagSwitch;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.FlagSwitchInfo;
import commandline.parsing.datastructure.ParameterSwitchInfo;
import commandline.parsing.datastructure.SwitchInfo;
import commandline.parsing.datastructure.SwitchSet;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 1:39 AM
 */

//TODO: Add support for org.apache.com.cli.OptionGroup
public class SwitchSetParser<T extends SwitchSet> {
    public class ResultProxy implements InvocationHandler {
        private CommandLine cl;
        private SwitchSetParser parser;

        public ResultProxy(CommandLine cl, SwitchSetParser parser) {
            this.cl = cl;
            this.parser = parser;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SwitchInfo info = parser.getSwitchInfo(method.getName());
            if (info instanceof FlagSwitchInfo) {
                return cl.hasOption(info.getKey());

            } else {
                String defaultValue =info.getDefaultValue();
                return SwitchSetParser.parseValue(cl.getOptionValue(info.getKey(), defaultValue), info.getType());
            }
        }
    }

    private final Class<T> setClass;
    private HashMap<String, SwitchInfo> switchMap = new HashMap<String, SwitchInfo>();
    private HashMap<String, String> methodMap = new HashMap<String, String>();
    private Options cliOptions = new Options();
    private HelpFormatter helpFormatter = null;

    public SwitchSetParser(Class<T> setClass) throws ParsingException {
        if (setClass == null) throw new IllegalArgumentException("setClass is null");
        this.setClass = setClass;
        for (Method method : setClass.getMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            ParamSwitch param = getAnnotation(ParamSwitch.class, annotations);
            Class<?> type = method.getReturnType();
            validateReturnType(type);
            if (param != null) {
                String key = getKey(param);
                if (switchMap.containsKey(key)) throw new ParsingException(ParsingException.Error.DuplicateSwitches);
                SwitchInfo info = new ParameterSwitchInfo(param.shortName(), param.longName(), param.description(),
                        param.required(), param.defaultValue(), type);
                switchMap.put(key, info);
                methodMap.put(method.getName(), key);
                cliOptions.addOption(info.createOption());
                continue;
            }
            FlagSwitch flag = getAnnotation(FlagSwitch.class, annotations);
            if (flag != null) {
                String key = getKey(flag);
                if (switchMap.containsKey(key)) throw new ParsingException(ParsingException.Error.DuplicateSwitches);
                SwitchInfo info = new FlagSwitchInfo(flag.shortName(), flag.longName(), flag.description());
                switchMap.put(key, info);
                methodMap.put(method.getName(), key);
                cliOptions.addOption(info.createOption());
                continue;
            }
        }
        helpFormatter = new HelpFormatter();
    }

    //TODO: extend this method to support more according to cli
    public void printHelpInfo(String syntax) {
        helpFormatter.printHelp(syntax, cliOptions);
    }

    public T parse(String[] args) throws ParsingException {
        try {
            CommandLine cl = new GnuParser().parse(cliOptions, args);
            return setClass.cast(Proxy.newProxyInstance(
                    setClass.getClassLoader(),
                    new Class[]{setClass},
                    new ResultProxy(cl, this)));
        } catch (ParseException e) {
            throw new ParsingException("", ParsingException.Error.ParsingError, e);
        }
    }

    private SwitchInfo getSwitchInfo(String methodName) {
        return switchMap.get(methodMap.get(methodName));
    }

    static private void validateReturnType(Class<?> clazz) throws ParsingException {
        if (clazz.equals(void.class)) throw new ParsingException(ParsingException.Error.InvalidSwitchType);
    }

    static <T extends Annotation> T getAnnotation(Class<T> expected, Annotation[] annotations) {
        if (expected == null || annotations == null || annotations.length <= 0) return null;
        for (Annotation a : annotations) {
            if (expected.isInstance(a)) return expected.cast(a);
        }
        return null;
    }

    static private String getKey(ParamSwitch s) {
        if (StringUtils.isNotBlank(s.longName())) return s.longName();
        return s.shortName();
    }

    static String getKey(FlagSwitch f) {
        if (StringUtils.isNotBlank(f.longName())) return f.longName();
        return f.shortName();
    }

    static <T> T parseValue(String value, Class<T> clazz) {
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
}
