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
