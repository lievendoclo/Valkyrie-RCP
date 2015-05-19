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

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

/**
 * Binder for a list of Enum values. Shows all possible enum values in a checkbox list and binds on the selected ones.
 */
public class CheckBoxListEnumBinder implements Binder {
    private Class<? extends Enum> enumClass;

    private boolean scrollPaneNeeded = true;

    public static final String SCROLLPANE_NEEDED_KEY = "scrollPaneNeeded";
    public static final String ENUM_CLASS_KEY = "enumClass";

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        Class<? extends Enum> enumClass;
        if(context.containsKey(ENUM_CLASS_KEY)) {
            enumClass = (Class<? extends Enum>) context.get(ENUM_CLASS_KEY);
        } else {
            enumClass = this.enumClass;
        }
        Boolean scrollPaneNeeded;
        if(context.containsKey(SCROLLPANE_NEEDED_KEY)) {
            scrollPaneNeeded = (Boolean) context.get(SCROLLPANE_NEEDED_KEY);
        } else {
            scrollPaneNeeded = this.scrollPaneNeeded;
        }
        CheckBoxListEnumBinding binding = new CheckBoxListEnumBinding(formModel, formPropertyPath, enumClass);
        binding.setScrollPaneNeeded(scrollPaneNeeded);
        return binding;
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        throw new UnsupportedOperationException("This binder creates its own control");
    }

    public boolean isScrollPaneNeeded() {
        return scrollPaneNeeded;
    }

    public void setScrollPaneNeeded(boolean scrollPaneNeeded) {
        this.scrollPaneNeeded = scrollPaneNeeded;
    }
}