package org.valkyriercp.form.builder;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.util.Locale;

/**
 * <code>FormComponentInterceptor</code> that allows customization on how a CheckBox form property is rendered.
 *
 * @see CheckBoxFormComponentInterceptorFactory for more details on configuration
 *
 * @author Peter De Bruycker
 */
public class CheckBoxFormComponentInterceptor extends AbstractFormComponentInterceptor {

	private boolean showLabel = true;
	private boolean showText = false;

	private String textKey = "text";

	private MessageSource messageSource;

	public CheckBoxFormComponentInterceptor(FormModel formModel, MessageSource messageSource, boolean showLabel,
			boolean showText, String textKey) {
		super(formModel);
		this.messageSource = messageSource;
		this.showLabel = showLabel;
		this.showText = showText;
		this.textKey = textKey;
	}

	public void processComponent(String propertyName, JComponent component) {
		if (component instanceof JCheckBox) {
			if (showText) {
				JCheckBox checkBox = (JCheckBox) component;

				checkBox.setText(fetchExtraText(messageSource, propertyName));
			}
		}
	}

	protected String fetchExtraText(MessageSource messageSource, String propertyName) {
		return messageSource.getMessage(new DefaultMessageSourceResolvable(getExtraTextKeys(propertyName), propertyName
				+ "." + textKey), Locale.getDefault());
	}

	/**
	 * Returns the keys used to fetch the extra text from the <code>MessageSource</code>.
	 * <p>
	 * The keys returned are
	 * <code>&lt;formModelId&gt;.&lt;propertyName&gt;.&lt;textKey&gt;, &lt;propertyName&gt;.&lt;textKey&gt;, &lt;textKey&gt;</code>
	 * <p>
	 * Can safely be overridden to add extra keys
	 *
	 * @param propertyName
	 *            the property name
	 * @return the keys
	 */
	protected String[] getExtraTextKeys(String propertyName) {
		return new String[] { getFormModel().getId() + "." + propertyName + "." + textKey,
				propertyName + "." + textKey, textKey };
	}

	protected boolean isCheckBoxProperty(String propertyName) {
		Class type = getFormModel().getFieldMetadata(propertyName).getPropertyType();

		return Boolean.TYPE.equals(type) || Boolean.class.equals(type);
	}

	public void processLabel(String propertyName, JComponent label) {
		if (!showLabel && isCheckBoxProperty(propertyName)) {
			((JLabel) label).setText("");
		}
	}
}

