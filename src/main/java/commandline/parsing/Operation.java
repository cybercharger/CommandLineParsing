package commandline.parsing;

import commandline.parsing.datastructure.SwitchSet;
import commandline.parsing.parser.SwitchSetParser;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Chris
 * Date: 3/26/13
 * Time: 11:51 PM
 */
public abstract class Operation<T extends SwitchSet, TResult> {

    private final String name;
    private SwitchSetParser<T> parser;

    protected Operation(String name, SwitchSetParser<T> parser) {
        if (StringUtils.isBlank(name)) throw new IllegalArgumentException("name is null or empty");
        if (parser == null) throw new IllegalArgumentException("parser is null");
        this.name = name;
        this.parser = parser;
    }

    public String getName() {
        return name;
    }

    public SwitchSetParser<T> getSwitchParser() {
        return parser;
    }

    public abstract TResult onOperation(T switchSet) throws Throwable;
}
