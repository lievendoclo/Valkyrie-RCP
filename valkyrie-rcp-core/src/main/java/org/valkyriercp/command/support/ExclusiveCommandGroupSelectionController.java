package org.valkyriercp.command.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExclusiveCommandGroupSelectionController {
    private List commands = new ArrayList();

    private boolean allowsEmptySelection;

    public boolean getAllowsEmptySelection() {
        return allowsEmptySelection;
    }

    public void setAllowsEmptySelection(boolean allowsEmptySelection) {
        this.allowsEmptySelection = allowsEmptySelection;
    }

    public void add(ToggleCommand command) {
        if (!commands.contains(command)) {
            commands.add(command);
            command.setExclusiveController(this);
        }
    }

    public void remove(ToggleCommand command) {
        if (commands.remove(command)) {
            command.setExclusiveController(null);
        }
    }

    public void handleSelectionRequest(ToggleCommand delegatingCommand, boolean requestingSelection) {
        if (requestingSelection) {
            ToggleCommand currentSelectedCommand = null;
            for (Iterator iterator = commands.iterator(); iterator.hasNext();) {
                ToggleCommand command = (ToggleCommand)iterator.next();
                if (command.isSelected()) {
                    currentSelectedCommand = command;
                    break;
                }
            }
            if (currentSelectedCommand == null) {
                delegatingCommand.requestSetSelection(true);
            }
            else {
                currentSelectedCommand.requestSetSelection(false);
                delegatingCommand.requestSetSelection(!currentSelectedCommand.isSelected());
                if (!delegatingCommand.isSelected() && currentSelectedCommand != null) {
                    currentSelectedCommand.requestSetSelection(true);
                }
            }
        }
        else {
            // its a deselection
            if (allowsEmptySelection) {
                delegatingCommand.requestSetSelection(false);
            }
        }
    }

}
