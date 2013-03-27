package commandline.parsing;

import commandline.parsing.datastructure.Operation;
import commandline.parsing.datastructure.SwitchSet;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:28 AM
 */
public class RemoteFileOperationCopy implements Operation {
    @Override
    public String getName() {
        return RemoteFileOperationNames.copy;
    }

    @Override
    public void onOperation(SwitchSet switchSet) {
        RemoteFileSwitchesForCopy set = (RemoteFileSwitchesForCopy)switchSet;
        System.out.println(String.format("copy file on %1$s, src: %2$s, dest: %3$s", set.getRemoteServer(), set.getFileToCopy(), set.getFileCopyTo()));
    }
}
