package org.valkyriercp.widget;

import org.valkyriercp.command.support.ActionCommand;

/**
 * Widget that can pass a selection of objects, i.e. a table in which a selection can be made
 *
 * @author jh
 */
public interface SelectionWidget extends Widget
{
    /**
     * Command called to set the selection for the widget. I.e. a double-click listener on the table
     * that selects the current selected row and closes the view to the table.sluiten
     *
     * @param command
     */
    void setSelectionCommand(ActionCommand command);

    void removeSelectionCommand();

    /**
     * @return The selected object(s)
     */
    Object getSelection();
}

