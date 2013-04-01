package commandline.parsing;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.datastructure.Converter;
import commandline.parsing.Operation;
import commandline.parsing.parser.ParserBase;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        validateOperations(operations);
        this.operations = operations;
    }

    public void printHelpInfo() {
        for (Operation op : operations) {
            op.getSwitchParser().printHelpInfo(op.getName());
        }
    }

    public Object execute(String[] args) throws Throwable {
        Operation opToRun = parseOperation(args);
        return opToRun.onOperation(opToRun.getSwitchParser().parse(Arrays.copyOfRange(args, 1, args.length)));
    }

    private void validateOperations(Operation[] operations) {
        if (operations == null || operations.length <= 1) throw new IllegalArgumentException("operations is null or empty or only one operation specified");
        Set<String> operationSet = new HashSet<String>(operations.length);
        for (Operation op : operations) {
            if (StringUtils.isBlank(op.getName())) throw new IllegalArgumentException("operation name is null or empty");
            if (operationSet.contains(op.getName())) throw new IllegalArgumentException("Duplicate operation name " + op.getName());
        }
    }

    private Operation parseOperation(String args[]) throws ParsingException {
        if (args == null || args.length < 1) {
            throw new ParsingException("no operation specified", ParsingException.Error.ParsingError);
        }
        for (Operation operation : operations) {
            if (operation.getName().equals(args[0])) {
                return operation;
            }
        }
        throw new ParsingException("no valid operation name is specified", ParsingException.Error.InvalidOptionName);
    }
}
