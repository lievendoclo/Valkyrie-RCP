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

import org.springframework.util.Assert;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelAdapter;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusLostTextComponentAdapter extends AbstractValueModelAdapter implements FocusListener {
    private final JTextComponent control;

    public FocusLostTextComponentAdapter(JTextComponent component, ValueModel valueModel) {
        super(valueModel);
        Assert.notNull(component);
        this.control = component;
        this.control.addFocusListener(this);
        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object value) {
        control.setText((String)value);
    }

    public void focusLost(FocusEvent e) {
        adaptedValueChanged(control.getText());
    }

    public void focusGained(FocusEvent e) {
    }
}
