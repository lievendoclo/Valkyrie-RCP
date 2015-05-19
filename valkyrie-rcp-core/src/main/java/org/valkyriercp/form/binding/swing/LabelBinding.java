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
package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;

/**
 * @author Mauro Ransolin
 */
public class LabelBinding extends CustomBinding {

    private final JLabel label;

    public LabelBinding(JLabel label, FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, String.class);
        this.label = label;
    }

    protected JComponent doBindControl() {
        label.setText((String)getValueModel().getValue());
        return label;
    }

    protected void readOnlyChanged() {
        label.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void enabledChanged() {
        label.setEnabled(isEnabled() && !isReadOnly());
    }

    protected void valueModelChanged(Object newValue) {
        label.setText((String)newValue);
    }
}