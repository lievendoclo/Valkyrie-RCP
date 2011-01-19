package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.form.FormModelHelper;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.widget.TabbedForm;

public class SupplierForm extends TabbedForm
{
    public SupplierForm()
    {
        super(FormModelHelper.createFormModel(new Supplier(), "supplierForm"));
    }

    protected Tab[] getTabs()
    {
        FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        setFocusControl(builder.addPropertyAndLabel("name")[1]);
        builder.nextRow();
        builder.addPropertyAndLabel("contactName");

        return new Tab[] { new Tab("detail", builder.getPanel())};
    }
}
