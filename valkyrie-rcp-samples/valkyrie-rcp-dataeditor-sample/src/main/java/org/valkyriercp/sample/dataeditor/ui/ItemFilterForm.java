package org.valkyriercp.sample.dataeditor.ui;

import com.jgoodies.forms.layout.FormLayout;
import net.miginfocom.swing.MigLayout;
import org.valkyriercp.form.FilterForm;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
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