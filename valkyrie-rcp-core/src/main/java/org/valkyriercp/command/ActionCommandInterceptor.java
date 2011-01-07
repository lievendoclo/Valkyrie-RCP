package org.valkyriercp.command;

import org.valkyriercp.command.support.ActionCommand;

/**
 * Implementations of this interface can be assigned to an action command to
 * intercept command execution.
 *
 * @author Keith Donald, Mathias Broekelmann
 */
public interface ActionCommandInterceptor {

	/**
	 * Will be called before the action command is executed.
	 *
	 * @param command
	 *            the action command which gets executed
	 * @return If false the action command and any subsequent action command
	 *         interceptor will not get executed. If true the action command
	 *         will be executed.
	 */
	public boolean preExecution(ActionCommand command);

	/**
	 * Will be called after successfull execution of an action command.
	 *
	 * @param command
	 *            the action command which was executed.
	 */
	public void postExecution(ActionCommand command);
}