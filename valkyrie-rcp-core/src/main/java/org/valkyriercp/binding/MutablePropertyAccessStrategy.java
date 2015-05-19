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
package org.valkyriercp.binding;

import org.springframework.beans.BeansException;
import org.valkyriercp.binding.value.ValueModel;

/**
 * <p>
 * An extension of the base property access strategy interface that allows for
 * mutable operations. Specifically, this interface allows:
 * </p>
 * <ul>
 * <li>registering custom property editors for performing type conversions</li>
 * <li>returning a domain object holder allowing the underlying domain object
 * to be changed and subscribed to for modification, and</li>
 * <li>adding listeners for changes on particular properties.</li>
 * </ul>
 *
 * @author Keith Donald
 */
public interface MutablePropertyAccessStrategy extends PropertyAccessStrategy {

	/**
	 * Get the <code>ValueModel</code> used to access the domainObject.
	 *
	 * @return the <code>ValueModel</code> of the domainObject.
	 */
	ValueModel getDomainObjectHolder();

	/**
	 * Get the <code>ValueModel</code> to access the given property. Possibly
	 * creating the valueModel if needed.
	 *
	 * @param propertyPath property to access.
	 * @return <code>ValueModel</code> that handles the given property.
	 * @throws org.springframework.beans.BeansException
	 */
	ValueModel getPropertyValueModel(String propertyPath) throws BeansException;

	/**
	 * Get a <code>MutablePropertyAccessStrategy</code> for the given
	 * property.
	 *
	 * TODO check why this exists and where this is used.
	 *
	 * @param propertyPath property.
	 * @return <code>MutablePropertyAccessStrategy</code> for the given
	 * property.
	 * @throws BeansException
	 */
	MutablePropertyAccessStrategy getPropertyAccessStrategyForPath(String propertyPath) throws BeansException;

	/**
	 * Return a new <code>MutablePropertyAccessStrategy</code> for the given
	 * valueModel.
	 *
	 * TODO check why this exists and where this is used.
	 *
	 * @param domainObjectHolder a <code>ValueModel</code> containing the
	 * domainObject.
	 * @return a newly created <code>MutablePropertyAccessStrategy</code>.
	 */
	MutablePropertyAccessStrategy newPropertyAccessStrategy(ValueModel domainObjectHolder);
}


