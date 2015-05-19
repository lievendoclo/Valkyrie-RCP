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

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class CheckBoxBinder extends ToggleButtonBinder {

    public CheckBoxBinder() {
        super();
    }

    protected CheckBoxBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected CheckBoxBinder(Class requiredSourceClass) {
        super(requiredSourceClass);
    }

    protected CheckBoxBinder(String[] supportedContextKeys) {
        super(supportedContextKeys);
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createCheckBox("");
    }

    protected void applyContext(ToggleButtonBinding toggleButtonBinding, FormModel formModel, String formPropertyPath, Map context) {
        super.applyContext(toggleButtonBinding, formModel, formPropertyPath, context);
        toggleButtonBinding.setConfigureFace(false);
    }
}