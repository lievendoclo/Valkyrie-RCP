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