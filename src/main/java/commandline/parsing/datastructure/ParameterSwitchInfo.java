package commandline.parsing.datastructure;

import commandline.parsing.Exceptions.ParsingException;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:03 AM
 */
public class ParameterSwitchInfo<T> extends SwitchInfo<T> {
    public ParameterSwitchInfo(String shortName, String longName, String description, boolean required, T defaultValue, Class<T> type) throws ParsingException {
        super(shortName, longName, description, required, false, defaultValue, type);
    }
}
