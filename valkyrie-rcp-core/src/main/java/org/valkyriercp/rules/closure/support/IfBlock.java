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