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
import org.valkyriercp.rules.closure.support.AlgorithmsAccessor;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.Constraints;

/**
 * Helper class for creating and composing constraints for a single domain object property.
 *
 * @author Keith Donald
 */
public class PropertyConstraints extends AlgorithmsAccessor {

	private Constraints c = Constraints.instance();

	private String propertyName;

	public PropertyConstraints(String propertyName) {
		setPropertyName(propertyName);
	}

	public void setPropertyName(String propertyName) {
		Assert.notNull(propertyName, "The propertyName to create constraints for is required");
		this.propertyName = propertyName;
	}

	public PropertyConstraint all(Constraint[] valueConstraints) {
		return c.all(propertyName, valueConstraints);
	}

	public PropertyConstraint any(Constraint[] valueConstraints) {
		return c.any(propertyName, valueConstraints);
	}

	public PropertyConstraint eq(Object value) {
		return c.eq(propertyName, value);
	}

	public PropertyConstraint lt(Comparable value) {
		return c.lt(propertyName, value);
	}

	public PropertyConstraint lte(Comparable value) {
		return c.lte(propertyName, value);
	}

	public PropertyConstraint gt(Comparable value) {
		return c.gte(propertyName, value);
	}

	public PropertyConstraint gte(Comparable value) {
		return c.gte(propertyName, value);
	}

	public PropertyConstraint eqProperty(String otherPropertyName) {
		return c.eqProperty(propertyName, otherPropertyName);
	}

	public PropertyConstraint ltProperty(String otherPropertyName) {
		return c.ltProperty(propertyName, otherPropertyName);
	}

	public PropertyConstraint lteProperty(String otherPropertyName) {
		return c.lteProperty(propertyName, otherPropertyName);
	}

	public PropertyConstraint gtProperty(String otherPropertyName) {
		return c.gtProperty(propertyName, otherPropertyName);
	}

	public PropertyConstraint gteProperty(String otherPropertyName) {
		return c.gteProperty(propertyName, otherPropertyName);
	}

	public PropertyConstraint inRange(Comparable min, Comparable max) {
		return c.inRange(propertyName, min, max);
	}

	public PropertyConstraint inRangeProperties(String minProperty, String maxProperty) {
		return c.inRangeProperties(propertyName, minProperty, maxProperty);
	}
}


