package org.valkyriercp.binding.value.swing;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

/**
 * @author Keith Donald
 */
public enum ValueCommitPolicy {

	AS_YOU_TYPE(0, "as_you_type") {
		public void configure(JFormattedTextField textField,
				DefaultFormatter formatter) {
			textField.setFocusLostBehavior(JFormattedTextField.PERSIST);
			formatter.setOverwriteMode(false);
			formatter.setAllowsInvalid(true);
			formatter.setCommitsOnValidEdit(true);
		}
	},

	FOCUS_LOST(1, "focus_lost") {
		public void configure(JFormattedTextField textField,
				DefaultFormatter formatter) {
			textField.setFocusLostBehavior(JFormattedTextField.COMMIT);
			formatter.setOverwriteMode(false);
			formatter.setAllowsInvalid(true);
			formatter.setCommitsOnValidEdit(false);
		}
	},

	ON_SUBMIT(2, "on_submit") {
		public void configure(JFormattedTextField textField,
				DefaultFormatter formatter) {
			textField.setFocusLostBehavior(JFormattedTextField.PERSIST);
			formatter.setOverwriteMode(false);
			formatter.setAllowsInvalid(true);
			formatter.setCommitsOnValidEdit(false);
		}
	}

	;
	private int code;
	private String label;

	ValueCommitPolicy(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public abstract void configure(JFormattedTextField textField,
			DefaultFormatter formatter);

}