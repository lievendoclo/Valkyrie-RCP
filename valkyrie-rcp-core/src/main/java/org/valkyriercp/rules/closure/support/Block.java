package org.valkyriercp.rules.closure.support;

/**
 * Convenient super class for closures that encapsulate a block of executable
 * code. Subclasses should override <code>void handle(Object)</code> for
 * executing a block of code without a result.
 *
 * @author Keith Donald
 */
public abstract class Block extends AbstractClosure {

	/**
	 * {@inheritDoc}
	 *
	 * @return allways <code>null</code>, only code is executed.
	 */
	public final Object call(Object argument) {
		handle(argument);
		return null;
	}

	/**
	 * Method to override in block subclasses that return no result: this method
	 * is intended encapsulate the block's processing.
	 *
	 * @param argument the argument to process
	 */
	protected abstract void handle(Object argument);

}
