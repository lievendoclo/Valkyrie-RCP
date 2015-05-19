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
package org.valkyriercp.widget.table;

import ca.odell.glazedlists.swing.EventTableModel;
import org.valkyriercp.binding.form.FieldMetadata;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.form.AbstractForm;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.EventObject;

/**
 * {@link javax.swing.table.TableCellEditor} that uses a backing {@link FormModel} to determine the editing capabilities and
 * committing of the value.
 *
 * <p>
 * NOTE: the CellEditor will get the first event, only afterwards the row selection changes. This has the
 * effect that editing a cell will switch values upon entering. Additionally the cell should always be marked
 * editable and the binding that acts as the editor should disable itself if needed. If we were to rely on
 * {@link FieldMetadata#isReadOnly()}, the previous value would determine if the cell is editable because
 * the underlying form object isn't changed yet.
 * </p>
 *
 * @author Jan Hoskens
 *
 */
public class ValueModelTableCellEditor extends AbstractCellEditor implements TableCellEditor
{

    private final FormModel formModel;

    private final ActionCommand commitCommand;

    private final FieldMetadata fieldMetaData;

    private final ValueModel valueModel;

    private final JComponent editor;

    /**
     * Creates a TableCellEditor. The property will be used to retrieve the {@link ValueModel} and
     * {@link FieldMetadata}. The first is used while getting the initial value, the latter is used to
     * determine if the cell is editable. Note that the given editor should be bound already.
     *
     * @param formModel
     *            model to use when committing/reverting.
     * @param propertyName
     *            name of the property.
     * @param editor
     *            JComponent bound to that property used in the editable state.
     */
    public ValueModelTableCellEditor(FormModel formModel, String propertyName, JComponent editor)
    {
        this(formModel, formModel.getFieldMetadata(propertyName), formModel.getValueModel(propertyName),
                editor);
    }

    public ValueModelTableCellEditor(AbstractForm form, String propertyName)
    {
    	this(form.getFormModel(), propertyName, form.getBindingFactory().createBinding(propertyName).getControl());
    }

    public ValueModelTableCellEditor(FormModel formModel, String propertyName, JComponent editor, ActionCommand commitCommand)
    {
        this(formModel, formModel.getFieldMetadata(propertyName), formModel.getValueModel(propertyName),
                editor, commitCommand);
    }

    public ValueModelTableCellEditor(FormModel formModel, FieldMetadata fieldMetadata, ValueModel valueModel,
            JComponent editor)
    {
        this(formModel, fieldMetadata, valueModel, editor, null);
    }

    /**
     * Creates a TableCellEditor. The {@link ValueModel} is used while getting the initial value, the
     * {@link FieldMetadata} is used to determine if the cell is editable. Note that the given editor should
     * be bound already.
     *
     * @param formModel
     *            model to use when committing/reverting.
     * @param valueModel
     *            valueModel to retrieve the initial value.
     * @param fieldMetadata
     *            metaData to determine if the cell is editable.
     * @param editor
     *            JComponent bound to that property used in the editable state.
     */
    public ValueModelTableCellEditor(FormModel formModel, FieldMetadata fieldMetadata, ValueModel valueModel,
            JComponent editor, ActionCommand commitCommand)
    {
        this.formModel = formModel;
        this.valueModel = valueModel;
        this.fieldMetaData = fieldMetadata;
        this.editor = editor;
        this.commitCommand = commitCommand;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
            int column)
    {
        TableModel tableModel = table.getModel();
        if (tableModel instanceof EventTableModel<?>)
        {
            formModel.setFormObject(((EventTableModel<?>)tableModel).getElementAt(row));
        }
        if (editor instanceof JTextComponent)
            ((JTextComponent)editor).selectAll();
        return editor;
    }

    @Override
    public void cancelCellEditing()
    {
        formModel.revert();
        super.cancelCellEditing();
    }

    public Object getCellEditorValue()
    {
        return valueModel.getValue();
    }

    /**
     * We cannot rely on the {@link FieldMetadata} because the form object is replaced AFTER this method
     * is queried. We would be returning the previous value instead of the current one.
     * Instead rely on the binding's component to set the editor read-only.
     */
    @Override
    public boolean isCellEditable(EventObject anEvent)
    {
        return true;
    }

    @Override
    public boolean stopCellEditing()
    {
        if (!formModel.isDirty())
            return super.stopCellEditing();

        if (formModel.isCommittable())
        {
            // if you've specified a commitCommand, all handling is left to you, otherwise just commit the form
            if (commitCommand != null)
                commitCommand.execute();
            else
                formModel.commit();

            return super.stopCellEditing();
        }

        return false;
    }

}
