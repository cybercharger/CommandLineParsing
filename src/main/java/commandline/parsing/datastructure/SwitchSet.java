package commandline.parsing.datastructure;

import java.util.Properties;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:52 PM
 */
public interface SwitchSet {
    String[] getArguments();

    Properties getProperties(String name);
}
