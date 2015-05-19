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
package org.valkyriercp.binding.form;

import org.valkyriercp.binding.validation.ValidationMessage;

/**
 * A class that can generate ValidationMessages for exception that occur during
 * a form model's binding process.
 *
 * @author Oliver Hutchison
 */
public interface BindingErrorMessageProvider {

	/**
	 * Translates the provided exception details into a ValidationMessage that
	 * will be used to provide feedback to the end user. Generally these
	 * exceptions results from type conversion problems.
	 *
	 * @param formModel the formModel
	 * @param propertyName the propertyName
	 * @param valueBeingSet the value which triggered the exception
	 */
	ValidationMessage getErrorMessage(FormModel formModel, String propertyName, Object valueBeingSet, Exception e);

}