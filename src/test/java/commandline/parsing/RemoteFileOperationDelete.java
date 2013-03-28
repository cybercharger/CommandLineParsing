package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.datastructure.Operation;
import commandline.parsing.parser.SwitchSetParser;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:33 AM
 */
public class RemoteFileOperationDelete extends Operation<RemoteFileSwitchesForDelete> {
    protected RemoteFileOperationDelete() throws ParsingException {
        super("delete", new SwitchSetParser<RemoteFileSwitchesForDelete>(RemoteFileSwitchesForDelete.class));
    }

    @Override
    public void onOperation(RemoteFileSwitchesForDelete set) {
        System.out.println(String.format("delete file <%2$s> on <%1$s>", set.getRemoteServer(), set.getFileToDelete()));
    }
}
