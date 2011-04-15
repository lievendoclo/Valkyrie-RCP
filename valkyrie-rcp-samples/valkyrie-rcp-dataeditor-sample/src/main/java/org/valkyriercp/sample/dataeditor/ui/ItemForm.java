package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.dataeditor.domain.Item;
import org.valkyriercp.widget.TabbedForm;

public class ItemForm extends TabbedForm
{
    protected Tab[] getTabs()
    {
        FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow, 3dlu, 100dlu", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        setFocusControl(builder.addPropertyAndLabel("name")[1]);
        builder.nextRow();
        builder.addPropertyAndLabel("description");
        builder.nextRow();
        builder.addPropertyAndLabel("supplier", "supplierBinder");

        return new Tab[] { new Tab("detail", builder.getPanel())};
    }

    @Override
    public FormModel createFormModel() {
        return formModelFactory.createFormModel(new Item(),"itemForm");
    }
}
