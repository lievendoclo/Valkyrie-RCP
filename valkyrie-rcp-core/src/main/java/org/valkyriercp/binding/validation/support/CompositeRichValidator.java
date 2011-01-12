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
		Assert.notNull(validators);
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
