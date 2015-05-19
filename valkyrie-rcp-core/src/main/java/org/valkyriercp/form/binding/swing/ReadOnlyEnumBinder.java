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
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

public class ReadOnlyEnumBinder implements Binder
{

    private Class requiredClass;

    public Class getRequiredClass()
    {
        return requiredClass;
    }

    public void setRequiredClass(Class requiredClass)
    {
        this.requiredClass = requiredClass;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context)
    {
        if (requiredClass == null)
        {
            return new ReadOnlyEnumBinding(formModel, formPropertyPath);
        }
        else
        {
            return new ReadOnlyEnumBinding(formModel, formPropertyPath, requiredClass);
        }
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        if (control instanceof JTextField)
        {
            return new ReadOnlyEnumBinding((JTextField) control, formModel, formPropertyPath);
        }
        else
        {
            throw new IllegalArgumentException("control must be a JTextField");
        }
    }

}