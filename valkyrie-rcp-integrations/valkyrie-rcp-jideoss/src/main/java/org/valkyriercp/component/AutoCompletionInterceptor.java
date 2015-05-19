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
package org.valkyriercp.component;

import com.jidesoft.swing.AutoCompletion;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;

public class AutoCompletionInterceptor extends AbstractFormComponentInterceptor {
    private AutoCompletionProvider autoCompletionProvider;

    public AutoCompletionInterceptor(FormModel formModel, AutoCompletionProvider autoCompletionProvider) {
        super(formModel);
        this.autoCompletionProvider = autoCompletionProvider;
    }

    @Override
    public void processComponent(String propertyName, JComponent component) {
        if(component instanceof JTextComponent) {
            JTextComponent comp = (JTextComponent) component;
            new AutoCompletion(comp, getAutoCompletionList(propertyName));
        }

        super.processComponent(propertyName, component);
    }

    private List getAutoCompletionList(String propertyName) {
        return autoCompletionProvider.getAutoCompletionOptions(getFormModel().getId(), propertyName);
    }
}
