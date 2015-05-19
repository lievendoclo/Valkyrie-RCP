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
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

public class TristateCheckBoxBinder implements Binder {
    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        return new TristateCheckBoxBinding(formModel, formPropertyPath);
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        if(control instanceof TristateCheckBox) {
            return new TristateCheckBoxBinding(formModel, formPropertyPath, (TristateCheckBox) control);
        } else {
            throw new IllegalArgumentException("component should be a TristateCheckBox");
        }
    }
}
