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
    }

    @PostConstruct
    public void postConstruct() {
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
