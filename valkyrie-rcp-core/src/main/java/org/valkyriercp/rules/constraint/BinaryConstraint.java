package org.valkyriercp.rules.constraint;

/**
 * A function object that tests two arguments and returns a single
 * <code>boolean</code> result.
 * <p><p>
 * A binary constraint is a function object that tests two arguments against
 * some conditional expression. For example, a "GreaterThan" constraint will
 * return true if the first argument is greater than the second.
 *
 * @author Keith Donald
 */
public interface BinaryConstraint extends Constraint {

    /**
     * Test the provided arguments against this predicates conditional
     * expression.
     *
     * @param argument1
     *            the first argument
     * @param argument2
     *            the second argument
     * @return true if the condition was satisfied, false otherwise
     */
    public boolean test(Object argument1, Object argument2);
}