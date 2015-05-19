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
package org.valkyriercp.form.binding.jodatime;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.LocalDate;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

public class JodaLocalDateBinder extends AbstractBinder
{
    public static final String READ_ONLY_KEY = "readOnly";

    private boolean readOnly = false;

    public JodaLocalDateBinder()
    {
        super(LocalDate.class);
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    @SuppressWarnings("unchecked")
    protected JComponent createControl(Map context)
    {
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setToolTipText(getApplicationConfig().messageResolver().getMessage("datePicker", "tooltip", "text"));
        return datePicker;
    }

    @SuppressWarnings("unchecked")
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        Boolean readOnly;
        if(context.containsKey(READ_ONLY_KEY)) {
             readOnly = (Boolean) context.get(READ_ONLY_KEY);
        } else {
            readOnly = this.readOnly;
        }
  
        return new JodaLocalDateBinding(formModel, formPropertyPath, ((JXDatePicker) control), readOnly);
    }
}
