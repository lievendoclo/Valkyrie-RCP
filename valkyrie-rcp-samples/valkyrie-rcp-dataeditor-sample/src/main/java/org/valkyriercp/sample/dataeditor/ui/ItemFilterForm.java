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

import net.miginfocom.swing.MigLayout;
import org.valkyriercp.form.FilterForm;
import org.valkyriercp.form.builder.MigLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.ItemFilter;

import javax.swing.*;

public class ItemFilterForm extends FilterForm
{
    public ItemFilterForm()
    {
        super("itemFilterForm");
    }

    @Override
    protected Object newFormObject()
    {
        return new ItemFilter();
    }

    protected JComponent createFormControl()
    {
        MigLayoutFormBuilder builder = new MigLayoutFormBuilder(getBindingFactory(), new MigLayout("wrap 2"));
        builder.addTitledHorizontalSeparator("Quick search", "span 2, grow");
        builder.addPropertyAndLabel("quickSearch");
        builder.addTitledHorizontalSeparator("Item search fields", "span 2, grow");
        builder.addPropertyAndLabel("nameContains");
        return builder.getPanel();
    }
}