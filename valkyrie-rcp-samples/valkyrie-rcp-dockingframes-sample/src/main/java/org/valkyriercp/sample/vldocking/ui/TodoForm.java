package org.valkyriercp.sample.vldocking.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.vldocking.domain.TodoItem;
import org.valkyriercp.widget.AbstractFocussableWidgetForm;

import javax.swing.*;

public class TodoForm  extends AbstractFocussableWidgetForm
{
    @Override
    public FormModel createFormModel() {
        return getApplicationConfig().formModelFactory().createFormModel(new TodoItem(), "todoItemForm");
    }

    protected JComponent createFormControl()
    {
        FormLayout layout = new FormLayout("right:pref, 4dlu, default", "default");
        FormLayoutFormBuilder builder = new FormLayoutFormBuilder(getBindingFactory(), layout);

        builder.addPropertyAndLabel("name");
        builder.nextRow();
        builder.addPropertyAndLabel("description");
        builder.nextRow();
        builder.addPropertyAndLabel("todoDate");

        return builder.getPanel();
    }
}
