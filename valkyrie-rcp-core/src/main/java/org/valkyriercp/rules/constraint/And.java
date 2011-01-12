package org.valkyriercp.rules.constraint;

import java.util.Iterator;

/**
 * A "and" compound constraint (aka conjunction).
 *
 * @author Keith Donald
 */
public class And extends CompoundConstraint {

	/**
	 * Creates a empty And conjunction.
	 */
	public And() {
		super();
	}

	/**
	 * "Ands" two constraints.
	 *
	 * @param constraint1
	 *            The first constraint.
	 * @param constraint2
	 *            The second constraint.
	 */
	public And(Constraint constraint1, Constraint constraint2) {
		super(constraint1, constraint2);
	}

	/**
	 * "Ands" the specified constraints.
	 *
	 * @param constraints
	 *            The constraints
	 */
	public And(Constraint[] constraints) {
		super(constraints);
	}

	/**
	 * Tests if all of the constraints aggregated by this compound constraint
	 * return <code>true</code> when evaulating this argument.
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object argument) {
		for (Iterator i = iterator(); i.hasNext();) {
			if (!((Constraint)i.next()).test(argument)) {
				return false;
			}
		}
		return true;
	}
}
