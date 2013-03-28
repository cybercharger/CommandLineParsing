package commandline.parsing.parser;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.datastructure.Converter;
import commandline.parsing.datastructure.Operation;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * User: chriskang
 * Date: 3/28/13
 * Time: 3:57 PM
 */
public class OperationExecutor extends ParserBase {
    private Operation[] operations;

    public OperationExecutor(Operation[] operations) {
        this(operations, null);
    }

    public OperationExecutor(Operation[] operations, Converter converter) {
        super(converter);
        if (operations == null || operations.length <= 1) throw new IllegalArgumentException("operations is null or empty or only one operation specified");
        this.operations = operations;
    }

    public void execute(String[] args) throws ParsingException {
        Operation opToRun = parseOperation(args);
        opToRun.onOperation(opToRun.getSwitchParser().parse(Arrays.copyOfRange(args, 1, args.length)));
    }

    private Operation parseOperation(String args[]) throws ParsingException {
        if (args == null || args.length < 1) {
            throw new ParsingException("no operation specified", ParsingException.Error.ParsingError);
        }
        for (Operation operation : operations) {
            if (StringUtils.isBlank(operation.getName())) throw new ParsingException(ParsingException.Error.InvalidOptionName);
            if (operation.getName().equals(args[0])) {
                return operation;
            }
        }
        throw new ParsingException(ParsingException.Error.InvalidOptionName);
    }
}
