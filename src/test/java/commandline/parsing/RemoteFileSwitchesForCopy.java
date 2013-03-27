package commandline.parsing;

import commandline.parsing.annotations.SwitchOperation;
import commandline.parsing.annotations.ParamSwitch;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:15 AM
 */
@SwitchOperation(name = RemoteFileOperationNames.copy)
public interface RemoteFileSwitchesForCopy extends RemoteFileSwitchesShared {
    @ParamSwitch(shortName = "s", longName = "source", description = "full file path of the file to copy", required = true)
    String getFileToCopy();

    @ParamSwitch(shortName = "d", longName = "destination", description = "full path of the new file", required = true)
    String getFileCopyTo();
}
