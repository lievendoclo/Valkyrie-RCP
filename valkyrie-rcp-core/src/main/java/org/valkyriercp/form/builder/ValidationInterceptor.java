package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.validation.ValidationListener;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.binding.validation.ValidationResultsModel;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Messagable;

import java.util.Iterator;

/**
 * Adds basic functionality to deal with Validation. Guards and messageReceivers
 * can be added to the {@link ValidationResultsModel} of the
 * {@link ValidatingFormModel}.
 *
 * @author oliverh
 */
public abstract class ValidationInterceptor extends AbstractFormComponentInterceptor {

	private final ValidationResultsModel validationResults;

	/**
	 * @param formModel the {@link ValidatingFormModel} to operate on.
	 */
	public ValidationInterceptor(FormModel formModel) {
		super(formModel);
		validationResults = ((ValidatingFormModel) formModel).getValidationResults();
	}

	/**
	 * Register a messageReceiver on a specific property. To keep things in
	 * sync, it also triggers a first time check. (validationResultsModel can
	 * already be populated)
	 *
	 * @param propertyName property to listen for.
	 * @param messageReceiver message capable component.
	 * @return {@link ValidationListener} created during the process.
	 */
	protected ValidationListener registerMessageReceiver(String propertyName, Messagable messageReceiver) {
		MessagableValidationListener messagableValidationListener = new MessagableValidationListener(propertyName,
				messageReceiver);
		validationResults.addValidationListener(propertyName, messagableValidationListener);
		messagableValidationListener.validationResultsChanged(validationResults);
		return messagableValidationListener;
	}

	/**
	 * Register a guarded object on a specific property. To keep things in sync,
	 * it also triggers a first time check. (validationResultsModel can already
	 * be populated)
	 *
	 * @param propertyName property to listen for.
	 * @param guarded component that needs guarding.
	 * @return {@link ValidationListener} created during the process.
	 */
	protected ValidationListener registerGuarded(String propertyName, Guarded guarded) {
		GuardedValidationListener guardedValidationListener = new GuardedValidationListener(propertyName, guarded);
		validationResults.addValidationListener(propertyName, guardedValidationListener);
		guardedValidationListener.validationResultsChanged(validationResults);
		return guardedValidationListener;
	}

	/**
	 * {@link ValidationListener} that will handle validation messages of the
	 * given property.
	 */
	private static class MessagableValidationListener implements ValidationListener {
		private final String propertyName;

		private final Messagable messageReceiver;

		public MessagableValidationListener(String propertyName, Messagable messageReceiver) {
			this.propertyName = propertyName;
			this.messageReceiver = messageReceiver;
		}

		public void validationResultsChanged(ValidationResults results) {
			if (results.getMessageCount(propertyName) > 0) {
				ValidationMessage message = getNewestMessage(results);
				messageReceiver.setMessage(message);
			}
			else {
				messageReceiver.setMessage(null);
			}
		}

		protected ValidationMessage getNewestMessage(ValidationResults results) {
			ValidationMessage newestMessage = null;
			for (Iterator i = results.getMessages(propertyName).iterator(); i.hasNext();) {
				ValidationMessage message = (ValidationMessage) i.next();
				if (newestMessage == null || newestMessage.getTimestamp() < message.getTimestamp()) {
					newestMessage = message;
				}
			}
			return newestMessage;
		}
	}

	/**
	 * {@link ValidationListener} that will handle the enabling of the guard
	 * according to the validation of the given property.
	 */
	private static class GuardedValidationListener implements ValidationListener {
		private final String propertyName;

		private final Guarded guarded;

		public GuardedValidationListener(String propertyName, Guarded guarded) {
			this.propertyName = propertyName;
			this.guarded = guarded;
		}

		public void validationResultsChanged(ValidationResults results) {
			guarded.setEnabled(results.getMessageCount(propertyName) == 0);
		}
	}
}