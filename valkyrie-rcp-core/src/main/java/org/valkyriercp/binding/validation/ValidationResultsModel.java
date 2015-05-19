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

import org.valkyriercp.core.PropertyChangePublisher;

/**
 * Adds the propertyChange and parent-child aspect to the
 * {@link ValidationResults} interface. Listeners can be registered on
 * validation changes of a specific property or on all changes. Additionally a
 * validationResultsModel has to be aware of its parent-child relationships to
 * forward these changes in its ValidationResults.
 *
 * @author Oliver Hutchison
 */
public interface ValidationResultsModel extends ValidationResults, PropertyChangePublisher {

	/** The name of the bound property <em>hasErrors</em>. */
	String HAS_ERRORS_PROPERTY = "hasErrors";

	/** The name of the bound property <em>hasWarnings</em>. */
	String HAS_WARNINGS_PROPERTY = "hasWarnings";

	/** The name of the bound property <em>hasInfos</em>. */
	String HAS_INFO_PROPERTY = "hasInfo";

	/**
	 * Adds a listener that will be notified when there is any change to the set
	 * of validation messages.
	 */
	void addValidationListener(ValidationListener listener);

	/**
	 * Removes the provided validation listener.
	 */
	void removeValidationListener(ValidationListener listener);

	/**
	 * Adds a listener that will be notified when there is any change to the set
	 * validation messages for the specified property.
	 */
	void addValidationListener(String propertyName, ValidationListener listener);

	/**
	 * Removes the provided validation listener.
	 */
	void removeValidationListener(String propertyName, ValidationListener listener);

	/**
	 * Add a validationResultsModel as a child to this one. Results originating
	 * from child models have to be taken into account by the parent.
	 *
	 * @param validationResultsModel
	 */
	void add(ValidationResultsModel validationResultsModel);

	/**
	 * Remove the given validationResultsModel from the list of children.
	 *
	 * @param validationResultsModel
	 */
	void remove(ValidationResultsModel validationResultsModel);
}
