package org.valkyriercp.dialog;

//package org.springframework.richclient.dialog;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.component.DefaultMessageAreaPane;
import org.valkyriercp.component.MessagePane;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.form.FormGuard;
import org.valkyriercp.form.FormModelFactory;
import org.valkyriercp.form.SimpleValidationResultsReporter;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;
import org.valkyriercp.layout.TableLayoutBuilder;
import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.constraint.Constraint;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeListener;
//import org.springframework.binding.form.ValidatingFormModel;
//import org.springframework.rules.closure.Closure;
//import org.springframework.rules.constraint.Constraint;
//import org.springframework.richclient.core.Message;
//import org.springframework.richclient.form.FormGuard;
//import org.springframework.richclient.form.FormModelHelper;
//import org.springframework.richclient.form.SimpleValidationResultsReporter;
//import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
//import org.springframework.richclient.layout.TableLayoutBuilder;

/**
 * Simple input application dialog consisting of a label and a text field for
 * accepting input.
 * 
 * @author Keith Donald
 */
public class InputApplicationDialog extends ApplicationDialog implements
		Messagable {

	private String inputLabelMessage = "dialog.input";

	private JComponent inputField;

	private Constraint inputConstraint;

	private Closure finishAction;

	private MessagePane reporter;

	private ValidatingFormModel formModel;

	public InputApplicationDialog(Object bean, String propertyName) {
		this(bean, propertyName, true);
	}

	public InputApplicationDialog(Object bean, String propertyName,
			boolean bufferChanges) {
		this(new FormModelFactory().createFormModel(bean, bufferChanges),
				propertyName);
		// this(FormModelHelper.createFormModel(bean, bufferChanges),
		// propertyName);
	}

	public InputApplicationDialog(ValidatingFormModel formModel,
			String propertyName) {
		this();
		this.formModel = formModel;
		setInputField(new SwingBindingFactory(formModel).createBinding(
				propertyName).getControl());
	}

	public InputApplicationDialog() {
		this(null, null, CloseAction.DISPOSE);
	}

	public InputApplicationDialog(String title, Window parent) {
		this(title, parent, CloseAction.DISPOSE);
	}

	public InputApplicationDialog(String title, Window parent,
			CloseAction closeAction) {
		super(title, parent, closeAction);
		setResizable(true);
	}

	public void setInputField(JComponent field) {
		Assert.notNull(field);
		this.inputField = field;
	}

	public void setInputLabelMessage(String inputLabel) {
		Assert.hasText(inputLabel, "The input label is required");
		this.inputLabelMessage = inputLabel;
	}

	public void setInputConstraint(Constraint constraint) {
		this.inputConstraint = constraint;
	}

	public void setFinishAction(Closure procedure) {
		this.finishAction = procedure;
	}

	protected MessagePane createMessagePane() {
		return new DefaultMessageAreaPane();
	}

	private MessagePane getMessagePane() {
		if (reporter == null) {
			reporter = createMessagePane();

			if (this.formModel != null) {
				new SimpleValidationResultsReporter(
						formModel.getValidationResults(), reporter);
				FormGuard formGuard = new FormGuard(formModel);
				formGuard.addGuarded(this, FormGuard.FORMERROR_GUARDED);
			}
		}
		return reporter;
	}

	protected JComponent createDialogContentPane() {
		TableLayoutBuilder layoutBuilder = new TableLayoutBuilder();

		if (this.inputField == null) {
			this.inputField = getApplicationConfig().componentFactory()
					.createTextField();
		}
		// work around for bug in JFormattedTextField text field for selectAll
		if (inputField instanceof JFormattedTextField) {
			SelectAllBugFixer selectAllBugFixer = new SelectAllBugFixer();
			inputField.addFocusListener(selectAllBugFixer);
		}

		layoutBuilder.cell(createInputLabel(),
				TableLayoutBuilder.DEFAULT_LABEL_ATTRIBUTES);
		layoutBuilder.labelGapCol();
		layoutBuilder.cell(inputField);

		layoutBuilder.unrelatedGapRow();
		layoutBuilder.cell(getMessagePane().getControl());

		layoutBuilder.relatedGapRow();
		layoutBuilder.separator("");
		return layoutBuilder.getPanel();
	}

	protected JComponent createInputLabel() {
		return getApplicationConfig().componentFactory().createLabelFor(
				inputLabelMessage, getInputField());
	}

	protected boolean onFinish() {
		if (checkInputConstraint()) {
			onFinish(getInputValue());
			return true;
		}
		return false;
	}

	private boolean checkInputConstraint() {
		if (inputConstraint != null)
			return inputConstraint.test(getInputValue());

		return true;
	}

	private Object getInputValue() {
		if (inputField instanceof JFormattedTextField) {
			return ((JFormattedTextField) inputField).getValue();
		} else if (inputField instanceof JTextComponent) {
			return ((JTextComponent) inputField).getText();
		} else {
			throw new IllegalStateException("Input field type not supported");
		}
	}

	protected void onFinish(Object inputValue) {
		if (formModel != null) {
			formModel.commit();
		}
		if (finishAction != null) {
			finishAction.call(inputValue);
		}
	}

	public ValidatingFormModel getFormModel() {
		return formModel;
	}

	/**
	 * @return Returns the inputField.
	 */
	public JComponent getInputField() {
		return inputField;
	}

	private static class SelectAllBugFixer extends FocusAdapter {
		public void focusGained(final FocusEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					((JFormattedTextField) evt.getComponent()).selectAll();
				}
			});
		}
	}

	public void setMessage(Message message) {
		getMessagePane().setMessage(message);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		getMessagePane().addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getMessagePane().addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getMessagePane().removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getMessagePane().removePropertyChangeListener(propertyName, listener);
	}
}