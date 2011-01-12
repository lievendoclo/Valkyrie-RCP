package org.valkyriercp.rules.constraint.property;

import org.valkyriercp.rules.constraint.Not;

/**
 * @author Keith Donald
 */
public class NegatedPropertyConstraint extends Not implements PropertyConstraint {
	public NegatedPropertyConstraint(PropertyConstraint e) {
		super(e);
	}

	public String getPropertyName() {
		return ((PropertyConstraint)super.getConstraint()).getPropertyName();
	}

	public boolean isDependentOn(String propertyName) {
		return ((PropertyConstraint)super.getConstraint()).isDependentOn(propertyName);
	}

	public boolean isCompoundRule() {
		return ((PropertyConstraint)super.getConstraint()).isCompoundRule();
	}


}
