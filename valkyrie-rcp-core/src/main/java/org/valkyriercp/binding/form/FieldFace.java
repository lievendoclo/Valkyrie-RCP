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
