package commandline.parsing.HelpClasses;

import commandline.parsing.annotations.SwitchOperation;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.SwitchSet;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:22 AM
 */
@SwitchOperation(name = RemoteFileOperationNames.delete)
public interface RemoteFileSwitchesForDelete extends RemoteFileSwitchesShared, SwitchSet {
    @ParamSwitch(shortName = "s", longName = "source", description = "full path of the file to delete", required = true)
    String getFileToDelete();
}
