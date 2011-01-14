package org.valkyriercp.form;

import org.springframework.util.Assert;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.binding.validation.ValidationResultsModel;
import org.valkyriercp.core.Messagable;

import java.util.Iterator;

/**
 * An implementation of ValidationResultsReporter that reports only a single
 * message from the configured validation results model to the associated
 * message receiver. More details of the searching process can be found in the
 * {@link #getValidationMessage(ValidationResults)} method.
 *
 * @author Keith Donald
 * @author Jan Hoskens
 */
public class SimpleValidationResultsReporter implements ValidationResultsReporter {

	/** ResultsModel containing the messages. */
	private ValidationResultsModel resultsModel;

	/** Recipient for the message. */
	private Messagable messageReceiver;

	/**
	 * Constructor.
	 *
	 * @param resultsModel ValidationResultsModel to monitor and report on.
	 * @param messageReceiver The receiver for validation messages.
	 */
	public SimpleValidationResultsReporter(ValidationResultsModel resultsModel, Messagable messageReceiver) {
		Assert.notNull(resultsModel, "resultsModel is required");
		Assert.notNull(messageReceiver, "messagePane is required");
		this.resultsModel = resultsModel;
		this.messageReceiver = messageReceiver;
		init();
	}

	/**
	 * Initialize listener and trigger a first-time check.
	 */
	private void init() {
		resultsModel.addValidationListener(this);
		validationResultsChanged(null);
	}

	/**
	 * Clear the messageReceiver.
	 */
	public void clearErrors() {
		messageReceiver.setMessage(null);
	}

	/**
	 * Handle a change in the validation results model. Update the message
	 * receiver based on our current results model state.
	 */
	public void validationResultsChanged(ValidationResults results) {
		if (resultsModel.getMessageCount() == 0) {
			messageReceiver.setMessage(null);
		}
		else {
			ValidationMessage message = getValidationMessage(resultsModel);
			messageReceiver.setMessage(message);
		}
	}

	/**
	 * <p>
	 * Get the message that should be reported.
	 * </p>
	 *
	 * Searching takes following rules into account:
	 * <ul>
	 * <li>Severity of the selected message is the most severe one (INFO <
	 * WARNING < ERROR).</li>
	 * <li>Timestamp of the selected message is the most recent one of the
	 * result of the previous rule.</li>
	 * </ul>
	 *
	 * Any custom Severities will be placed in order according to their given
	 * magnitude.
	 *
	 * @param resultsModel Search this model to find the message.
	 * @return the message to display on the Messagable.
	 */
	protected ValidationMessage getValidationMessage(ValidationResults resultsModel) {
		ValidationMessage validationMessage = null;
        for (Iterator i = resultsModel.getMessages().iterator(); i.hasNext();) {
            ValidationMessage tmpMessage = (ValidationMessage) i.next();
            if (validationMessage == null
                    || (validationMessage.getSeverity().compareTo(tmpMessage.getSeverity()) < 0)
                    || ((validationMessage.getTimestamp() < tmpMessage.getTimestamp()) && (validationMessage
                            .getSeverity() == tmpMessage.getSeverity()))) {
                validationMessage = tmpMessage;
            }
        }
        return validationMessage;
	}

	public boolean hasErrors() {
		return resultsModel.getHasErrors();
	}
}