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
