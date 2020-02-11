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
package org.valkyriercp.binding.form.support;

import org.springframework.beans.PropertyAccessException;
import org.valkyriercp.convert.ConversionException;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.form.BindingErrorMessageProvider;
import org.valkyriercp.binding.form.HierarchicalFormModel;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.validation.RichValidator;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResultsModel;
import org.valkyriercp.binding.validation.Validator;
import org.valkyriercp.binding.validation.support.DefaultValidationResults;
import org.valkyriercp.binding.validation.support.DefaultValidationResultsModel;
import org.valkyriercp.binding.validation.support.RulesValidator;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelWrapper;
import org.valkyriercp.util.ValkyrieRepository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Default form model implementation. Is configurable, hierarchical and
 * validating.
 * <p>
 * If you need this form model to use validation rules that are specific to a
 * given context (such as a specific form), then you will need to call
 * {@link #setValidator(Validator)} with a validator configured with the
 * required context id. Like this: <code>
 * RulesValidator validator = myValidatingFormModel.getValidator();
 * validator.setRulesContextId( "mySpecialFormId" );
 * </code>
 * Along with this you will need to register your rules using the context id.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public class DefaultFormModel extends AbstractFormModel implements ValidatingFormModel {

	private final DefaultValidationResultsModel validationResultsModel = new DefaultValidationResultsModel();

	private final DefaultValidationResults additionalValidationResults = new DefaultValidationResults();

	private final Map bindingErrorMessages = new HashMap();

	private boolean validating = true;

	private boolean oldValidating = true;

	private boolean oldHasErrors = false;

	private Validator validator;
    private BindingErrorMessageProvider bindingErrorMessageProvider;

    public DefaultFormModel() {
		init();
	}

	public DefaultFormModel(Object domainObject) {
		super(domainObject);
		init();
	}

	public DefaultFormModel(Object domainObject, boolean buffered) {
		super(domainObject, buffered);
		init();
	}

	public DefaultFormModel(ValueModel domainObjectHolder) {
		super(domainObjectHolder, true);
		init();
	}

	public DefaultFormModel(ValueModel domainObjectHolder, boolean buffered) {
		super(domainObjectHolder, buffered);
		init();
	}

	public DefaultFormModel(MutablePropertyAccessStrategy domainObjectAccessStrategy) {
		super(domainObjectAccessStrategy, true);
		init();
	}

	public DefaultFormModel(MutablePropertyAccessStrategy domainObjectAccessStrategy, boolean bufferChanges) {
		super(domainObjectAccessStrategy, bufferChanges);
		init();
	}

	/**
	 * Initialization of DefaultFormModel. Adds a listener on the Enabled
	 * property in order to switch validating state on or off. When disabling a
	 * formModel, no validation will happen.
	 */
	protected void init() {
		addPropertyChangeListener(ENABLED_PROPERTY, new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				validatingUpdated();
			}

		});
		validationResultsModel.addPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY,
				childStateChangeHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValidating() {
		if (validating && isEnabled()) {
			if (getParent() instanceof ValidatingFormModel)
			{
				return ((ValidatingFormModel)getParent()).isValidating();
			}
			return true;
	    }
		return false;
	}

	public void setValidating(boolean validating) {
		this.validating = validating;
		validatingUpdated();
	}

	protected void validatingUpdated() {
		boolean validating = isValidating();
		if (hasChanged(oldValidating, validating)) {
			if (validating) {
				validate();
			}
			else {
				validationResultsModel.clearAllValidationResults();
			}
			oldValidating = validating;
			firePropertyChange(VALIDATING_PROPERTY, !validating, validating);
		}
	}

	public void addChild(HierarchicalFormModel child) {
		if (child.getParent() == this)
			return;

		super.addChild(child);
		if (child instanceof ValidatingFormModel) {
			getValidationResults().add(((ValidatingFormModel) child).getValidationResults());
			child.addPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY, childStateChangeHandler);
		}
	}

	public void removeChild(HierarchicalFormModel child) {
		if (child instanceof ValidatingFormModel) {
			getValidationResults().remove(((ValidatingFormModel) child).getValidationResults());
			child.removePropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY, childStateChangeHandler);
		}
		super.removeChild(child);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Additionally the {@link DefaultFormModel} adds the event:
	 *
	 * <ul>
	 * <li><em>Has errors event:</em> if the validation results model
	 * contains errors, the form model error state should be revised as well as
	 * the committable state.</li>
	 * </ul>
	 *
	 * Note that we see the {@link ValidationResultsModel} as a child model of
	 * the {@link DefaultFormModel} as the result model is bundled together with
	 * the value models.
	 */
	protected void childStateChanged(PropertyChangeEvent evt) {
		super.childStateChanged(evt);
		if (ValidationResultsModel.HAS_ERRORS_PROPERTY.equals(evt.getPropertyName())) {
			hasErrorsUpdated();
		}
	}

	public void setParent(HierarchicalFormModel parent) {
		super.setParent(parent);
		if (parent instanceof ValidatingFormModel) {
			parent.addPropertyChangeListener(ValidatingFormModel.VALIDATING_PROPERTY, parentStateChangeHandler);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Additionally the {@link DefaultFormModel} adds the event:
	 *
	 * <ul>
	 * <li><em>Validating state:</em> if validating is disabled on parent,
	 * child should not validate as well. If parent is set to validating the
	 * child's former validating state should apply.</li>
	 * </ul>
	 */
	protected void parentStateChanged(PropertyChangeEvent evt) {
		super.parentStateChanged(evt);
		if (ValidatingFormModel.VALIDATING_PROPERTY.equals(evt.getPropertyName())) {
			validatingUpdated();
		}
	}

	public void removeParent() {
		if (getParent() instanceof ValidatingFormModel) {
			getParent().removePropertyChangeListener(ValidatingFormModel.VALIDATING_PROPERTY, parentStateChangeHandler);
		}
		super.removeParent();
	}

	public ValidationResultsModel getValidationResults() {
		return validationResultsModel;
	}

	public boolean getHasErrors() {
		return validationResultsModel.getHasErrors();
	}

	protected void hasErrorsUpdated() {
		boolean hasErrors = getHasErrors();
		if (hasChanged(oldHasErrors, hasErrors)) {
			oldHasErrors = hasErrors;
			firePropertyChange(ValidationResultsModel.HAS_ERRORS_PROPERTY, !hasErrors, hasErrors);
			committableUpdated();
		}
	}

	public void validate() {
		if (isValidating()) {
			validateAfterPropertyChanged(null);
		}
	}

	public Validator getValidator() {
		if (validator == null) {
			setValidator(new RulesValidator(this));
		}
		return validator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Setting a validator will trigger a validate of the current object.
	 * </p>
	 */
	public void setValidator(Validator validator) {
		Assert.notNull(validator, "validator");
		this.validator = validator;
		validate();
	}

	public boolean isCommittable() {
		final boolean superIsCommittable = super.isCommittable();
		final boolean hasNoErrors = !getValidationResults().getHasErrors();
		return superIsCommittable && hasNoErrors;
	}

	protected ValueModel preProcessNewValueModel(String formProperty, ValueModel formValueModel) {
		if (!(formValueModel instanceof ValidatingFormValueModel)) {
			return new ValidatingFormValueModel(formProperty, formValueModel, true);
		}
		return formValueModel;
	}

	protected void postProcessNewValueModel(String formProperty, ValueModel valueModel) {
		validateAfterPropertyChanged(formProperty);
	}

	protected ValueModel preProcessNewConvertingValueModel(String formProperty, Class targetClass,
			ValueModel formValueModel) {
		return new ValidatingFormValueModel(formProperty, formValueModel, false);
	}

	protected void postProcessNewConvertingValueModel(String formProperty, Class targetClass, ValueModel valueModel) {
	}

	protected void formPropertyValueChanged(String formProperty) {
		validateAfterPropertyChanged(formProperty);
	}

	/**
	 *
	 * @param formProperty the name of the only property that has changed since
	 * the last call to validateAfterPropertyChange or <code>null</code> if
	 * this is not known/available.
	 */
	protected void validateAfterPropertyChanged(String formProperty) {
		if (isValidating()) {
			Validator validator = getValidator();
			if (validator != null) {
				DefaultValidationResults validationResults = new DefaultValidationResults(bindingErrorMessages.values());
				if (formProperty != null && validator instanceof RichValidator) {
					validationResults.addAllMessages(((RichValidator) validator)
							.validate(getFormObject(), formProperty));
				}
				else {
					validationResults.addAllMessages(validator.validate(getFormObject()));
				}
				validationResults.addAllMessages(additionalValidationResults);
				validationResultsModel.updateValidationResults(validationResults);
			}
		}
	}

	protected void raiseBindingError(ValidatingFormValueModel valueModel, Object valueBeingSet, Exception e) {
		ValidationMessage oldValidationMessage = (ValidationMessage) bindingErrorMessages.get(valueModel);
		ValidationMessage newValidationMessage = getBindingErrorMessage(valueModel.getFormProperty(), valueBeingSet, e);
		bindingErrorMessages.put(valueModel, newValidationMessage);
		if (isValidating()) {
			validationResultsModel.replaceMessage(oldValidationMessage, newValidationMessage);
		}
	}

	protected void clearBindingError(ValidatingFormValueModel valueModel) {
		ValidationMessage validationMessage = (ValidationMessage) bindingErrorMessages.remove(valueModel);
		if (validationMessage != null) {
			validationResultsModel.removeMessage(validationMessage);
		}
	}

	public void raiseValidationMessage(ValidationMessage validationMessage) {
		additionalValidationResults.addMessage(validationMessage);
		if (isValidating()) {
			validationResultsModel.addMessage(validationMessage);
		}
	}

	public void clearValidationMessage(ValidationMessage validationMessage) {
		additionalValidationResults.removeMessage(validationMessage);
		if (isValidating()) {
			validationResultsModel.removeMessage(validationMessage);
		}
	}

    public BindingErrorMessageProvider getBindingErrorMessageProvider() {
        if(bindingErrorMessageProvider == null)
            return ValkyrieRepository.getInstance().getApplicationConfig().bindingErrorMessageProvider();
        return bindingErrorMessageProvider;
    }

	protected ValidationMessage getBindingErrorMessage(String propertyName, Object valueBeingSet, Exception e) {
		return getBindingErrorMessageProvider().getErrorMessage(this, propertyName, valueBeingSet, e);
	}

	public String toString() {
		return new ToStringCreator(this).append("id", getId()).append("buffered", isBuffered()).append("enabled",
				isEnabled()).append("dirty", isDirty()).append("validating", isValidating()).append(
				"validationResults", getValidationResults()).toString();
	}

    public void setBindingErrorMessageProvider(BindingErrorMessageProvider bindingErrorMessageProvider) {
        this.bindingErrorMessageProvider = bindingErrorMessageProvider;
    }

    protected class ValidatingFormValueModel extends AbstractValueModelWrapper {
		private final String formProperty;

		private final ValueChangeHandler valueChangeHander;

		public ValidatingFormValueModel(String formProperty, ValueModel model, boolean validateOnChange) {
			super(model);
			this.formProperty = formProperty;
			if (validateOnChange) {
				this.valueChangeHander = new ValueChangeHandler();
				addValueChangeListener(valueChangeHander);
			}
			else {
				this.valueChangeHander = null;
			}
		}

		public String getFormProperty() {
			return formProperty;
		}

		public void setValueSilently(Object value, PropertyChangeListener listenerToSkip) {
			try {
				if (logger.isDebugEnabled()) {
					Class valueClass = (value != null ? value.getClass() : null);
					logger.debug("Setting '" + formProperty + "' value to convert/validate '"
							+ (UserMetadata.isFieldProtected(DefaultFormModel.this, formProperty) ? "***" : value)
							+ "', class=" + valueClass);
				}
				super.setValueSilently(value, listenerToSkip);
				clearBindingError(this);
			}
			catch (ConversionException ce) {
				logger.warn("Conversion exception occurred setting value", ce);
				raiseBindingError(this, value, ce);
			}
			catch (PropertyAccessException pae) {
				logger.warn("Type Mismatch Exception occurred setting value", pae);
				raiseBindingError(this, value, pae);
			}
		}

		public class ValueChangeHandler implements PropertyChangeListener {
			public void propertyChange(PropertyChangeEvent evt) {
				formPropertyValueChanged(formProperty);
			}
		}
	}
}
