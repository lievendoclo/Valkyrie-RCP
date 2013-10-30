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


