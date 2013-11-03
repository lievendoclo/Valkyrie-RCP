package org.valkyriercp.rules.closure.support;

import org.springframework.util.Assert;
import org.valkyriercp.rules.closure.BinaryClosure;

/**
 * Conveneince support class for binary closures. Overrides call(argument) to
 * treat the passed argument as an array with two elements.
 *
 * @author Keith Donald
 */
public abstract class AbstractBinaryClosure extends AbstractClosure implements
        BinaryClosure {
    public Object call(Object argument) {
        if (argument == null) {
            argument = new Object[0];
        }
        Assert.isTrue(argument.getClass().isArray(),
                "Binary argument must be an array");
        Object[] arguments = (Object[])argument;
        Assert.isTrue(arguments.length == 2,
                "Binary argument must contain 2 elements");
        return call(arguments[0], arguments[1]);
    }

}
