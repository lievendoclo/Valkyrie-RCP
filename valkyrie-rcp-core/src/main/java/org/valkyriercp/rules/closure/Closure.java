package org.valkyriercp.rules.closure;

/**
 * A function object that evaluates one argument, executing a block of code. It
 * returns a single result.
 * <p>
 * A closure evaluates a single argument against some expression. For example, a
 * "StringLength" closure might accept any object and return the length of the
 * object's string form.
 *
 * @author Keith Donald
 */
public interface Closure {

	/**
	 * Evaluate the function with the provided argument, returning the result.
	 *
	 * @param argument the argument
	 * @return the function return value
	 */
	Object call(Object argument);
}
