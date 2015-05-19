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
