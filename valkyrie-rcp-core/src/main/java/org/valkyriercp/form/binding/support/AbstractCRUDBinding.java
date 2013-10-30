package org.valkyriercp.form.binding.support;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.command.support.AbstractCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCRUDBinding extends CustomBinding
{

    private AbstractCommand removeCommand;

    private AbstractCommand addCommand;

    private AbstractCommand detailCommand;

    private AbstractCommand editCommand;

    private List<AbstractCommand> commands;

    private boolean addSupported;

    private boolean removeSupported;

    private boolean editSupported;

    private boolean showDetailSupported;

    private boolean filteringSupported;

    protected AbstractCRUDBinding(FormModel formModel, String formPropertyPath, Class requiredSourceClass)
    {
        super(formModel, formPropertyPath, requiredSourceClass);
    }

    protected List<AbstractCommand> getCommands()
    {
        if (this.commands == null)
            this.commands = createCommands();
        return this.commands;
    }

    protected List<AbstractCommand> createCommands()
    {
        int i = isAddSupported() ? 1 : 0;
        i = isRemoveSupported() ? i + 1 : i;
        i = isShowDetailSupported() ? i + 1 : i;
        i = isEditSupported() ? i + 1 : i;
        this.commands = new ArrayList<AbstractCommand>(i);
        if (isShowDetailSupported())
            commands.add(getDetailCommand());
        if (isAddSupported())
            commands.add(getAddCommand());
        if (isRemoveSupported())
            commands.add(getRemoveCommand());
        if (isEditSupported())
            commands.add(getEditCommand());
        return this.commands;
    }

    protected AbstractCommand getAddCommand()
    {
        if (this.addCommand == null)
            this.addCommand = createAddCommand();
        return this.addCommand;
    }

    abstract protected AbstractCommand createAddCommand();

    protected AbstractCommand getRemoveCommand()
    {
        if (this.removeCommand == null)
            this.removeCommand = createRemoveCommand();
        return this.removeCommand;
    }

    abstract protected AbstractCommand createRemoveCommand();

    protected AbstractCommand getDetailCommand()
    {
        if (this.detailCommand == null)
            this.detailCommand = createDetailCommand();
        return this.detailCommand;
    }

    abstract protected AbstractCommand createDetailCommand();

    protected AbstractCommand getEditCommand()
    {
        if (this.editCommand == null)
            this.editCommand = createEditCommand();
        return this.editCommand;
    }

    abstract protected AbstractCommand createEditCommand();

    public boolean isAddSupported()
    {
        return addSupported;
    }

    public void setAddSupported(boolean addSupported)
    {
        this.addSupported = addSupported;
    }

    public boolean isEditSupported()
    {
        return editSupported;
    }

    public void setEditSupported(boolean editSupported)
    {
        this.editSupported = editSupported;
    }

    public boolean isRemoveSupported()
    {
        return removeSupported;
    }

    public void setRemoveSupported(boolean removeSupported)
    {
        this.removeSupported = removeSupported;
    }

    public boolean isShowDetailSupported()
    {
        return showDetailSupported;
    }

    public void setShowDetailSupported(boolean showDetailSupported)
    {
        this.showDetailSupported = showDetailSupported;
    }

    public boolean isFilteringSupported() {
        return filteringSupported;
    }

    public void setFilteringSupported(boolean filteringSupported) {
        this.filteringSupported = filteringSupported;
    }
}

