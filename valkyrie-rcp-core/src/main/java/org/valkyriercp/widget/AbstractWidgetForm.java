package org.valkyriercp.widget;

import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.form.AbstractForm;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Provides an easy way to create widgets based on an AbstractForm.
 */
public abstract class AbstractWidgetForm extends AbstractForm implements Widget
{
    /** Id for the undo command. */
    public static final String UNDO_CMD_ID = "undo";

    /** Id for the save command. */
    public static final String SAVE_CMD_ID = "save";

    protected boolean showing = false;

    protected AbstractWidgetForm() {
    }

    protected AbstractWidgetForm(String id) {
        super(id);
    }

    public boolean canClose()
    {
        return true;
    }

    public List<? extends AbstractCommand> getCommands()
    {
        return Collections.emptyList();
    }

    public JComponent getComponent()
    {
        return getControl();
    }

    public void onAboutToHide()
    {
        showing = false;
    }

    public void onAboutToShow()
    {
        showing = true;
    }

    public boolean isShowing()
    {
        return showing;
    }

    @Override
    protected String getCommitCommandFaceDescriptorId()
    {
        return SAVE_CMD_ID;
    }

    @Override
    protected String getRevertCommandFaceDescriptorId()
    {
        return UNDO_CMD_ID;
    }

    @Override
    public void registerLocalCommandExecutors(PageComponentContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

