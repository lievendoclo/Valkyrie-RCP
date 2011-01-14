package org.valkyriercp.sample.simple.ui.binder;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.binding.swing.AbstractGlazedListsBinding;
import org.valkyriercp.sample.simple.domain.TodoItem;
import org.valkyriercp.sample.simple.ui.TodoForm;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;
import org.valkyriercp.widget.table.TableDescription;

public class TodoItemListBinding extends AbstractGlazedListsBinding
{
    public TodoItemListBinding(FormModel formModel, String formPropertyPath)
    {
        super(formModel, formPropertyPath);
        setDialogId("todoItemListBindingDialog");
        setAddSupported(true);
        setEditSupported(true);
        setRemoveSupported(true);
        setShowDetailSupported(true);
    }

    protected TableDescription getTableDescription()
    {
        PropertyColumnTableDescription desc = new PropertyColumnTableDescription("todoListBinding", TodoItem.class);
        desc.addPropertyColumn("name");
        desc.addPropertyColumn("description");
        return desc;
    }

    // detail form behavior

    @Override
    protected Object getNewFormObject()
    {
        return new TodoItem();
    }

    @Override
    protected AbstractForm createAddEditForm()
    {
        return new TodoForm();
    }

    @Override
    protected AbstractForm createDetailForm()
    {
        AbstractForm f = new TodoForm();
        f.getFormModel().setReadOnly(true);
        return f;
    }
}

