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

import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.rules.constraint.Required;

/**
 * Predicate that tests if the specified bean property is "present" - that is,
 * passes the "Required" test.
 *
 * @author Keith Donald
 * @see Required
 */
public class PropertyPresent extends AbstractPropertyConstraint {

	/**
	 * Constructs a property present predicate for the specified property.
	 *
	 * @param propertyName
	 *            The bean property name.
	 */
	public PropertyPresent(String propertyName) {
		super(propertyName);
	}

	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		return Required.instance().test(domainObjectAccessStrategy.getPropertyValue(getPropertyName()));
	}

	public String toString() {
		return "required if '" + getPropertyName() + "' present";
	}

}