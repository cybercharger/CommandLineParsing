package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.datastructure.Operation;
import commandline.parsing.parser.OperationExecutor;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 5:05 PM
 */
public class TestOperationExecutor {
    @Test
    public void testOperation() {
        try {
            OperationExecutor oe = new OperationExecutor(new Operation[]{
                    new RemoteFileOperationCopy(),
                    new RemoteFileOperationDelete(),
            });
            String[] cmds = new String[]{
                    "copy -saaa -dbbb -t localhost -v",
                    "delete -s xxx -t localhost"
            };

            for (String cmd : cmds) {
                System.out.println("Result of parsing: " + cmd);
                oe.execute(StringUtils.split(cmd));
            }

        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }
}
