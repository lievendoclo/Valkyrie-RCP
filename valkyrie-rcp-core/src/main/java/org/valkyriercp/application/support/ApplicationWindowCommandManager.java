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
package org.valkyriercp.application.support;

import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.DefaultCommandManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ApplicationWindowCommandManager extends DefaultCommandManager {
    private List sharedCommands;

    public ApplicationWindowCommandManager() {
        super();
    }

    public ApplicationWindowCommandManager(CommandRegistry parent) {
        super(parent);
    }

    public ApplicationWindowCommandManager(CommandServices commandServices) {
        super(commandServices);
    }

    public void setSharedCommandIds(String... sharedCommandIds) {
        if (sharedCommandIds.length == 0) {
            sharedCommands = Collections.EMPTY_LIST;
        }
        else {
            this.sharedCommands = new ArrayList(sharedCommandIds.length);
            for (int i = 0; i < sharedCommandIds.length; i++) {
                ActionCommand globalCommand = createTargetableActionCommand(sharedCommandIds[i], null);
                sharedCommands.add(globalCommand);
            }
        }
    }

    public void addSharedCommandIds(String... sharedCommandIds) {
        if (sharedCommands == null) {
            sharedCommands = Collections.EMPTY_LIST;
        }
        else {
            for (int i = 0; i < sharedCommandIds.length; i++) {
                ActionCommand globalCommand = createTargetableActionCommand(sharedCommandIds[i], null);
                sharedCommands.add(globalCommand);
            }
        }
    }

    public Iterator getSharedCommands() {
        if (sharedCommands == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return sharedCommands.iterator();
    }

}
