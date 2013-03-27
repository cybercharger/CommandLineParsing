package commandline.parsing.datastructure;

import commandline.parsing.Exceptions.ParsingException;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:54 PM
 */

public class FlagSwitchInfo extends SwitchInfo {
    public FlagSwitchInfo(String shortName, String longName, String description) throws ParsingException {
        super(shortName, longName, description, false, true, "false", Boolean.class);
    }
}
