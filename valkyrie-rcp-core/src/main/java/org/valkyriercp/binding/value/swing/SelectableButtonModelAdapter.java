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
package org.valkyriercp.binding.value.swing;

import org.valkyriercp.binding.value.ValueModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Adapts a value model to a ButtonModel
 *
 * @author Oliver Hutchison
 */
public class SelectableButtonModelAdapter extends DefaultButtonModel implements PropertyChangeListener {
    private final ValueModel valueModel;

    public SelectableButtonModelAdapter(ValueModel valueModel) {
        this.valueModel = valueModel;
        this.valueModel.addValueChangeListener(this);
        propertyChange(null);
    }

    public void propertyChange(PropertyChangeEvent e) {
        Boolean selected = (Boolean)valueModel.getValue();
        setSelected(selected == null ? false : selected.booleanValue());
    }

    public void setPressed(boolean pressed) {
        if ((isPressed() == pressed) || !isEnabled()) {
            return;
        } else if (! pressed && isArmed()) {
            setSelected(!this.isSelected());
        }
        super.setPressed(pressed);
    }

    public void setSelected(boolean selected) {
        if (isSelected() == selected) {
            return;
        }
        super.setSelected(selected);
        valueModel.setValue(Boolean.valueOf(isSelected()));
    }
}
