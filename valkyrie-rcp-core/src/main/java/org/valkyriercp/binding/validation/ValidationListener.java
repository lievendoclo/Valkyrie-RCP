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
 * Specific listener interface to facilitate passing of
 * {@link ValidationResults}.
 *
 * @author Keith Donald
 */
public interface ValidationListener {

	/**
	 * Fired whenever there is a change to set of validation results.
	 *
	 * @param results the set of valiation results, this set will incude results
	 * for all properties even if the listener is only listening for changes to
	 * a specific properties results.
	 */
	public void validationResultsChanged(ValidationResults results);
}
