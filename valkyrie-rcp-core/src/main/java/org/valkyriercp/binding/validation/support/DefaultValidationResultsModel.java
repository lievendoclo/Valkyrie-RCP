package org.valkyriercp.binding.validation.support;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
//import org.springframework.util.CachingMapDecorator;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.binding.validation.ValidationListener;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.binding.validation.ValidationResultsModel;
import org.valkyriercp.core.Severity;
import org.valkyriercp.util.CachingMapDecorator;
import org.valkyriercp.util.EventListenerListHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Default implementation of {@link ValidationResultsModel}. Several events are
 * fired when validationResults are set and can be tracked by registering the
 * appropriate listener.
 *
 * <p>
 * You can register listeners on:
 * </p>
 * <ul>
 * <li>Changes of the ValidationResults in general. ({@link #addValidationListener(ValidationListener)})</li>
 * <li>Changes of validationResults concerning a specific property of the
 * FormModel. ({@link #addValidationListener(String, ValidationListener)})</li>
 * <li>Specific events concerning errors, warnings and info. ({@link #addPropertyChangeListener(String, java.beans.PropertyChangeListener) with one of:
 * ValidationResultsModel#HAS_ERRORS_PROPERTY,
 * ValidationResultsModel#HAS_INFO_PROPERTY or
 * ValidationResultsModel#HAS_WARNINGS_PROPERTY)</li>
 * </ul>
 *
 * <p>
 * A child-parent relation can be used to bundle events and results. A listener
 * set on a parent will receive events originating from the child and when
 * polling for messages, childMessages will be available as well. This makes it
 * possible to efficiently couple formModels and their validation aspect and
 * provides a means to bundle validation reporting. When eg using a
 * {@link org.springframework.richclient.form.ValidationResultsReporter}, you have the opportunity to bundle
 * results from various unrelated formModels to report to one end point.</p>
 *
 * <p>Example:</p>
 * <pre>
 * DefaultFormModel formModelA = ...
 * DefaultFormModel formModelChildOfA = ...
 * formModelA.addChild(formModelChildOfA);
 *
 * DefaultFormModel formModelB = ...
 *
 * \\ At this stage, the ValidationResultsModel of formModelChildOfA will route results &amp;
 * \\ events to the ValidationResultsModel of formModelA
 *
 * DefaultValidationResultsModel container = new DefaultValidationResultsModel();
 * container.add(formModelA.getValidationResults());
 * container.add(formModelB.getValidationResults());
 *
 * new SimpleValidationResultsReporter(container, messagable);
 *
 * \\ the reporter will now receive events &amp; results of all formModels and can show messages of each of them
 *
 * </pre>
 *
 * @see org.springframework.binding.form.support.DefaultFormModel#addChild(org.springframework.binding.form.HierarchicalFormModel)
 * @see org.springframework.richclient.form.SimpleValidationResultsReporter
 *
 * @author Oliver Hutchison
 * @author Jan Hoskens
 */
public class DefaultValidationResultsModel implements ValidationResultsModel, ValidationListener,
        PropertyChangeListener {

	private final EventListenerListHelper validationListeners = new EventListenerListHelper(ValidationListener.class);

	private final CachingMapDecorator propertyValidationListeners = new org.valkyriercp.util.CachingMapDecorator() {

		protected Object create(Object propertyName) {
			return new EventListenerListHelper(ValidationListener.class);
		}
	};

	private final CachingMapDecorator propertyChangeListeners = new org.valkyriercp.util.CachingMapDecorator() {

		protected Object create(Object propertyName) {
			return new EventListenerListHelper(PropertyChangeListener.class);
		}
	};

	/** Delegate or reference to this. */
	private final ValidationResultsModel delegateFor;

	/** All children connected to this {@link DefaultValidationResultsModel}. */
	private List children = new ArrayList();

	/** The actual results for this instance only. */
	private ValidationResults validationResults = EmptyValidationResults.INSTANCE;

	/** Error bookkeeping. */
	private boolean hasErrors = false;

	/** Warning bookkeeping. */
	private boolean hasWarnings = false;

	/** Info bookkeeping. */
	private boolean hasInfo = false;

	/**
	 * Constructor without delegate. (Delegating for 'this').
	 */
	public DefaultValidationResultsModel() {
		delegateFor = this;
	}

	/**
	 * Constructor with delegate.
	 *
	 * @param delegateFor delegate object.
	 */
	public DefaultValidationResultsModel(ValidationResultsModel delegateFor) {
		this.delegateFor = delegateFor;
	}

	public void updateValidationResults(ValidationResults newValidationResults) {
		Assert.notNull(newValidationResults, "newValidationResults");
		ValidationResults oldValidationResults = validationResults;
		validationResults = newValidationResults;
		if (oldValidationResults.getMessageCount() == 0 && validationResults.getMessageCount() == 0) {
			return;
		}
		fireChangedEvents();
		for (Iterator i = propertyValidationListeners.keySet().iterator(); i.hasNext();) {
			String propertyName = (String) i.next();
			if (oldValidationResults.getMessageCount(propertyName) > 0
					|| validationResults.getMessageCount(propertyName) > 0) {
				fireValidationResultsChanged(propertyName);
			}
		}
	}

	// TODO: test
	public void addMessage(ValidationMessage validationMessage) {
		if (!validationResults.getMessages().contains(validationMessage)) {
			ValidationResults oldValidationResults = validationResults;
			List newMessages = new ArrayList(oldValidationResults.getMessages());
			newMessages.add(validationMessage);
			validationResults = new DefaultValidationResults(newMessages);
			fireChangedEvents();
			fireValidationResultsChanged(validationMessage.getProperty());
		}
	}

	// TODO: test
	public void removeMessage(ValidationMessage validationMessage) {
		if (validationResults.getMessages().contains(validationMessage)) {
			ValidationResults oldValidationResults = validationResults;
			List newMessages = new ArrayList(oldValidationResults.getMessages());
			newMessages.remove(validationMessage);
			validationResults = new DefaultValidationResults(newMessages);
			fireChangedEvents();
			fireValidationResultsChanged(validationMessage.getProperty());
		}
	}

	// TODO: test
	public void replaceMessage(ValidationMessage messageToReplace, ValidationMessage replacementMessage) {
		ValidationResults oldValidationResults = validationResults;
		List newMessages = new ArrayList(oldValidationResults.getMessages());
		final boolean containsMessageToReplace = validationResults.getMessages().contains(messageToReplace);
		if (containsMessageToReplace) {
			newMessages.remove(messageToReplace);
		}
		newMessages.add(replacementMessage);
		validationResults = new DefaultValidationResults(newMessages);
		fireChangedEvents();
		if (containsMessageToReplace
				&& !ObjectUtils.nullSafeEquals(messageToReplace.getProperty(), replacementMessage.getProperty())) {
			fireValidationResultsChanged(messageToReplace.getProperty());
		}
		fireValidationResultsChanged(replacementMessage.getProperty());
	}

	public void clearAllValidationResults() {
		updateValidationResults(EmptyValidationResults.INSTANCE);
	}

	/**
	 * @return <code>true</code> if this instance of one of its children has
	 * errors contained in their results.
	 */
	public boolean getHasErrors() {
		return hasErrors;
	}

	/**
	 * Revaluate the hasErrors property and fire an event if things have
	 * changed.
	 */
	private void updateErrors() {
		boolean oldErrors = hasErrors;
		hasErrors = false;
		if (validationResults.getHasErrors()) {
			hasErrors = true;
		}
		else {
			Iterator childIter = children.iterator();
			while (childIter.hasNext()) {
				ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
				if (childModel.getHasErrors()) {
					hasErrors = true;
					break;
				}
			}
		}
		firePropertyChange(HAS_ERRORS_PROPERTY, oldErrors, hasErrors);
	}

	/**
	 * @return <code>true</code> if this instance of one of its children has
	 * info contained in their results.
	 */
	public boolean getHasInfo() {
		return hasInfo;
	}

	/**
	 * Revaluate the hasInfo property and fire an event if things have changed.
	 */
	private void updateInfo() {
		boolean oldInfo = hasInfo;
		hasInfo = false;
		if (validationResults.getHasInfo()) {
			hasInfo = true;
		}
		else {
			Iterator childIter = children.iterator();
			while (childIter.hasNext()) {
				ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
				if (childModel.getHasInfo()) {
					hasInfo = true;
					break;
				}
			}
		}
		firePropertyChange(HAS_INFO_PROPERTY, oldInfo, hasInfo);
	}

	/**
	 * @return <code>true</code> if this instance of one of its children has
	 * warnings contained in their results.
	 */
	public boolean getHasWarnings() {
		return hasWarnings;
	}

	/**
	 * Revaluate the hasWarnings property and fire an event if things have
	 * changed.
	 */
	private void updateWarnings() {
		boolean oldWarnings = hasWarnings;
		hasWarnings = false;
		if (validationResults.getHasWarnings()) {
			hasWarnings = true;
		}
		else {
			Iterator childIter = children.iterator();
			while (childIter.hasNext()) {
				ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
				if (childModel.getHasWarnings()) {
					hasWarnings = true;
					break;
				}
			}
		}
		firePropertyChange(HAS_WARNINGS_PROPERTY, oldWarnings, hasWarnings);
	}

	public int getMessageCount() {
		int count = validationResults.getMessageCount();
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			count += childModel.getMessageCount();
		}
		return count;
	}

	public int getMessageCount(Severity severity) {
		int count = validationResults.getMessageCount(severity);
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			count += childModel.getMessageCount(severity);
		}
		return count;
	}

	public int getMessageCount(String propertyName) {
		int count = validationResults.getMessageCount(propertyName);
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			count += childModel.getMessageCount(propertyName);
		}
		return count;
	}

	public Set getMessages() {
		Set messages = new HashSet();
		messages.addAll(validationResults.getMessages());
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			messages.addAll(childModel.getMessages());
		}
		return messages;
	}

	public Set getMessages(Severity severity) {
		Set messages = new HashSet();
		messages.addAll(validationResults.getMessages(severity));
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			messages.addAll(childModel.getMessages(severity));
		}
		return messages;
	}

	public Set getMessages(String propertyName) {
		Set messages = new HashSet();
		messages.addAll(validationResults.getMessages(propertyName));
		Iterator childIter = children.iterator();
		while (childIter.hasNext()) {
			ValidationResultsModel childModel = (ValidationResultsModel) childIter.next();
			messages.addAll(childModel.getMessages(propertyName));
		}
		return messages;
	}

	public void addValidationListener(ValidationListener listener) {
		validationListeners.add(listener);
	}

	public void removeValidationListener(ValidationListener listener) {
		validationListeners.remove(listener);
	}

	public void addValidationListener(String propertyName, ValidationListener listener) {
		getValidationListeners(propertyName).add(listener);
	}

	public void removeValidationListener(String propertyName, ValidationListener listener) {
		getValidationListeners(propertyName).remove(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException("This method is not implemented");
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException("This method is not implemented");
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		getPropertyChangeListeners(propertyName).add(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		getPropertyChangeListeners(propertyName).remove(listener);
	}

	protected void fireChangedEvents() {
		updateErrors();
		updateWarnings();
		updateInfo();
		fireValidationResultsChanged();
	}

	protected void fireValidationResultsChanged() {
		validationListeners.fire("validationResultsChanged", delegateFor);
	}

	protected void fireValidationResultsChanged(String propertyName) {
		for (Iterator i = getValidationListeners(propertyName).iterator(); i.hasNext();) {
			((ValidationListener) i.next()).validationResultsChanged(delegateFor);
		}
	}

	protected EventListenerListHelper getValidationListeners(String propertyName) {
		return ((EventListenerListHelper) propertyValidationListeners.get(propertyName));
	}

	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		if (oldValue != newValue) {
			EventListenerListHelper propertyChangeListeners = getPropertyChangeListeners(propertyName);
			if (propertyChangeListeners.hasListeners()) {
				PropertyChangeEvent event = new PropertyChangeEvent(delegateFor, propertyName, Boolean
						.valueOf(oldValue), Boolean.valueOf(newValue));
				propertyChangeListeners.fire("propertyChange", event);
			}
		}
	}

	protected EventListenerListHelper getPropertyChangeListeners(String propertyName) {
		return ((EventListenerListHelper) propertyChangeListeners.get(propertyName));
	}

	public String toString() {
		return new ToStringCreator(this).append("messages", getMessages()).toString();
	}

	/**
	 * Add a validationResultsModel as a child to this one. Attach listeners and
	 * if it already has messages, fire events.
	 *
	 * @param validationResultsModel
	 */
	public void add(ValidationResultsModel validationResultsModel) {
		if (children.add(validationResultsModel)) {
			validationResultsModel.addValidationListener(this);
			validationResultsModel.addPropertyChangeListener(HAS_ERRORS_PROPERTY, this);
			validationResultsModel.addPropertyChangeListener(HAS_WARNINGS_PROPERTY, this);
			validationResultsModel.addPropertyChangeListener(HAS_INFO_PROPERTY, this);
			if ((validationResultsModel.getMessageCount() > 0))
				fireChangedEvents();
		}
	}

	/**
	 * Remove the given validationResultsModel from the list of children. Remove
	 * listeners and if it had messages, fire events.
	 *
	 * @param validationResultsModel
	 */
	public void remove(ValidationResultsModel validationResultsModel) {
		if (children.remove(validationResultsModel)) {
			validationResultsModel.removeValidationListener(this);
			validationResultsModel.removePropertyChangeListener(HAS_ERRORS_PROPERTY, this);
			validationResultsModel.removePropertyChangeListener(HAS_WARNINGS_PROPERTY, this);
			validationResultsModel.removePropertyChangeListener(HAS_INFO_PROPERTY, this);
			if (validationResultsModel.getMessageCount() > 0)
				fireChangedEvents();
		}
	}

	/**
	 * {@link DefaultValidationResultsModel} registers itself as a
	 * validationListener on it's children to forward the event.
	 */
	public void validationResultsChanged(ValidationResults results) {
		fireValidationResultsChanged();
	}

	/**
	 * Forwarding of known property events coming from child models. Each event
	 * triggers a specific evaluation of the parent property, which will trigger
	 * events as needed.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == HAS_ERRORS_PROPERTY)
			updateErrors();
		else if (evt.getPropertyName() == HAS_WARNINGS_PROPERTY)
			updateWarnings();
		else if (evt.getPropertyName() == HAS_INFO_PROPERTY)
			updateInfo();
	}
}
