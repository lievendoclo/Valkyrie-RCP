package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.TristateCheckBox;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TristateCheckBoxBinding extends CustomBinding {
    private TristateCheckBox tristateCheckBox;

    public TristateCheckBoxBinding(FormModel formModel, String formPropertyPath) {
        this(formModel, formPropertyPath, new TristateCheckBox());
    }

    public TristateCheckBoxBinding(FormModel formModel, String formPropertyPath, TristateCheckBox tristateCheckBox) {
        super(formModel, formPropertyPath, Boolean.class);
        this.tristateCheckBox = tristateCheckBox;
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        Boolean value = (Boolean) newValue;
        if(value == null) {
            tristateCheckBox.setState(TristateCheckBox.STATE_MIXED);
        } else if(value) {
            tristateCheckBox.setState(TristateCheckBox.STATE_SELECTED);
        } else {
            tristateCheckBox.setState(TristateCheckBox.STATE_UNSELECTED);
        }
    }

    @Override
    protected JComponent doBindControl() {
        tristateCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(tristateCheckBox.getState()) {
                    case TristateCheckBox.STATE_MIXED:
                        controlValueChanged(null);
                        break;
                    case TristateCheckBox.STATE_SELECTED:
                        controlValueChanged(Boolean.TRUE);
                        break;
                    case TristateCheckBox.STATE_UNSELECTED:
                        controlValueChanged(Boolean.FALSE);
                        break;
                }
            }
        });
        return tristateCheckBox;
    }

    @Override
    protected void readOnlyChanged() {
        tristateCheckBox.setEnabled(!isReadOnly());
    }

    @Override
    protected void enabledChanged() {
        tristateCheckBox.setEnabled(!isReadOnly());
    }
}
