package commandline.parsing.HelpClasses;

import commandline.parsing.annotations.FlagSwitch;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.SwitchSet;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:10 AM
 */
public interface RemoteFileSwitchesShared {
    @ParamSwitch(shortName = "t", longName = "target-server", required = true, description = "remote server to copy/delete file(s) on")
    String getRemoteServer();

    @FlagSwitch(shortName = "v", longName = "verbose", description = "show verbose log")
    Boolean getVerboseLog();
}
