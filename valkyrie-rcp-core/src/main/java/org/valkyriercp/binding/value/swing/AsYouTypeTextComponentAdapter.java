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
import org.springframework.util.StringUtils;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelAdapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class AsYouTypeTextComponentAdapter extends AbstractValueModelAdapter implements DocumentListener
{

    private final JTextComponent control;

    private boolean convertEmptyStringToNull;

    private boolean settingText;

    public AsYouTypeTextComponentAdapter(JTextComponent control, ValueModel valueModel)
    {
        this(control, valueModel, false);
    }

    public AsYouTypeTextComponentAdapter(JTextComponent control, ValueModel valueModel, boolean convertEmptyStringToNull)
    {
        super(valueModel);
        Assert.notNull(control, "control should not be null");
        this.control = control;
        this.control.getDocument().addDocumentListener(this);
        this.convertEmptyStringToNull = convertEmptyStringToNull;
        initalizeAdaptedValue();
    }

    public void removeUpdate(DocumentEvent e)
    {
        controlTextValueChanged();
    }

    public void insertUpdate(DocumentEvent e)
    {
        controlTextValueChanged();
    }

    public void changedUpdate(DocumentEvent e)
    {
        controlTextValueChanged();
    }

    private void controlTextValueChanged()
    {
        if (!settingText)
        {
            if (!StringUtils.hasText(control.getText()) && convertEmptyStringToNull)
                adaptedValueChanged(null);
            else
                adaptedValueChanged(control.getText());
        }
    }

    protected void valueModelValueChanged(Object value)
    {
        // this try block will coalesce the 2 DocumentEvents that
        // JTextComponent.setText() fires into 1 call to
        // componentValueChanged()
        try
        {
            settingText = true;
            control.setText((String) value);
        }
        finally
        {
            settingText = false;
        }
    }
}

