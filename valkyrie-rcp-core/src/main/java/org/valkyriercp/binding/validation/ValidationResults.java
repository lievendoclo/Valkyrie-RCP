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

import org.valkyriercp.core.Severity;

import java.util.Set;

/**
 * Interface to be implemented by objects that hold a list of validation results
 * for a specific object.
 *
 * @author Oliver Hutchison
 * @see org.springframework.binding.validation.support.DefaultValidationResults
 */
public interface ValidationResults {

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.ERROR</code>.
	 */
	boolean getHasErrors();

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.WARNING</code>.
	 */
	boolean getHasWarnings();

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.INFO</code>.
	 */
	boolean getHasInfo();

	/**
	 * Returns the total number of validation messages.
	 */
	int getMessageCount();

	/**
	 * Returns the total number of validation messages of the specified
	 * Severity.
	 */
	int getMessageCount(Severity severity);

	/**
	 * Returns the total number of validation messages that apply to the
	 * specified property name.
	 */
	int getMessageCount(String propertyName);

	/**
	 * Returns a set holding all of the validation messages.
	 */
	Set getMessages();

	/**
	 * Returns a set holding all of the validation messages of the specified
	 * Severity.
	 */
	Set getMessages(Severity severity);

	/**
	 * Returns a set holding all of the validation messages that apply to the
	 * specified property name.
	 */
	Set getMessages(String propertyName);
}