package commandline.parsing.annotations;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:47 AM
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A flag switch should be
 * 1) optional (not required);
 * 2) default false, absence means false otherwise true;
 * 3) always boolean;
 * A typical example of flag switch is like -v, --verbose switch in bunch of the command line tools to show verbose log.
 * Please use ParameterSwitch with type parameter Boolean for a non-flag boolean switch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlagSwitch {
    String shortName ();
    String longName ();
    String description ();
}
