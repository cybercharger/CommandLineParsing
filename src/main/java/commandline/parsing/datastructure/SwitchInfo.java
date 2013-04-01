package commandline.parsing.datastructure;

import commandline.parsing.Exceptions.ParsingException;
import org.apache.commons.cli.Option;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:54 AM
 */
public abstract class SwitchInfo {

    static public final String nullDefault = "";

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isFlag() {
        return flag;
    }

    public String getDefaultValue() {
        if (nullDefault.equals(defaultValue)) return null;
        return defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public String getKey() {
        if (StringUtils.isNotBlank(longName)) return longName;
        return shortName;
    }

    private String shortName;
    private String longName;
    private String description;
    private boolean required;
    private boolean flag;
    private String defaultValue;
    private Class<?> type;

    public SwitchInfo(String shortName, String longName, String description, boolean required, boolean flag, String defaultValue, Class<?> type) {
        if (StringUtils.isBlank(shortName) && StringUtils.isBlank(longName)) {
            throw new IllegalArgumentException("both short name and long name are null or empty");
        }
        String key = (!StringUtils.isBlank(shortName) ? shortName : longName);
        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("description is not specified for switch: " + key);
        }
        if (!required && StringUtils.isEmpty(defaultValue)) {
            throw new IllegalArgumentException("default value is not set for optional switch: " + key);
        }
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.required = required;
        this.flag = flag;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public Option createOption() {
        Option opt = new Option(shortName, longName, !flag, description);
        opt.setRequired(required);
        return opt;
    }
}
