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

import com.google.common.base.Function;
import org.valkyriercp.form.binding.swing.editor.LookupBinder;
import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.sample.dataeditor.domain.SupplierFilter;

import java.awt.*;

public class SupplierBinder extends LookupBinder<Supplier>
{
    public SupplierBinder()
    {
        setDataEditorId("supplierDataEditor");
        setRequiredSourceClass(Supplier.class);
        setObjectLabelFunction(new Function<Supplier, String>() {
            @Override
            public String apply( Supplier o) {
                return o.getName();
            }
        });
        setCreateFilterFromFieldFunction(new Function<String, SupplierFilter>() {
            @Override
            public SupplierFilter apply(String textFieldValue) {
                SupplierFilter s = new SupplierFilter();
                s.setNameContains(textFieldValue);
                return s;
            }
        });
        setDialogSize(new Dimension(800,600));
    }
}
