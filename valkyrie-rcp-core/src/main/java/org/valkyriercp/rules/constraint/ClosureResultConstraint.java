package org.valkyriercp.rules.constraint;

import org.springframework.util.Assert;
import org.valkyriercp.rules.closure.Closure;

/**
 * Tests the result returned from evaluating a closure closure.
 *
 * @author Keith Donald
 */
public class ClosureResultConstraint implements Constraint {
	private Constraint constraint;

	private Closure closure;

	/**
	 * Creates a ClosureResultConstraint that tests the result returned from
	 * evaulating the specified unary closure.
	 *
	 * @param closure
	 *            The closure to test.
	 * @param constraint
	 *            The predicate that will test the closure return value.
	 */
	public ClosureResultConstraint(Closure closure, Constraint constraint) {
		Assert.notNull(closure, "closure is required");
		Assert.notNull(constraint, "constraint is required");
		this.constraint = constraint;
		this.closure = closure;
	}

	/**
	 * Tests the result returned by evaluating the specified argument against
	 * the configured closure.
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object argument) {
		Object returnValue = closure.call(argument);
		return this.constraint.test(returnValue);
	}

	public Closure getFunction() {
		return closure;
	}

	public Constraint getPredicate() {
		return constraint;
	}

	public String toString() {
		return "[" + closure.toString() + " " + constraint.toString() + "]";
	}
}
