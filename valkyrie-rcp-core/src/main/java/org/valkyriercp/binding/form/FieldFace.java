package org.valkyriercp.binding.form;

import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.VisualizedElement;
import org.valkyriercp.core.support.LabelInfo;

import javax.swing.*;

/**
 * Provides metadata related to the visualization of a form property and
 * convenience methods for configuring GUI components using the metadata.
 *
 * @author Oliver Hutchison
 */
public interface FieldFace extends DescribedElement, VisualizedElement {

	/**
	 * The name of the property in human readable form, typically used for
	 * validation messages.
	 */
	String getDisplayName();

	/**
	 * A short caption describing the property, typically used for tool tips.
	 */
	String getCaption();

	/**
	 * A longer caption describing the property.
	 */
	String getDescription();

	/**
	 * The text, mnemonic and mnemonicIndex for any labels created for the
	 * property.
	 */
	LabelInfo getLabelInfo();

	/**
	 * The icon that is used for any labels created for this property.
	 */
	Icon getIcon();

	/**
	 * Configures the supplied JLabel using LabelInfo and Icon.
	 */
	void configure(JLabel label);

	/**
	 * Configures the supplied button using LabelInfo and Icon.
	 */
	void configure(AbstractButton button);
}
