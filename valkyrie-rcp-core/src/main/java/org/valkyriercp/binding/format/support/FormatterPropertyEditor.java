package org.valkyriercp.binding.format.support;

import org.valkyriercp.binding.format.Formatter;

import java.beans.PropertyEditorSupport;

/**
 * Adapts a formatter to the property editor contract.
 *
 * @author Keith Donald
 */
public class FormatterPropertyEditor extends PropertyEditorSupport {

	/**
	 * The wrapped formatter.
	 */
	private Formatter formatter;

	/**
	 * The target value class (may be null).
	 */
	private Class targetClass;

	/**
	 * Creates a formatter property editor.
	 * @param formatter the formatter to adapt
	 */
	public FormatterPropertyEditor(Formatter formatter) {
		this.formatter = formatter;
	}

	/**
	 * Creates a formatter property editor.
	 * @param formatter the formatter to adapt
	 * @param targetClass the target class for "setAsText" conversions
	 */
	public FormatterPropertyEditor(Formatter formatter, Class targetClass) {
		this.formatter = formatter;
		this.targetClass = targetClass;
	}

	public String getAsText() {
		return formatter.formatValue(getValue());
	}

	public void setAsText(String text) throws IllegalArgumentException {
		setValue(formatter.parseValue(text, targetClass));
	}
}