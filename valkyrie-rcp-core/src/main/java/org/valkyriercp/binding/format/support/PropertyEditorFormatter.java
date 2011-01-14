package org.valkyriercp.binding.format.support;

import org.springframework.util.Assert;

import java.beans.PropertyEditor;

/**
 * Adapts a property editor to the formatter interface.
 *
 * @author Keith Donald
 */
public class PropertyEditorFormatter extends AbstractFormatter {

	private PropertyEditor propertyEditor;

	/**
	 * Wrap given property editor in a formatter.
	 */
	public PropertyEditorFormatter(PropertyEditor propertyEditor) {
		Assert.notNull(propertyEditor, "Property editor is required");
		this.propertyEditor = propertyEditor;
	}

	/**
	 * Returns the wrapped property editor.
	 */
	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	protected String doFormatValue(Object value) {
		propertyEditor.setValue(value);
		return propertyEditor.getAsText();
	}

	protected Object doParseValue(String formattedValue, Class targetClass) {
		propertyEditor.setAsText(formattedValue);
		return propertyEditor.getValue();
	}
}