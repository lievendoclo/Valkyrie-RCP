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
package org.valkyriercp.binding.validation.support;

import org.springframework.util.Assert;
import org.valkyriercp.binding.validation.RichValidator;
import org.valkyriercp.binding.validation.ValidationResults;

/**
 * This {@link RichValidator} allows combining several {@link RichValidator}s.
 * Eg when using a validator for Hibernate (validation available on persistent
 * object through annotations), you might want to add a RulesValidator for more
 * specific rules or just to expand its features.
 *
 * @author Jan Hoskens
 *
 */
public class CompositeRichValidator implements RichValidator {

	private RichValidator[] validators;

	/**
	 * Create a {@link CompositeRichValidator} that combines all the results
	 * from the given validators.
	 */
	public CompositeRichValidator(RichValidator... validators) {
		Assert.notNull(validators, "validators should not be null");
		this.validators = validators;
	}

	public ValidationResults validate(Object object, String property) {
		DefaultValidationResults results = new DefaultValidationResults();
		for (int i = 0; i < validators.length; ++i) {
			results.addAllMessages(validators[i].validate(object, property));
		}
		return results;
	}

	public ValidationResults validate(Object object) {
		DefaultValidationResults results = new DefaultValidationResults();
		for (int i = 0; i < validators.length; ++i) {
			results.addAllMessages(validators[i].validate(object));
		}
		return results;
	}

}
