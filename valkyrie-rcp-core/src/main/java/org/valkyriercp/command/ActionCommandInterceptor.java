/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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