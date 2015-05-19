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
package org.valkyriercp.list;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.swing.*;

/**
 * @author Peter De Bruycker
 */
public class ComboBoxAutoCompletionInterceptorFactory implements FormComponentInterceptorFactory {

    public class ComboBoxAutoCompletionInterceptor extends AbstractFormComponentInterceptor {

        /**
         * Constructs a new <code>AutoCompletionInterceptor</code> instance.
         *
         * @param formModel
         *            the formModel
         */
        public ComboBoxAutoCompletionInterceptor(FormModel formModel) {
            super(formModel);
        }

        public void processComponent(String propertyName, JComponent component) {
            JComponent inner = getInnerComponent(component);
            if (inner instanceof JComboBox ) {
                JComboBox comboBox = (JComboBox) inner;
                if( comboBox.isEditable()) {
                    // It's editable, so install autocompletion for editable comboboxes
                    new EditableComboBoxAutoCompletion(comboBox);
                } else {
                    new ComboBoxAutoCompletion(comboBox);
                }
            }
        }
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new ComboBoxAutoCompletionInterceptor(formModel);
    }
}
