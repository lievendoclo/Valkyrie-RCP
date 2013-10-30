package org.valkyriercp.rules;

import org.valkyriercp.rules.constraint.property.PropertyConstraint;

/**
 * Interface indicating the providing of validation. This can be
 * used in the validator to identify the source for validation rules.
 *
 * @author Keith Donald
 */
public interface PropertyConstraintProvider {

	/**
	 * Returns the constraint on the given property.
	 */
	public PropertyConstraint getPropertyConstraint(String propertyName);
}