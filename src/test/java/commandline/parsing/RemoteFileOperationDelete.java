package commandline.parsing;

import commandline.parsing.datastructure.Operation;
import commandline.parsing.datastructure.SwitchSet;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:33 AM
 */
public class RemoteFileOperationDelete implements Operation {
    @Override
    public String getName() {
        return RemoteFileOperationNames.delete;
    }

    @Override
    public void onOperation(SwitchSet switchSet) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
