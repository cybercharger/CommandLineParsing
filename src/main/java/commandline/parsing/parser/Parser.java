package commandline.parsing.parser;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.annotations.SwitchOperation;
import commandline.parsing.datastructure.Operation;
import commandline.parsing.datastructure.SwitchInfo;
import commandline.parsing.datastructure.SwitchSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 12:39 PM
 */
public class Parser {

    private HashMap<String, Operation> opMap = new HashMap<String, Operation>();
    private HashMap<String, HashMap<String, SwitchInfo>> switchMap = new HashMap<String, HashMap<String, SwitchInfo>>();

    public Parser(Operation[] operations, SwitchSet[] switchSets) throws ParsingException {
        if (switchSets == null || switchSets.length == 0) throw new IllegalArgumentException("switchSets is null or empty");
        prepareOperations(operations);
        for (SwitchSet set : switchSets) {
            prepareSwitchSet(set);
        }
    }

    private void validateOperation(Operation op) throws ParsingException {
        if (op == null) throw new IllegalArgumentException("null operations found");
        if (null == op.getName()) throw new ParsingException(ParsingException.Error.InvalidOptionName);
        if (opMap.containsKey(op.getName())) throw new ParsingException(ParsingException.Error.DuplicateOperations);
    }

    private void prepareOperations(Operation[] operations) throws ParsingException {
        // no operation specified, then operation should be the command itself
        if (operations == null || operations.length == 0) throw new IllegalArgumentException("operations is null or empty");
        for (Operation op : operations) {
            validateOperation(op);
            opMap.put(op.getName(), op);
            switchMap.put(op.getName(), new HashMap<String, SwitchInfo>());
        }
    }

    private void prepareSwitchSet(SwitchSet set) throws ParsingException {
        if (set == null) throw new IllegalArgumentException("null switch set found");
        HashMap<String, SwitchInfo> target = null;
        SwitchOperation annOpt = getAnnotation(SwitchOperation.class, set.getClass().getDeclaredAnnotations());
        if (annOpt == null) throw new ParsingException(ParsingException.Error.OperationNameNotSpecified);
    }


    static public void parse(Object target) throws IllegalAccessException, InvocationTargetException {
    }

    static <T extends Annotation> T getAnnotation(Class<T> expected, Annotation[] annotations) {
        if (expected == null || annotations == null || annotations.length <= 0) return null;
        for (Annotation a : annotations) {
            if (expected.isInstance(a)) return expected.cast(a);
        }
        return null;

    }
}
