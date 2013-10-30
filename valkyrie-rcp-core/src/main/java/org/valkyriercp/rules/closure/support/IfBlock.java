package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.constraint.Constraint;

/**
 * Only execute the specified closure if a provided constraint is also true.
 *
 * @author Keith Donald
 */
public class IfBlock extends Block {
	private static final long serialVersionUID = 1L;

	/**
	 * Block of code to execute if object passes test.
	 */
	private Closure closure;

	/**
	 * Constraint to test against.
	 */
	private Constraint constraint;

	/**
	 * Constructor.
	 *
	 * @param constraint Constraint to test against.
	 * @param closure closure to be executed if object passes the test.
	 */
	public IfBlock(Constraint constraint, Closure closure) {
		this.constraint = constraint;
		this.closure = closure;
	}

	/**
	 * Only invoke the wrapped closure against the provided argument if the
	 * constraint permits, else take no action.
	 */
	protected void handle(Object argument) {
		if (constraint.test(argument)) {
			closure.call(argument);
		}
	}

}