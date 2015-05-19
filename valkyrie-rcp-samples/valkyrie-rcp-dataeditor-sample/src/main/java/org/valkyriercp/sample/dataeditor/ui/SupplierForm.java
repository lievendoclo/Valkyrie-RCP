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
package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.widget.TabbedForm;

public class SupplierForm extends TabbedForm
{
    protected Tab[] getTabs()
    {
        FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        setFocusControl(builder.addPropertyAndLabel("name")[1]);
        builder.nextRow();
        builder.addPropertyAndLabel("contactName");

        return new Tab[] { new Tab("detail", builder.getPanel())};
    }

    @Override
    public FormModel createFormModel() {
        return getApplicationConfig().formModelFactory().createFormModel(new Supplier(), "supplierForm");
    }
}
