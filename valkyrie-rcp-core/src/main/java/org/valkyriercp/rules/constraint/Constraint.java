package org.valkyriercp.rules.constraint;

/**
 * A function object that tests one argument and returns a single
 * <code>boolean</code> result.
 * <p>
 * A constraint tests a single argument against some conditional expression. For
 * example, a "required" constraint will return true if the provided argument is
 * non-null or empty, false otherwise.
 * </p>
 *
 * @author Keith Donald
 */
public interface Constraint {

	/**
	 * Test the provided argument against this predicate's condition.
	 *
	 * @param argument the argument value
	 * @return <code>true</code> if the condition was satisfied,
	 * <code>false</code> otherwise
	 */
	boolean test(Object argument);
}
