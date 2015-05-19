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
package org.valkyriercp.form.binding.swing.date;

import org.jdesktop.swingx.JXDatePicker;
import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Binds a <cod>Date</code> to a <code>JXDatePicker</code>
 *
 * @author Peter De Bruycker
 */
public class JXDatePickerDateFieldBinding extends AbstractDateFieldBinding {

	private JXDatePicker datePicker;

	public JXDatePickerDateFieldBinding(JXDatePicker datePicker, FormModel formModel, String formPropertyPath) {
		super(formModel, formPropertyPath);
		this.datePicker = datePicker;
	}

	protected void valueModelChanged(Object newValue) {
		datePicker.setDate((Date) newValue);
	}

	protected JComponent doBindControl() {
		datePicker.setDate((Date) getValue());

		if (getDateFormat() != null) {
			datePicker.setFormats(new String[] {getDateFormat()});
		}

		datePicker.addPropertyChangeListener("date", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				controlValueChanged(datePicker.getDate());
			}
		});

		return datePicker;
	}

	protected void enabledChanged() {
		datePicker.setEnabled(isEnabled());
	}

	protected void readOnlyChanged() {
		datePicker.setEditable(!isReadOnly());
	}

}
