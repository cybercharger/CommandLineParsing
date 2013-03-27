package commandline.parsing.Exceptions;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:47 AM
 */
public class ParsingException extends Exception {
    public enum Error {
        DuplicateSwitches,
        InvalidSwitchName,
        DuplicateOperations,
        InvalidOptionName,
        OperationNameNotSepcified,
        NoDescription,
        ParsingError,
        Unspecified,
    }

    private Error error;

    public ParsingException(Error err) {
        super();
        this.error = err;
    }

    public ParsingException(String message, Error err) {
        super(message);
        this.error = err;
    }

    public ParsingException(String message, Error err, Throwable cause) {
        super(message, cause);
        this.error = err;
    }
}
