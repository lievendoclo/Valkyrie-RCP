package org.valkyriercp.rules.closure.support;

/**
 * Simple implementation of AbstractClosure: makes it easy to just execute a
 * block of code without any additional parameters.
 */
public abstract class NoArgBlock extends AbstractClosure {

	/**
	 * {@inheritDoc}
	 *
	 * @return allways <code>null</code>, only code is executed.
	 */
	public final Object call(Object argument) {
		handle();
		return null;
	}

	/**
	 * Block of code to be executed. Implement this.
	 */
	protected void handle() {
		throw new UnsupportedOperationException("You must override call() for a noArg block that returns a value, or "
				+ "override handle() for a noArg block that returns no value");
	}

}
