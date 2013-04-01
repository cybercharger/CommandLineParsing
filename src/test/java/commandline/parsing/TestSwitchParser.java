package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.ParamSwitch;
import commandline.parsing.datastructure.SwitchSet;
import commandline.parsing.parser.SwitchSetParser;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Properties;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 2:18 PM
 */
public class TestSwitchParser {
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

    public class PrimitivesImpl implements Primitives {
        private boolean aBoolean;
        private short aShort;
        private int anInt;
        private long aLong;
        private double aDouble;
        private String[] args;

        public PrimitivesImpl(boolean aBoolean, short aShort, int anInt, long aLong, double aDouble, String[] args) {
            this.aBoolean = aBoolean;
            this.aShort = aShort;
            this.anInt = anInt;
            this.aLong = aLong;
            this.aDouble = aDouble;
            this.args = args;
        }

        @Override
        public boolean getBoolean() {
            return aBoolean;
        }

        @Override
        public short getShort() {
            return aShort;
        }

        @Override
        public int getInt() {
            return anInt;
        }

        @Override
        public long getLong() {
            return aLong;
        }

        @Override
        public double getDouble() {
            return aDouble;
        }

        @Override
        public String[] getArguments() {
            return args;
        }

        @Override
        public Properties getProperties(String name) {
            return null;
        }
    }

    @Test
    public void testPrimitive() {
        try {
            SwitchSetParser<Primitives> ssp = new SwitchSetParser<Primitives>(Primitives.class);
            String cmd = "-bfalse -i 100 -l -200 -s3 -d2.2 arg1 arg2";
            Primitives res = ssp.parse(StringUtils.split(cmd));
            verify(res, new PrimitivesImpl(false, (short)3, 100, -200L, 2.2, new String[] {"arg1", "arg2"}));
            cmd = "-bfalse -i 100 -l -200 -s3 -d2.2";
            res = ssp.parse(StringUtils.split(cmd));
            verify(res, new PrimitivesImpl(false, (short)3, 100, -200L, 2.2, new String[0]));
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }

    private void verify(Primitives actual, PrimitivesImpl expected) {
        Assert.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assert.assertEquals(expected.getShort(), actual.getShort());
        Assert.assertEquals(expected.getInt(), actual.getInt());
        Assert.assertEquals(expected.getLong(), actual.getLong());
        Assert.assertEquals(expected.getDouble(), actual.getDouble());
        String[] actArgs = actual.getArguments();
        String[] expArgs = expected.getArguments();
        if (actArgs == null && expArgs == null) return;
        Assert.assertEquals(expArgs.length, actArgs.length);
        for (int i = 0; i < actArgs.length; ++i) {
            Assert.assertEquals(expArgs[i], actArgs[i]);
        }
    }
}
