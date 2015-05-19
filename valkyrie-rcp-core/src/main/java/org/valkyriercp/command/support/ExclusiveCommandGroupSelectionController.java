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
