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

