package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.form.FilterForm;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.sample.dataeditor.domain.SupplierFilter;

import javax.swing.*;

public class SupplierFilterForm extends FilterForm
{
    public SupplierFilterForm()
    {
        super("supplierFilterForm");
    }

    @Override
    protected Object newFormObject()
    {
        return new SupplierFilter();
    }

    @Override
    public void setFormObject(Object formObject)
    {
        if(formObject instanceof Supplier)
        {
            super.setFormObject(SupplierFilter.fromSupplier((Supplier) formObject));
        }
        else
        {
            super.setFormObject(formObject);
        }
    }

    protected JComponent createFormControl()
    {
        FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        builder.addPropertyAndLabel("nameContains");
        builder.nextRow();
        builder.addPropertyAndLabel("contactNameContains");
        return builder.getPanel();
    }
}
