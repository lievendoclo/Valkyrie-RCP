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

import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.rules.constraint.AbstractConstraint;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UniquePropertyValueConstraint extends AbstractConstraint implements PropertyConstraint {
	private String propertyName;

	private Map distinctValueTable;

	public UniquePropertyValueConstraint(String propertyName) {
		this.propertyName = propertyName;
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


	/**
	 * Returns <code>true</code> if each domain object in the provided collection has a unique
	 * value for the configured property.
	 */
	public boolean test(Object o) {
		Collection domainObjects = (Collection)o;
		distinctValueTable = new HashMap((int)(domainObjects.size() * .75));
		Iterator it = domainObjects.iterator();
		MutablePropertyAccessStrategy accessor = null;
		while (it.hasNext()) {
			Object domainObject = it.next();
			if (accessor == null) {
				accessor = createPropertyAccessStrategy(domainObject);
			}
			else {
				accessor.getDomainObjectHolder().setValue(domainObject);
			}
			Object propertyValue = accessor.getPropertyValue(propertyName);
			Integer hashCode;
			if (propertyValue == null) {
				hashCode = new Integer(0);
			}
			else {
				hashCode = new Integer(propertyValue.hashCode());
			}
			if (distinctValueTable.containsKey(hashCode)) {
				return false;
			}

            distinctValueTable.put(hashCode, propertyValue);
		}
		return true;
	}

	protected MutablePropertyAccessStrategy createPropertyAccessStrategy(Object o) {
		return new BeanPropertyAccessStrategy(o);
	}

}