package org.valkyriercp.rules.constraint;

import java.util.Iterator;

/**
 * A "or" compound constraint (aka disjunction).
 *
 * @author Keith Donald
 */
public class Or extends CompoundConstraint {

	/**
	 * Creates a empty UnaryOr disjunction.
	 */
	public Or() {
		super();
	}

	/**
	 * "Ors" two constraints.
	 *
	 * @param constraint1
	 *            The first constraint.
	 * @param constraint2
	 *            The second constraint.
	 */
	public Or(Constraint constraint1, Constraint constraint2) {
		super(constraint1, constraint2);
	}

	/**
	 * "Ors" the specified constraints.
	 *
	 * @param constraints
	 *            The constraints
	 */
	public Or(Constraint[] constraints) {
		super(constraints);
	}

	/**
	 * Tests if any of the constraints aggregated by this compound constraint
	 * test <code>true</code>.
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object value) {
		for (Iterator i = iterator(); i.hasNext();) {
			if (((Constraint)i.next()).test(value)) {
				return true;
			}
		}
		return false;
	}
}
