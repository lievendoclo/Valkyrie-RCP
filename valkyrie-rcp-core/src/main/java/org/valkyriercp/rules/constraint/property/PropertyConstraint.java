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
package org.valkyriercp.rules.constraint.property;

import org.valkyriercp.rules.constraint.Constraint;

/**
 * A predicate that constrains a bean property in some way.
 *
 * @author Keith Donald
 */
public interface PropertyConstraint extends Constraint {

	/**
	 * Returns the constrained property name.
	 *
	 * @return The property name
	 */
	public String getPropertyName();

	/**
	 * Returns <code>true</code> if this property constraint is dependent on
	 * the provided propertyName for test evaluation; that is, it should be retested
	 * when propertyName changes.
	 * @param propertyName
	 * @return true or false
	 */
	public boolean isDependentOn(String propertyName);

	/**
	 * Does this property constraint effect more than one property?
	 * @return true if yes, false otherwise
	 */
	public boolean isCompoundRule();
}
