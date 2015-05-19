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

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.format.support.AbstractFormatterFactory;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class FormattedTextFieldBinder extends AbstractBinder {

    public static final String FORMATTER_FACTORY_KEY = "formatterFactory";

    public FormattedTextFieldBinder(Class requiredSourceClass) {
        super(requiredSourceClass, new String[] {FORMATTER_FACTORY_KEY});
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JFormattedTextField, "Control must be an instance of JFormattedTextField.");
        return new FormattedTextFieldBinding((JFormattedTextField)control, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createFormattedTextField(
                (AbstractFormatterFactory)context.get(FORMATTER_FACTORY_KEY));
    }
}
