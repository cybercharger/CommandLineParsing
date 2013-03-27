package commandline.parsing;


import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.parser.SwitchSetParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void testApp() throws ParsingException {
        SwitchSetParser<RemoteFileSwitchesForCopy> switchParser =
                new SwitchSetParser<RemoteFileSwitchesForCopy>(RemoteFileSwitchesForCopy.class);
        switchParser.printHelpInfo("copy file");

        String cmdArg = "-saaa -dbbb -t localhost -v";
        System.out.println("mock cmd ags: " + cmdArg);
        String[] args = StringUtils.split(cmdArg);
        RemoteFileSwitchesForCopy res = switchParser.parse(args);
        mockCopyFile(res);
    }

    private void mockCopyFile(RemoteFileSwitchesForCopy param) {
        System.out.println(String.format(
                "Copy file %1$s to %2$s on server %3$s using verbose log: %4$s",
                param.getFileToCopy(), param.getFileCopyTo(), param.getRemoteServer(), param.getVerboseLog().toString()
        ));
    }
}
