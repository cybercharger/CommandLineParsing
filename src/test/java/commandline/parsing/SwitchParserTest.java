package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.SwitchSet;
import commandline.parsing.parser.SwitchSetParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 2:18 PM
 */
public class SwitchParserTest {
    public interface Primitives extends SwitchSet {
        @ParamSwitch(shortName = "b", longName = "bool", required = true, description = "boolean")
        boolean getBoolean();

        @ParamSwitch(shortName = "s", longName = "sht", required = true, description = "short")
        short getShort();

        @ParamSwitch(shortName = "i", longName = "int", required = true, description = "int")
        int getInt();

        @ParamSwitch(shortName = "l", longName = "long", required = true, description = "long")
        long getLong();

        @ParamSwitch(shortName = "d", longName = "double", required = true, description = "double")
        double getDouble();
    }

    @Test
    public void testPrimitive() {
        try {
            SwitchSetParser<Primitives> ssp = new SwitchSetParser<Primitives>(Primitives.class);
            String cmd = "-bfalse -i 100 -l -200 -s3 -d2.2";
            Primitives res = ssp.parse(StringUtils.split(cmd));
            printPrimitives(res);
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }

    private void printPrimitives(Primitives p) {
        System.out.println(String.format("b: %s", p.getBoolean()));
        System.out.println(String.format("s: %d", p.getShort()));
        System.out.println(String.format("i: %d", p.getInt()));
        System.out.println(String.format("l: %d", p.getLong()));
        System.out.println(String.format("d: %f", p.getDouble()));
    }
}
