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