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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.support.FormModelPropertyAccessStrategy;
import org.valkyriercp.binding.validation.RichValidator;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.PropertyConstraintProvider;
import org.valkyriercp.rules.Rules;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.rules.reporting.BeanValidationResultsCollector;
import org.valkyriercp.rules.reporting.MessageTranslator;
import org.valkyriercp.rules.reporting.ObjectNameResolver;
import org.valkyriercp.rules.reporting.PropertyResults;
import org.valkyriercp.util.ValkyrieRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Implementation of a {@link RichValidator} which will check the formObject
 * against rules found in a {@link RulesSource}. This {@link RulesSource} can
 * be specifically supplied, which allows multiple rulesSources, or can be
 * globally defined in the Application Context. In the latter case the
 * {@link RulesValidator} will look for the specific {@link RulesSource} type in
 * the context.
 * </p>
 *
 * <p>
 * When validating an object, all results are cached. Any following validation
 * of a specific property will validate that property, update the cached results
 * accordingly and return <em>all</em> validation results of the object.
 * </p>
 *
 * @author Keith Donald
 * @author Jan Hoskens
 */
public class RulesValidator implements RichValidator, ObjectNameResolver {

	private static final Log logger = LogFactory.getLog(RulesValidator.class);

	private final DefaultValidationResults results = new DefaultValidationResults();

	private MessageTranslator messageTranslator;

	private final Map validationErrors = new HashMap();

	private final FormModel formModel;

	private BeanValidationResultsCollector validationResultsCollector;

	private RulesSource rulesSource;

	private String rulesContextId = null;

	private Class objectClass;

	/**
	 * Creates a RulesValidator for the given formModel. When no RulesSource is
	 * given, a default/global RulesSource is retrieved by the
	 * ApplicationServices class.
	 */
	public RulesValidator(FormModel formModel) {
		this(formModel, null);
	}

	/**
	 * Create a RulesValidator which uses the supplied RulesSource on the
	 * FormModel.
	 */
	public RulesValidator(FormModel formModel, RulesSource rulesSource) {
		this.formModel = formModel;
		this.rulesSource = rulesSource;
		validationResultsCollector = new BeanValidationResultsCollector(new FormModelPropertyAccessStrategy(formModel));
        messageTranslator = getApplicationConfig().messageTranslatorFactory().createTranslator(this);

	}

	/**
	 * {@inheritDoc}
	 */
	public org.valkyriercp.binding.validation.ValidationResults validate(Object object) {
		return validate(object, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public org.valkyriercp.binding.validation.ValidationResults validate(Object object, String propertyName) {
		// Forms can have different types of objects, so when type of object
		// changes, messages that are already listed on the previous type must
		// be removed. If evaluating the whole object (propertyName == null)
		// also clear results.
		if ((propertyName == null) || ((objectClass != null) && objectClass != object.getClass())) {
			clearMessages();
		}
		objectClass = object.getClass();
		Rules rules = null;
		if (object instanceof PropertyConstraintProvider) {
			PropertyConstraintProvider propertyConstraintProvider = (PropertyConstraintProvider) object;
			if (propertyName != null) {
				PropertyConstraint validationRule = propertyConstraintProvider.getPropertyConstraint(propertyName);
				checkRule(validationRule);
			}
			else {
				for (Iterator fieldNamesIter = formModel.getFieldNames().iterator(); fieldNamesIter.hasNext();) {
					PropertyConstraint validationRule = propertyConstraintProvider
							.getPropertyConstraint((String) fieldNamesIter.next());
					checkRule(validationRule);
				}
			}
		}
		else {
			if (getRulesSource() != null) {
				rules = getRulesSource().getRules(objectClass, getRulesContextId());
				if (rules != null) {
					for (Iterator i = rules.iterator(); i.hasNext();) {
						PropertyConstraint validationRule = (PropertyConstraint) i.next();
						if (propertyName == null) {
							if (formModel.hasValueModel(validationRule.getPropertyName())) {
								checkRule(validationRule);
							}
						}
						else if (validationRule.isDependentOn(propertyName)) {
							checkRule(validationRule);
						}
					}
				}
			}
			else {
				logger.debug("No rules source has been configured; "
						+ "please set a valid reference to enable rules-based validation.");
			}
		}
		return results;
	}

	private void checkRule(PropertyConstraint validationRule) {
		if (validationRule == null)
			return;
		BeanValidationResultsCollector resultsCollector = takeResultsCollector();
		PropertyResults results = resultsCollector.collectPropertyResults(validationRule);
		returnResultsCollector(resultsCollector);
		if (results == null) {
			constraintSatisfied(validationRule);
		}
		else {
			constraintViolated(validationRule, results);
		}
	}

	protected void constraintSatisfied(PropertyConstraint exp) {
		ValidationMessage message = (ValidationMessage) validationErrors.remove(exp);
		if (message != null) {
			results.removeMessage(message);
		}
	}

	protected void constraintViolated(PropertyConstraint exp, PropertyResults propertyResults) {
		ValidationMessage message = new DefaultValidationMessage(exp.getPropertyName(), Severity.ERROR,
				messageTranslator.getMessage(propertyResults));

		ValidationMessage oldMessage = (ValidationMessage) validationErrors.get(exp);
		if (!message.equals(oldMessage)) {
			results.removeMessage(oldMessage);
			validationErrors.put(exp, message);
			results.addMessage(message);
		}
	}

	private RulesSource getRulesSource() {
		if (rulesSource == null) {
			rulesSource = getApplicationConfig().rulesSource();
		}
		return rulesSource;
	}

	private BeanValidationResultsCollector takeResultsCollector() {
		BeanValidationResultsCollector resultsCollector = validationResultsCollector;
		if (resultsCollector != null) {
			validationResultsCollector = null;
		}
		else {
			resultsCollector = new BeanValidationResultsCollector(new FormModelPropertyAccessStrategy(formModel));
		}
		return resultsCollector;
	}

	private void returnResultsCollector(BeanValidationResultsCollector resultsCollector) {
		validationResultsCollector = resultsCollector;
	}

	/**
	 * Returns the rules context id set on this validator.
	 */
	public String getRulesContextId() {
		return rulesContextId;
	}

	/**
	 * Set the rules context id. This is passed in the call to
	 * {@link RulesSource#getRules(Class, String)} to allow for context specific
	 * rules.
	 * @param rulesContextId
	 */
	public void setRulesContextId(String rulesContextId) {
		this.rulesContextId = rulesContextId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String resolveObjectName(String objectName) {
		return formModel.getFieldFace(objectName).getDisplayName();
	}

	/**
	 * Clear the current validationMessages and the errors.
	 *
	 * @see #validate(Object, String)
	 */
	public void clearMessages() {
		this.results.clearMessages();
		this.validationErrors.clear();
	}

    public ApplicationConfig getApplicationConfig() {
        return ValkyrieRepository.getInstance().getApplicationConfig();
    }
}
