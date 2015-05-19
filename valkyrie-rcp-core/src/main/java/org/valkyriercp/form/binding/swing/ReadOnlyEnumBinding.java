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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;

public class ReadOnlyEnumBinding extends CustomBinding
{
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    private JTextField label;

    public ReadOnlyEnumBinding(FormModel formModel, String formPropertyPath)
    {
        this(formModel, formPropertyPath, Enum.class);
    }

    public ReadOnlyEnumBinding(FormModel formModel, String formPropertyPath, Class requiredClass)
    {
        this(new JTextField(), formModel, formPropertyPath, requiredClass);
    }

    public ReadOnlyEnumBinding(JTextField label, FormModel formModel, String formPropertyPath)
    {
        this(label, formModel, formPropertyPath, Enum.class);
    }

    public ReadOnlyEnumBinding(JTextField label, FormModel formModel, String formPropertyPath,
                               Class requiredClass)
    {
        super(formModel, formPropertyPath, requiredClass);
        this.label = label;
        setReadOnly(true);
    }

    @Override
    protected void valueModelChanged(Object newValue)
    {
        label.setText(getEnumString((Enum) newValue));

    }

    private String getEnumString(Enum e)
    {
        if (e != null)
        {
            Class<? extends Enum> valueClass = e.getClass();
            return messageSourceAccessor.getMessage(valueClass.getName() + "." + e.name());
        }
        return "";
    }

    @Override
    protected JComponent doBindControl()
    {
        return label;
    }

    @Override
    protected void enabledChanged()
    {
        label.setEnabled(isEnabled());

    }

    @Override
    protected void readOnlyChanged()
    {
        label.setEditable(!isReadOnly());
    }

    @Override
    public void setReadOnly(boolean readOnly)
    {
        // always readonly
        super.setReadOnly(true);
    }

}