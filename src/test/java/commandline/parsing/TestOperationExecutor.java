package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.HelpClasses.RemoteFileOperationCopy;
import commandline.parsing.HelpClasses.RemoteFileOperationDelete;
import commandline.parsing.datastructure.SwitchSet;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

            HashMap<String, SwitchSet> map = new HashMap<String, SwitchSet>();
            map.put("copy -saaa -dbbb -t localhost -v", new RemoteFileOperationCopy.Params("localhost", "aaa", "bbb", true));
            map.put("delete -s xxx -t localhost", new RemoteFileOperationDelete.Params("localhost", "xxx", false));

            for (Map.Entry<String, SwitchSet> entry : map.entrySet()) {
                SwitchSet res = (SwitchSet)oe.execute(StringUtils.split(entry.getKey()));
                Assert.assertTrue(entry.getValue().equals(res));
            }

        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }
}
