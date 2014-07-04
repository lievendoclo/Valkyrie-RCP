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
