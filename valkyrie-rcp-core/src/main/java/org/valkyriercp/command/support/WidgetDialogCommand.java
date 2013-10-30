package org.valkyriercp.command.support;

import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.widget.TitledWidgetApplicationDialog;

import java.awt.*;

public class WidgetDialogCommand extends AbstractWidgetCommand
{
    private ApplicationDialog dialog;

    /** parent for centering the dialog. */
    private Component parent;

    public WidgetDialogCommand(String id)
    {
        super(id);
    }

    protected void doExecuteCommand()
    {
        dialog = (dialog == null) ? createDialog() : dialog;
        if (getParent() != null)
        {
            dialog.setParentComponent(getParent());
        }
        dialog.showDialog();
    }

    protected ApplicationDialog createDialog()
    {
        ApplicationDialog newlyCreatedDialog = new TitledWidgetApplicationDialog(getWidget());
        applicationConfig.applicationObjectConfigurer().configure(newlyCreatedDialog, getId());
        return newlyCreatedDialog;
    }

    public Component getParent()
    {
        return parent;
    }

    /**
     * @param dialogParent
     *            The parent of the dialog for preservation of hierarchy and correct modality.
     */
    public void setParent(Component dialogParent)
    {
        this.parent = dialogParent;
    }
}


