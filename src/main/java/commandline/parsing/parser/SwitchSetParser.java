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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
            if (method.getName().equals("getArguments")) {
                return cl.getArgs();
            }

            if (method.getName().equals("getProperties")) {
                return cl.getOptionProperties((String) (args[0]));
            }

            SwitchInfo info = parser.getSwitchInfo(method.getName());
            if (info instanceof FlagSwitchInfo) {
                return cl.hasOption(info.getKey());

            } else {
                String defaultValue = info.getDefaultValue();
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
        Set<String> shorts = new HashSet<String>();
        Set<String> longs = new HashSet<String>();
        for (Method method : setClass.getMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            Annotation a = getSwitchAnnotation(annotations);
            if (null == a) continue;
            Class<?> type = method.getReturnType();
            validateReturnType(type);

            String shortName;
            String longName;
            String key;
            SwitchInfo info;

            if (a instanceof ParamSwitch) {
                ParamSwitch param = ParamSwitch.class.cast(a);
                shortName = param.shortName();
                longName = param.longName();
                key = getKey(param);
                info = new ParameterSwitchInfo(param.shortName(), param.longName(), param.description(),
                        param.required(), param.defaultValue(), type);
            } else if (a instanceof FlagSwitch) {
                FlagSwitch flag = FlagSwitch.class.cast(a);
                shortName = flag.shortName();
                longName = flag.longName();
                key = getKey(flag);
                info = new FlagSwitchInfo(flag.shortName(), flag.longName(), flag.description());
            } else {
                throw new RuntimeException(String.format("ParamSwitch/FlagSwitch is get from %s, but none of them can be converted to", method.getName()));
            }
            if (shortName != null && shorts.contains(shortName)) {
                throw new IllegalArgumentException("duplicate shortName found: " + shortName);
            } else {
                shorts.add(shortName);
            }
            if (longName != null && longs.contains(longName)) {
                throw new IllegalArgumentException("duplicate longName found: " + longName);
            } else {
                longs.add(longName);
            }
            if (StringUtils.isBlank(key)) throw new IllegalArgumentException("neither short name nor long name is specified");
            switchMap.put(key, info);
            methodMap.put(method.getName(), key);
            cliOptions.addOption(info.createOption());
        }
        helpFormatter = new HelpFormatter();
    }

    public void printHelpInfo(String syntax) {
        String required = " ";
        String optional = "";
        for (Map.Entry<String, SwitchInfo> entry : switchMap.entrySet()) {
            SwitchInfo info = entry.getValue();
            String s = StringUtils.isBlank(info.getShortName()) ? "--" + info.getLongName() : "-" + info.getShortName();
            if (info.isRequired()) {
                required += s + " ";
            } else {
                optional += "[" + s + "] ";
            }
        }
        helpFormatter.printHelp(syntax + required + optional, cliOptions);
    }

    public T parse(String[] args) throws ParsingException {
        try {
            CommandLine cl = new GnuParser().parse(cliOptions, args);
            return setClass.cast(Proxy.newProxyInstance(
                    setClass.getClassLoader(),
                    new Class[]{setClass},
                    new ResultProxy(cl, this)));
        } catch (ParseException e) {
            throw new ParsingException(e.getMessage(), ParsingException.Error.ParsingError, e);
        }
    }

    private SwitchInfo getSwitchInfo(String methodName) {
        return switchMap.get(methodMap.get(methodName));
    }

    static private void validateReturnType(Class<?> clazz) throws ParsingException {
        if (clazz.equals(void.class)) throw new ParsingException(ParsingException.Error.InvalidSwitchType);
    }

    static Annotation getSwitchAnnotation(Annotation[] annotations) {
        if (annotations == null || annotations.length <= 0) return null;
        for (Annotation a : annotations) {
            if (a instanceof ParamSwitch || a instanceof FlagSwitch) return a;
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
