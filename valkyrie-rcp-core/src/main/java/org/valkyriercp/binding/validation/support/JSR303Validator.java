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
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.validation.RichValidator;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.reporting.ObjectNameResolver;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class JSR303Validator<T>  implements RichValidator<T>, ObjectNameResolver {

	private ValidatingFormModel formModel;
    private Class<T> beanClass;
	private final Validator validator;

	private Set<String> ignoredProperties;

    private DefaultValidationResults results = new DefaultValidationResults();

	/**
	 * Creates a new JSR303Validator without ignoring any properties.
	 *
	 * @param formModel The {@link ValidatingFormModel} on which validation
	 * needs to occur
	 * @param beanClass The class of the object this validator needs to check
	 */
	public JSR303Validator(ValidatingFormModel formModel, Class<T> beanClass) {
		this(formModel, beanClass, new HashSet<String>());
	}

	/**
	 * Creates a new JSR303Validator with additionally a set of
	 * properties that should not be validated.
	 *
	 * @param formModel The {@link ValidatingFormModel} on which validation
	 * needs to occur
	 * @param beanClass The class of the object this validator needs to check
	 * @param ignoredProperties properties that should not be checked
	 * though are
	 */
	public JSR303Validator(ValidatingFormModel formModel, Class<T> beanClass,
                                   Set<String> ignoredProperties) {
		this.formModel = formModel;
        this.beanClass = beanClass;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		this.ignoredProperties = ignoredProperties;
	}

	/**
	 * {@inheritDoc}
	 */
	public ValidationResults validate(T object) {
		return validate(object, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public ValidationResults validate(T object, String propertyName) {
		if (propertyName == null) {
			results.clearMessages();
		}
		else {
			results.clearMessages(propertyName);
		}

		addInvalidValues(doValidate(object, propertyName));
		return results;
	}

	/**
	 * Add all {@link ConstraintViolation}s to the {@link ValidationResults}.
	 */
	protected void addInvalidValues(Set<ConstraintViolation<T>> invalidValues) {
		if (invalidValues != null) {
			for (ConstraintViolation invalidValue : invalidValues) {
				results.addMessage(translateMessage(invalidValue));
			}
		}
	}

	/**
	 * Translate a single {@link ConstraintViolation} to a {@link ValidationMessage}.
	 */
	protected ValidationMessage translateMessage(ConstraintViolation invalidValue) {
		return new DefaultValidationMessage(invalidValue.getPropertyPath().toString(), Severity.ERROR,
				resolveObjectName(invalidValue.getPropertyPath().toString()) + " " + invalidValue.getMessage());
	}

	/**
	 * Validates the object through Hibernate Validator
	 *
	 * @param object The object that needs to be validated
	 * @param property The properties that needs to be validated
	 * @return An array of {@link ConstraintViolation}, containing all validation
	 * errors
	 */
	protected Set<ConstraintViolation<T>> doValidate(final T object, final String property) {
		if (property == null) {
			final Set<ConstraintViolation<T>> ret = new HashSet<>();
			PropertyDescriptor[] propertyDescriptors;
			try {
				propertyDescriptors = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();
			}
			catch (IntrospectionException e) {
				throw new IllegalStateException("Could not retrieve property information");
			}
			for (final PropertyDescriptor prop : propertyDescriptors) {
				String propertyName = prop.getName();
				if (formModel.hasValueModel(propertyName) && !ignoredProperties.contains(propertyName)) {
					final Set<ConstraintViolation<T>> result = validator.validateValue(beanClass, propertyName, formModel
							.getValueModel(propertyName).getValue()); //validator.validateProperty(object, propertyName);
					if (result != null) {
						ret.addAll(result);
					}
				}
			}
			return ret;
		}
		else if (!ignoredProperties.contains(property) && formModel.hasValueModel(property)) {
			return validator.validateValue(beanClass, property, formModel
							.getValueModel(property).getValue());
		}
		else {
			return null;
		}
	}

	/**
	 * Clear the current validationMessages and the errors.
	 *
	 * @see #validate(Object, String)
	 */
	public void clearMessages() {
		this.results.clearMessages();
	}

	/**
	 * Add a property for the validator to ignore.
	 *
	 * @param propertyName Name of the property to ignore. Cannot be null.
	 */
	public void addIgnoredProperty(String propertyName) {
		Assert.notNull(propertyName, "propertyName should not be null");
		ignoredProperties.add(propertyName);
	}

	/**
	 * Remove a property for the Hibernate validator to ignore.
	 *
	 * @param propertyName Name of the property to be removed. Cannot be null.
	 */
	public void removeIgnoredProperty(String propertyName) {
		Assert.notNull(propertyName, "propertyName should not be null");
		ignoredProperties.remove(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	public String resolveObjectName(String objectName) {
		return formModel.getFieldFace(objectName).getDisplayName();
	}
}
