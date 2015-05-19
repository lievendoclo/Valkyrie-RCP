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
package org.valkyriercp.form.binding.jodatime;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JodaLocalDateBinding extends CustomBinding implements PropertyChangeListener {
    private final JXDatePicker datePicker;
    private final boolean readOnly;

    private boolean isSettingText = false;

    public JodaLocalDateBinding(FormModel model, String path, JXDatePicker datePicker, boolean readOnly) {
        super(model, path, LocalDate.class);
        this.datePicker = datePicker;
        this.readOnly = readOnly;
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        isSettingText = true;
        setDatePickerValue((LocalDate) newValue);
        readOnlyChanged();
        isSettingText = false;
    }

    private void setDatePickerValue(LocalDate dateTime) {
        if (dateTime == null) {
            datePicker.setDate(null);
        } else {
            datePicker.setDate(dateTime.toDateTimeAtStartOfDay().toGregorianCalendar().getTime());
        }
    }

    @Override
    protected JComponent doBindControl() {
        setDatePickerValue((LocalDate) getValue());
        datePicker.getEditor().addPropertyChangeListener("value", this);
        return datePicker;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (!isSettingText && !isReadOnly())
            if (datePicker.getDate() == null)
                controlValueChanged(null);
            else
                controlValueChanged(new DateTime(datePicker.getDate()).toLocalDate());
    }

    @Override
    protected void readOnlyChanged() {
        datePicker.setEditable(isEnabled() && !this.readOnly && !isReadOnly());
    }

    @Override
    protected void enabledChanged() {
        datePicker.setEnabled(isEnabled());
        readOnlyChanged();
    }
}
