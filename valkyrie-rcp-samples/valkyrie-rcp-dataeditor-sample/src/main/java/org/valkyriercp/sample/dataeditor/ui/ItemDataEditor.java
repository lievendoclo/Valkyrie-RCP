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


import org.valkyriercp.sample.dataeditor.domain.Item;
import org.valkyriercp.widget.editor.DefaultDataEditorWidget;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;

import javax.annotation.PostConstruct;

public class ItemDataEditor extends DefaultDataEditorWidget
{
    private ItemDataProvider itemDataProvider;

    public ItemDataEditor(ItemDataProvider itemDataProvider)
    {
        super("itemDataEditor");
        this.itemDataProvider = itemDataProvider;

        init();
    }

    public void init() {
        setDataProvider(itemDataProvider);
        setDetailForm(new ItemForm());
        setFilterForm(new ItemFilterForm());

        PropertyColumnTableDescription tableDescription = new PropertyColumnTableDescription("itemDataEditor", Item.class);
        tableDescription.addPropertyColumn("name");
        tableDescription.addPropertyColumn("description");
        tableDescription.addPropertyColumn("supplier.name");
        setTableWidget(tableDescription);
    }


}
