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
package org.valkyriercp.binding.validation;

/**
 * Interface to be implemented by classes capable of incrementally validating
 * domain objects. This interface is intended for validators that are able to
 * validate a subset of the domain objects properties.
 *
 * @author Oliver Hutchison
 */
public interface RichValidator<T> extends Validator<T> {

	/**
	 * Validates the given property of the provided object.
	 *
	 * @param object the object to validate (may be an implementation of
	 * @link org.springframework.binding.PropertyAccessStrategy)
	 * @param property the name of the only property that has changed since the
	 * last call to validate.
	 * @return the results on the validation
	 */
	ValidationResults validate(T object, String property);
}
