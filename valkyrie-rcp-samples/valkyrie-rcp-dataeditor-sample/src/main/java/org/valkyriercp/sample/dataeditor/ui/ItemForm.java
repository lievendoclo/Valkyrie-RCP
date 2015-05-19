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

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.MigLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.Item;
import org.valkyriercp.widget.TabbedForm;

public class ItemForm extends TabbedForm
{
    protected Tab[] getTabs()
    {
        MigLayoutFormBuilder builder = new MigLayoutFormBuilder(getBindingFactory());
        setFocusControl(builder.addPropertyAndLabel("name", "wrap")[1]);
        builder.addPropertyAndLabel("description", "wrap");
        builder.addPropertyAndLabelWithBinder("supplier", "supplierBinder", "grow");

        // or you could use the following block, which uses the generic lookupBinder
        // and configures it through context parameters
        /*
        Map<String, Object> binderContext = new HashMap<String, Object>();
        binderContext.put(LookupBinder.REQUIRED_SOURCE_CLASS_KEY, Supplier.class);
        binderContext.put(LookupBinder.DATA_EDITOR_ID_KEY, "supplierDataEditor");
        binderContext.put(LookupBinder.OBJECT_LABEL_FUNCTION_KEY, new Function<Supplier, String>() {
            @Override
            public String apply(Supplier supplier) {
                if(supplier == null)
                    return null;
                return supplier.getName();
            }
        });
        binderContext.put(LookupBinder.CREATE_FILTER_FROM_FIELD_FUNCTION_KEY, new Function<String, SupplierFilter>() {
            @Override
            public SupplierFilter apply(String textFieldValue) {
                SupplierFilter s = new SupplierFilter();
                s.setNameContains(textFieldValue);
                return s;
            }
        });
        binderContext.put(LookupBinder.DIALOG_SIZE_KEY, new Dimension(800,600));
        builder.addPropertyAndLabel("supplier", "genericLookupBinder", binderContext);
         */

        return new Tab[] { new Tab("detail", builder.getPanel())};
    }

    @Override
    public FormModel createFormModel() {
        return getApplicationConfig().formModelFactory().createFormModel(new Item(),"itemForm");
    }
}
