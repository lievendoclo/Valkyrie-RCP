package org.valkyriercp.rules;

import org.valkyriercp.rules.constraint.property.PropertyConstraint;

/**
 * Rules sources provide a mechanism for accessing rules associated with a bean
 * class and specific bean properties. A rules source is effectively a "Rules"
 * data access object.
 *
 * @author Keith Donald
 * @TODO move to validation package at some point...
 */
public interface RulesSource {

	/**
	 * Return the validation rules for the provided bean.
	 *
	 * @param bean
	 *            the bean class
	 * @return The validation rules, or <code>null</code> if none exist.
	 */
	public Rules getRules(Class bean);

	public Rules getRules(Class bean, String contextId);

	/**
	 * Return the validation rules for the provided bean property.
	 *
	 * @param beanClass
	 *            the bean class
	 * @param propertyName
	 *            the bean propertyName
	 * @return The validation rules, or <code>null</code> if none exist.
	 */
	public PropertyConstraint getPropertyConstraint(Class beanClass, String propertyName);

	public PropertyConstraint getPropertyConstraint(Class beanClass, String propertyName, String contextId);

}