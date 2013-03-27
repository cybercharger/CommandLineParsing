package commandline.parsing.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:47 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamSwitch {
    String shortName ();
    String longName ();
    boolean required ();
    String defaultValue () default "";
    String description ();
}
