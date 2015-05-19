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
import java.util.List;
import java.util.Map;

public class CheckBoxListBinder<T> implements Binder {
    public static final String SELECTABLE_VALUES_KEY = "selectableValues";
    public static final String SCROLLPANE_NEEDED_KEY = "scrollPaneNeeded";
    
    private List<T> selectableValues;
    private boolean scrollPaneNeeded;

    public CheckBoxListBinder(List<T> selectableValues) {
        this.selectableValues = selectableValues;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        Boolean scrollPaneNeeded;
        if(context.containsKey(SCROLLPANE_NEEDED_KEY)) {
            scrollPaneNeeded = (Boolean) context.get(SCROLLPANE_NEEDED_KEY);
        } else {
            scrollPaneNeeded = this.scrollPaneNeeded;
        }
        List<T> selectableValues;
        if(context.containsKey(SELECTABLE_VALUES_KEY)) {
            selectableValues = (List<T>) context.get(SELECTABLE_VALUES_KEY);
        } else {
            selectableValues = this.selectableValues;
        }
        CheckBoxListBinding<T> tCheckBoxListBinding = new CheckBoxListBinding<T>(formModel, formPropertyPath, selectableValues);
        
        tCheckBoxListBinding.setScrollPaneNeeded(scrollPaneNeeded);
        return tCheckBoxListBinding;
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
