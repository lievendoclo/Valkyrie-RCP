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