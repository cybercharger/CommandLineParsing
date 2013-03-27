package commandline.parsing.datastructure;

import commandline.parsing.Exceptions.ParsingException;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:54 AM
 */
public abstract class SwitchInfo<T> {
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

    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getType() {
        return type;
    }

    private String shortName;
    private String longName;
    private String description;
    private boolean required;
    private boolean flag;
    private T defaultValue;
    private Class<T> type;

    public SwitchInfo(String shortName, String longName, String description, boolean required, boolean flag, T defaultValue, Class<T> type)
            throws ParsingException {
        if (StringUtils.isBlank(shortName) && StringUtils.isBlank(longName)) {
            throw new ParsingException("both short name and long name are null or empty", ParsingException.Error.InvalidSwitchName);
        }
        if (StringUtils.isBlank(description)) throw new ParsingException(ParsingException.Error.NoDescription);
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.required = required;
        this.flag = flag;
        this.defaultValue = defaultValue;
        this.type = type;
    }
}
