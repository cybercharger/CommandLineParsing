package commandline.parsing.datastructure;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:51 PM
 */
public interface Operation {
    String getName();
    void onOperation(SwitchSet switchSet);
}
