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
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;

/**
 * Convenience superclass for bean property expressions.
 *
 * @author Keith Donald
 */
public abstract class AbstractPropertyConstraint implements PropertyConstraint {

	private String propertyName;

	protected AbstractPropertyConstraint() {
	}

	protected AbstractPropertyConstraint(String propertyName) {
		setPropertyName(propertyName);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isDependentOn(String propertyName) {
		return getPropertyName().equals(propertyName);
	}

	public boolean isCompoundRule() {
		return false;
	}

	protected void setPropertyName(String propertyName) {
		Assert.notNull(propertyName, "The propertyName to constrain is required");
		this.propertyName = propertyName;
	}

	public boolean test(Object o) {
		if (o instanceof PropertyAccessStrategy)
			return test((PropertyAccessStrategy)o);

        return test(new BeanPropertyAccessStrategy(o));
	}

	protected abstract boolean test(PropertyAccessStrategy domainObjectAccessStrategy);

	public String toString() {
		return getPropertyName();
	}

}
