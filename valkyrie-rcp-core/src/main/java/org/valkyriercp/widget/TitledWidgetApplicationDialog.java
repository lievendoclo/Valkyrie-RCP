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
package org.valkyriercp.widget;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.util.GuiStandardUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Allows to create an applicationDialog in which a given widget can be shown
 *
 * <p>
 * Don't forget to set the parent component if the parent isn't the application window
 * </p>
 */
public class TitledWidgetApplicationDialog extends ApplicationDialog
        implements
        DescriptionConfigurable,
        Messagable
{

    /** Default Id for ok command. */
    public static final String OK_COMMAND_ID = "okCommand";

    /** Default Id for cancel command. */
    public static final String CANCEL_COMMAND_ID = "cancelCommand";

    /** Default Id for exit command. */
    public static final String EXIT_COMMAND_ID = "exit";

    /** Default Id for select command. */
    public static final String SELECT_COMMAND_ID = "select";

    /** Default Id for select command. */
    public static final String SELECT_NONE_COMMAND_ID = "selectNoneCommand";

    /** Ok-mode: OK + Finish button. */
    public static final int OK_MODE = 1;

    /** Cancel-mode: Cancel button. */
    public static final int CANCEL_MODE = 2;

    /** Select-mode: Select + Cancel button. */
    public static final int SELECT_CANCEL_MODE = 3;

    private final Widget widget;

    private final int mode;

    private final String finishId;

    private final String cancelId;

    private final String titledWidgetId;

    private ActionCommand selectNoneCommand;

    /**
     * Create dialog with only OK button
     *
     * @param widget
     *            The widget to show in the dialog
     */
    public TitledWidgetApplicationDialog(Widget widget)
    {
        this(widget, OK_MODE);
    }

    /**
     * Create dialog in specified mode
     *
     * @param widget
     *            The widget to show in the dialog
     * @param mode
     *            The mode of the dialog:
     *            <code>OK_MODE, CANCEL_MODE of SELECT_CANCEL_MODE</code>.
     */
    public TitledWidgetApplicationDialog(Widget widget, int mode)
    {
        this(widget, mode, mode == SELECT_CANCEL_MODE ? SELECT_COMMAND_ID : EXIT_COMMAND_ID, EXIT_COMMAND_ID);
    }

    /**
     * Creation of dialog with full configuration
     *
     * @param widget
     *            The widget to show in the dialog
     * @param mode
     *            The mode of the dialog:
     *            <code>OK_MODE, CANCEL_MODE of SELECT_CANCEL_MODE</code>.
     * @param finishId
     *            specific id for the finish command
     * @param cancelId
     *            specific id for the cancel command.
     */
    public TitledWidgetApplicationDialog(Widget widget, int mode, String finishId, String cancelId)
    {
        this.widget = widget;
        this.mode = mode;
        this.finishId = finishId;
        this.cancelId = cancelId;
        if (widget instanceof TitledWidget)
            this.titledWidgetId = ((TitledWidget) widget).getId();
        else
            this.titledWidgetId = null;
    }

    public Widget getWidget()
    {
        return this.widget;
    }

    protected JComponent createButtonBar()
    {
        CommandGroup widgetCommands = getApplicationConfig().commandManager().createCommandGroup(null, widget.getCommands());
        CommandGroup dialogCommands = getApplicationConfig().commandManager().createCommandGroup(getCommandGroupMembers());
        JPanel panel = new JPanel(new FormLayout(new ColumnSpec[]{FormFactory.DEFAULT_COLSPEC,
                FormFactory.GLUE_COLSPEC, FormFactory.UNRELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC},
                new RowSpec[]{FormFactory.DEFAULT_ROWSPEC}));
        CellConstraints cc = new CellConstraints();
        panel.add(widgetCommands.createButtonBar(), cc.xy(1, 1));
        panel.add(dialogCommands.createButtonBar(), cc.xy(4, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return panel;
    }

    protected java.util.List<AbstractCommand> getCommandGroupMembers()
    {
        if (this.mode == SELECT_CANCEL_MODE)
            return Arrays.asList(getFinishCommand(), getSelectNoneCommand(), getCancelCommand());
        if (this.mode == OK_MODE)
            return Collections.singletonList(getFinishCommand());
        if (this.mode == CANCEL_MODE)
            return Collections.singletonList(getCancelCommand());
        return Collections.singletonList(getCancelCommand());
    }

    protected ActionCommand getSelectNoneCommand()
    {
    	if (selectNoneCommand == null)
    	{
    		selectNoneCommand = new ActionCommand(getSelectNoneCommandId()) {
    			public void doExecuteCommand() {
    				onSelectNone();
    			}
    		};
    		selectNoneCommand.setSecurityControllerId(getFinishSecurityControllerId());
    	}
    	return selectNoneCommand;
    }

    private String getSelectNoneCommandId() {
		return SELECT_NONE_COMMAND_ID;
	}

    protected void addDialogComponents()
    {
        JComponent dialogContentPane = createDialogContentPane();
        if (getPreferredSize() != null)
        {
            dialogContentPane.setSize(getPreferredSize());
        }
        if (!(this.widget instanceof TitledWidget))
        {
            GuiStandardUtils.attachDialogBorder(dialogContentPane);
        }
        getDialogContentPane().add(dialogContentPane);
        getDialogContentPane().add(createButtonBar(), BorderLayout.SOUTH);
        if (this.titledWidgetId != null)
            getApplicationConfig().applicationObjectConfigurer().configure(this.widget, this.titledWidgetId);
    }

    protected void onAboutToShow()
    {
        super.onAboutToShow();
        if (this.mode == SELECT_CANCEL_MODE && widget instanceof SelectionWidget)
            ((SelectionWidget) widget).setSelectionCommand(getFinishCommand());
        widget.onAboutToShow();
    }

    protected void onWindowClosing()
    {
        widget.onAboutToHide();
        if (this.mode == SELECT_CANCEL_MODE && widget instanceof SelectionWidget)
            ((SelectionWidget) widget).removeSelectionCommand();
        super.onWindowClosing();
    }

    protected boolean onFinish()
    {
        return true;
    }

	/**
	 * Hook called upon executing the select none command. This should normally
	 * de-select anything and execute the finish behaviour.
	 *
	 * @return
	 */
    protected boolean onSelectNone()
    {
		getFinishCommand().execute();
		return true;
    }

    /**
     * {@inheritDoc}
     */
    protected JComponent createDialogContentPane()
    {
        return widget.getComponent();
    }

    /**
     * {@inheritDoc}
     */
    public void setTitle(String title)
    {
        super.setTitle(title);
        if ((this.widget instanceof TitleConfigurable) && (this.titledWidgetId == null))
            ((TitleConfigurable) this.widget).setTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    protected String getFinishCommandId()
    {
        return this.finishId;
    }

    /**
     * {@inheritDoc}
     */
    protected String getCancelCommandId()
    {
        return this.cancelId;
    }

    /**
     * {@inheritDoc}
     */
    public void setCaption(String shortDescription)
    {
        if (this.widget instanceof DescriptionConfigurable)
            ((DescriptionConfigurable) this.widget).setCaption(shortDescription);
    }

    /**
     * {@inheritDoc}
     */
    public void setDescription(String longDescription)
    {
        if (this.widget instanceof DescriptionConfigurable)
            ((DescriptionConfigurable) this.widget).setDescription(longDescription);
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(Message message)
    {
        if (this.widget instanceof Messagable)
            ((Messagable) this.widget).setMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
    {
        if (this.widget instanceof Messagable)
            ((Messagable) this.widget).addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener propertyChangeListener)
    {
        if (this.widget instanceof Messagable)
            ((Messagable) this.widget).addPropertyChangeListener(property, propertyChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
    {
        if (this.widget instanceof Messagable)
            ((Messagable) this.widget).removePropertyChangeListener(propertyChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(String property, PropertyChangeListener propertyChangeListener)
    {
        if (this.widget instanceof Messagable)
            ((Messagable) this.widget).removePropertyChangeListener(property, propertyChangeListener);
    }
}

