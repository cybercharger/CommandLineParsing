package commandline.parsing.parser;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.FlagSwitch;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.*;
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
public class SwitchSetParser<T extends SwitchSet> extends ParserBase {
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
                return getConverter().convert(cl.getOptionValue(info.getKey(), defaultValue), info.getType());
            }
        }
    }

    private final Class<T> setClass;
    private HashMap<String, SwitchInfo> switchMap = new HashMap<String, SwitchInfo>();
    private HashMap<String, String> methodMap = new HashMap<String, String>();
    private Options cliOptions = new Options();
    private HelpFormatter helpFormatter = null;

    public SwitchSetParser(Class<T> setClass) throws ParsingException {
        this(setClass, null);
    }

    public SwitchSetParser(Class<T> setClass, Converter convert) throws ParsingException {
        super(convert);

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
}
