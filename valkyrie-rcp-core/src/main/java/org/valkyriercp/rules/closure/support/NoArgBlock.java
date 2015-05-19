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
