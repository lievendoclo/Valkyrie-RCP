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
