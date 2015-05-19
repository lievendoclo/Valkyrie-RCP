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

public class FileChooserBinder implements Binder
{
    public static final String USE_FILE_KEY = "useFile";
    private boolean useFile = false;

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        Boolean useFile;
        if(context.containsKey(USE_FILE_KEY))
            useFile = (Boolean) context.get(USE_FILE_KEY);
        else
            useFile = this.useFile;
        return new FileChooserBinding(formModel, formPropertyPath, getPropertyType(formModel, formPropertyPath), useFile);
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        throw new UnsupportedOperationException("This binder creates its own control");
    }

    protected Class<?> getPropertyType(FormModel formModel, String formPropertyPath)
    {
        return formModel.getFieldMetadata(formPropertyPath).getPropertyType();
    }

    public boolean isUseFile() {
        return useFile;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }
}
