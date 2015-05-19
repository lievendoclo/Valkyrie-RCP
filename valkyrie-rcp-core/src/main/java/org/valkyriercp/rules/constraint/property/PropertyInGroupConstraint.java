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

import org.springframework.util.Assert;
import org.valkyriercp.binding.PropertyAccessStrategy;

import java.util.Arrays;
import java.util.Collection;

/**
 * Property constraint which works like {@link org.valkyriercp.rules.constraint.InGroup} constraint but allows
 * using a dynamic value list to determine if a property value is in a group of
 * values.
 * <p>
 * To use it a second property field is required which contains the values for
 * the 'in group' test
 *
 * @author Mathias Broekelmann
 *
 */
public class PropertyInGroupConstraint extends AbstractPropertyConstraint {

	public final String groupPropertyName;

	/**
	 * @param propertyName the property which contains the value to test against
	 * the group values
	 * @param groupPropertyName the property which contains the group values
	 */
	public PropertyInGroupConstraint(String propertyName, String groupPropertyName) {
		super(propertyName);
		Assert.notNull(groupPropertyName, "The groupPropertyName to constrain is required");
		this.groupPropertyName = groupPropertyName;
	}

	public boolean isDependentOn(String propertyName) {
		return super.isDependentOn(propertyName) || getGroupPropertyName().equals(propertyName);
	}

	public String getGroupPropertyName() {
		return groupPropertyName;
	}

	protected boolean test(PropertyAccessStrategy accessor) {
		Object propertyValue = accessor.getPropertyValue(getPropertyName());
		Collection values = getValues(accessor.getPropertyValue(getGroupPropertyName()));
		return values != null && values.contains(propertyValue);
	}

	private Collection getValues(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Collection) {
			return (Collection) value;
		}
		if (value instanceof Object[]) {
			return Arrays.asList((Object[]) value);
		}
		throw new IllegalArgumentException("property " + getGroupPropertyName()
				+ " must contain a collection or an object array");
	}

}

