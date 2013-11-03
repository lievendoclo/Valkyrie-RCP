package org.valkyriercp.sample.dataeditor.ui;


import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.widget.editor.DefaultDataEditorWidget;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;

import javax.annotation.PostConstruct;

public class SupplierDataEditor extends DefaultDataEditorWidget
{
    private SupplierDataProvider supplierDataProvider;

    public SupplierDataEditor(SupplierDataProvider supplierDataProvider)
    {
        super("supplierDataEditor");
        this.supplierDataProvider = supplierDataProvider;

        init();
    }

    public void init() {
        setDataProvider(supplierDataProvider);
        setDetailForm(new SupplierForm());
        setFilterForm(new SupplierFilterForm());

        PropertyColumnTableDescription tableDescription = new PropertyColumnTableDescription("supplierDataEditor", Supplier.class);
        tableDescription.addPropertyColumn("name");
        tableDescription.addPropertyColumn("contactName");
        setTableWidget(tableDescription);
    }
}
