package org.valkyriercp.form.binding.swing;

import com.google.common.collect.Lists;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.component.ComponentUtils;
import org.valkyriercp.component.Focussable;
import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.dialog.TitledApplicationDialog;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.binding.support.AbstractCRUDBinding;
import org.valkyriercp.util.CloneUtils;
import org.valkyriercp.widget.AbstractFocussableWidgetForm;
import org.valkyriercp.widget.table.TableDescription;
import org.valkyriercp.widget.table.TableWidget;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractGlazedListsBinding extends AbstractCRUDBinding
{

    /**
     * List on screen, has to be a cloned one to be able to detect differences.
     */
    protected Object viewControllerObject;

    /**
     * Add mode: form is used to add an object.
     */
    private static final int ADD_MODE = 1;

    /**
     * Edit mode: form is used to display and edit an object.
     */
    private static final int EDIT_MODE = 2;

    /**
     * Edit mode: form is used to display and edit an object.
     */
    private static final int DETAIL_MODE = 3;

    /**
     * Current mode of the form/dialog.
     */
    protected int currentMode = ADD_MODE;

    /**
     * Table used to show the list.
     */
    protected GlazedListTableWidget table;

    /**
     * Dialog used to add/create rowObjects.
     */
    protected ApplicationDialog formDialog;

    /**
     * Form used to add/create rowObjects.
     */
    protected AbstractForm form;

    /**
     * Id to configure the dialog.
     */
    protected String dialogId;

    /**
     * Use the original sort order of the provided data
     */
    protected boolean useOriginalSortOrder;

    /**
     * Form used to show rowObjects.
     */
    private AbstractForm detailForm;

    /**
     * Dialog used to show rowObjects.
     */
    private ApplicationDialog detailFormDialog;

    /**
     * Constructor.
     *
     * @param formModel           formModel met de property.
     * @param formPropertyPath    pad naar de property.
     */
    public AbstractGlazedListsBinding(FormModel formModel, String formPropertyPath)
    {
        this(formModel, formPropertyPath, false);
    }

    /**
     * Constructor.
     *
     * @param formModel            formModel met de property.
     * @param formPropertyPath     pad naar de property.
     * @param useOriginalSortOrder gebruik de originele sorteervolgorde van de lijst
     */
    public AbstractGlazedListsBinding(FormModel formModel, String formPropertyPath,
                                      boolean useOriginalSortOrder)
    {
        super(formModel, formPropertyPath, null);
        this.useOriginalSortOrder = useOriginalSortOrder;
    }

    @Override
    protected final void valueModelChanged(Object newValue)
    {
        this.viewControllerObject = CloneUtils.getClone(newValue);
        this.table.setRows(getList(this.viewControllerObject));
        onValueModelChanged();
        readOnlyChanged();
    }

    protected void onValueModelChanged()
    {
    }

    protected final void listChanged()
    {
        controlValueChanged(CloneUtils.getClone(this.viewControllerObject));
        this.table.setRows(getList(this.viewControllerObject));
    }

    @Override
    protected JComponent doBindControl()
    {
        getTable();
        JPanel editor = new JPanel(new BorderLayout())
        {

            private static final long serialVersionUID = 853322345395517384L;

            @Override
            public void setEnabled(boolean enable)
            {
                table.getTable().setEnabled(enable);
                for (AbstractCommand command : getCommands())
                {
                    command.setEnabled(false);
                }
            }
        };
        JTable tableComponent = table.getTable();
        if (isEditSupported() && getEditCommand().isAuthorized())
        {
            tableComponent.addMouseListener(new MouseAdapter()
            {

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() > 1 && SwingUtilities.isLeftMouseButton(e))
                    {
                        edit(table.getSelectedRows());
                    }
                }
            });
        }
        else if (isShowDetailSupported() && getDetailCommand().isAuthorized())
        {
            tableComponent.addMouseListener(new MouseAdapter()
            {

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() > 1 && SwingUtilities.isLeftMouseButton(e))
                    {
                        showDetail(table.getSelectedRows());
                    }
                }
            });
        }
        editor.add(table.getComponent(), BorderLayout.CENTER);
        java.util.List<AbstractCommand> commands = getCommands();
        if ((commands != null) && (commands.size() > 0))
        {
            JPanel buttons = ComponentUtils.createIconButtonPanel(commands);
            editor.add(buttons, BorderLayout.SOUTH);
        }
        valueModelChanged(getValue());
        return editor;
    }

    protected TableWidget getTable()
    {
        if (table == null)
        {
            if (useOriginalSortOrder) {
                this.table = new GlazedListTableWidget(null, getTableDescription(), null, isFilteringSupported());
            }
            else
                this.table = new GlazedListTableWidget(null, getTableDescription(), isFilteringSupported());
        }
        return this.table;
    }

    public Object[] getSelection()
    {
        return this.table.getSelectedRows();
    }

    /**
     * Enable/disable the commands on readOnly change or valueModelChange
     */
    @Override
    protected void readOnlyChanged()
    {
        for (AbstractCommand abstractCommand : getCommands())
        {
            if (isShowDetailSupported() && abstractCommand == getDetailCommand())
                (abstractCommand).setEnabled(isEnabled());
            else
                (abstractCommand).setEnabled(isEnabled() && !isReadOnly());

        }
    }

    @Override
    protected void enabledChanged()
    {
        this.table.getTable().setEnabled(isEnabled());
        readOnlyChanged();
    }

    @Override
    protected AbstractCommand createAddCommand()
    {
        AbstractCommand addRow = new ActionCommand("addrow")
        {

            @Override
            protected void doExecuteCommand()
            {
                add(viewControllerObject);
            }
        };
        addRow.setSecurityControllerId(getAddCommandSecurityControllerId());
        applicationConfig.commandConfigurer().configure(addRow);
        return addRow;
    }

    /**
     * Returns the securityControllerId for the add command. Default returns <code>null</code>, provide
     * your own id to enable security.
     */
    protected String getAddCommandSecurityControllerId()
    {
        return null;
    }

    @Override
    protected AbstractCommand createRemoveCommand()
    {
        AbstractCommand removeRow = new ActionCommand("removerow")
        {

            @Override
            protected void doExecuteCommand()
            {
                remove(viewControllerObject, table.getSelectedRows());
            }
        };
        removeRow.setSecurityControllerId(getRemoveCommandSecurityControllerId());
        applicationConfig.commandConfigurer().configure(removeRow);
        return removeRow;
    }

    /**
     * Returns the securityControllerId for the remove command. Default returns <code>null</code>, provide
     * your own id to enable security.
     */
    protected String getRemoveCommandSecurityControllerId()
    {
        return null;
    }

    @Override
    protected AbstractCommand createDetailCommand()
    {
        AbstractCommand detail = new ActionCommand("detailrow")
        {

            @Override
            protected void doExecuteCommand()
            {
                Object[] selection = table.getSelectedRows();
                if (selection.length > 0)
                    showDetail(selection);
            }
        };
        applicationConfig.commandConfigurer().configure(detail);
        return detail;
    }

    @Override
    protected AbstractCommand createEditCommand()
    {
        ActionCommand editRow = new ActionCommand("editrow")
        {

            @Override
            protected void doExecuteCommand()
            {
                edit(table.getSelectedRows());
            }
        };
        editRow.setSecurityControllerId(getEditCommandSecurityControllerId());
        applicationConfig.commandConfigurer().configure(editRow);
        return editRow;
    }

    /**
     * Returns the securityControllerId for the remove command. Default returns <code>null</code>, provide
     * your own id to enable security.
     */
    protected String getEditCommandSecurityControllerId()
    {
        return null;
    }

    public void edit(Object[] rows)
    {
        if (rows != null)
        {
            currentMode = EDIT_MODE;
            getOrCreateAddEditForm();
            for (Object row : rows)
            {
                form.setFormObject(row);
                getOrCreateFormDialog().showDialog();
            }
        }
    }

    protected void add(Object list)
    {
        this.currentMode = ADD_MODE;
        getOrCreateAddEditForm().setFormObject(getNewFormObject());
        getOrCreateFormDialog().showDialog();
    }

    protected void remove(Object list, Object[] selection)
    {
        if (list instanceof Collection)
        {
            if (selection.length > 0)
            {
                ((Collection) list).removeAll(Arrays.asList(selection));
                listChanged();
            }
        }
        else
            throw new UnsupportedOperationException();
    }

    public void showDetail(Object[] rows)
    {
        if (rows != null)
        {
            getOrCreateDetailForm();
            for (Object row : rows)
            {
                detailForm.setFormObject(row);
                getOrCreateDetailFormDialog().showDialog();
            }
        }
    }

    protected AbstractForm getOrCreateAddEditForm()
    {
        if (this.form == null)
            this.form = createAddEditForm();
        return this.form;
    }

    protected AbstractForm getOrCreateDetailForm()
    {
        if (this.detailForm == null)
            this.detailForm = createDetailForm();
        return this.form;
    }

    protected AbstractForm createDetailForm()
    {
        throw new UnsupportedOperationException("Need a Form for adding/editing");
    }

    protected AbstractForm createAddEditForm()
    {
        throw new UnsupportedOperationException("Need a Form for adding/editing");
    }

    protected abstract TableDescription getTableDescription();

    protected Collection getList(Object value)
    {
        if (value instanceof Collection)
        {
            return (Collection) value;
        }
        else if (value == null)
        {
            return Collections.emptyList();
        }
        else
        {
            throw new IllegalArgumentException("getList 's argument is not a Collection: " + value.getClass());
        }
    }

    protected Object getNewFormObject()
    {
        return null;
    }

    public void setDialogId(String dialogId)
    {
        this.dialogId = dialogId;
    }

    public String getDialogId()
    {
        if (dialogId == null)
            return "listBindingDialog";
        return dialogId;
    }

    private ApplicationDialog getOrCreateFormDialog()
    {
        if (this.formDialog == null)
        {
            this.formDialog = createFormDialog();
            this.formDialog.setParentComponent(table.getComponent());
            applicationConfig.applicationObjectConfigurer().configure(this.formDialog, getDialogId());
        }
        return this.formDialog;
    }

    private ApplicationDialog getOrCreateDetailFormDialog()
    {
        if (this.detailFormDialog == null)
        {
            this.detailFormDialog = createDetailFormDialog();
            this.detailFormDialog.setParentComponent(table.getComponent());
            applicationConfig.applicationObjectConfigurer().configure(this.detailFormDialog, getDialogId());
        }
        return this.detailFormDialog;
    }

    private ApplicationDialog createDetailFormDialog()
    {
        return new TitledApplicationDialog()
        {
            protected ArrayList<AbstractCommand> getCommandGroupMembers()
            {
                return Lists.<AbstractCommand>newArrayList(getFinishCommand());
            }

            @Override
            protected JComponent createTitledDialogContentPane()
            {
                return detailForm.getControl();
            }

            @Override
            public void setTitle(String title)
            {
                super.setTitle(title);
                setTitlePaneTitle(title);
            }

            protected boolean onFinish()
            {
                return true;
            }
        };
    }

    private ApplicationDialog createFormDialog()
    {
        return new TitledApplicationDialog()
        {

            @Override
            protected JComponent createTitledDialogContentPane()
            {
                form.newSingleLineResultsReporter(this);
                form.addGuarded(this);
                return form.getControl();
            }

            @Override
            public void setTitle(String title)
            {
                super.setTitle(title);
                setTitlePaneTitle(title);
            }

            @Override
            protected void onAboutToShow()
            {
                form.getFormModel().validate();
                if (form instanceof Focussable)
                    ((AbstractFocussableWidgetForm) form).grabFocus();
            }

            @Override
            protected boolean onFinish()
            {
                if (form.hasErrors())
                    return false;
                form.commit();
                final Object formObject;
                if (currentMode == ADD_MODE)
                    formObject = onFinishAdd(viewControllerObject, form.getFormObject());
                else
                    formObject = onFinishEdit(viewControllerObject, form.getFormObject());

                if (formObject != null)
                {
                    listChanged();
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            getTable().selectRowObject(formObject, null);
                        }
                    });
                }
                return formObject != null;
            }
        };
    }

    protected Object onFinishAdd(Object list, Object newItem)
    {
        if (list instanceof Collection)
        {
            ((Collection) list).add(newItem);
            return newItem;
        }
        throw new UnsupportedOperationException(
                "onFinishAdd received a non Collection object, needs alternate implementation");
    }

    protected Object onFinishEdit(Object list, Object newItem)
    {
        return newItem;
    }
}

