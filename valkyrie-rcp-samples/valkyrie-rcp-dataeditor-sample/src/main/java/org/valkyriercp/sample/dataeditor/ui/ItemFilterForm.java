package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.form.FilterForm;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
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
        FormLayout layout = new FormLayout("default, 3dlu, fill:pref:nogrow", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        builder.addHorizontalSeparator("Quick search", 3);
        builder.nextRow();
        builder.addPropertyAndLabel("quickSearch");
        builder.nextRow();
        builder.addHorizontalSeparator("Item search fields", 3);
        builder.nextRow();
        builder.addPropertyAndLabel("nameContains");
        return builder.getPanel();
    }
}