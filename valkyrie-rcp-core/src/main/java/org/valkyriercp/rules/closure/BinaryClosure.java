package org.valkyriercp.rules.closure;

/**
 * A function object that tests two arguments and returns a single
 * <code>boolean</code> result.
 * <p><p>
 * A binary closure is a function object that acts on two arguments. For
 * example, the "Maximum" binary closure returns the maximum of two numbers.
 *
 * @author Keith Donald
 */
public interface BinaryClosure extends Closure {

    /**
     * Executes this closure with the provided arguments.
     *
     * @param argument1
     *            the first argument
     * @param argument2
     *            the second argument
     * @return The result of executing the closure
     */
    public Object call(Object argument1, Object argument2);
}
