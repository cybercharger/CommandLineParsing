package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.datastructure.Operation;
import commandline.parsing.parser.SwitchSetParser;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:28 AM
 */
public class RemoteFileOperationCopy extends Operation<RemoteFileSwitchesForCopy> {
    protected RemoteFileOperationCopy() throws ParsingException {
        super("copy", new SwitchSetParser<RemoteFileSwitchesForCopy>(RemoteFileSwitchesForCopy.class));
    }

    @Override
    public void onOperation(RemoteFileSwitchesForCopy set) {
        String info = "copy file on <%1$s>, src: <%2$s>, dest: <%3$s>" + (set.getVerboseLog() ? " using verbose log" : "");
        System.out.println(String.format(info, set.getRemoteServer(), set.getFileToCopy(), set.getFileCopyTo()));
    }
}
