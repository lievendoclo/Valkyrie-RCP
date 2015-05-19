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
package org.valkyriercp.sample.vldocking.ui.binder;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.binding.swing.AbstractGlazedListsBinding;
import org.valkyriercp.sample.vldocking.domain.TodoItem;
import org.valkyriercp.sample.vldocking.ui.TodoForm;
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

