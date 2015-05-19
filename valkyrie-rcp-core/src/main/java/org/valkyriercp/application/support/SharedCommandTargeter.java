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

import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.command.support.TargetableActionCommand;

import java.util.Iterator;

/**
 * Retargets window-scoped, shared commands when the active View associated with
 * an ApplicationPage changes.
 *
 * @author Keith Donald
 */
public class SharedCommandTargeter extends PageComponentListenerAdapter {
    private ApplicationWindow window;

    public SharedCommandTargeter(ApplicationWindow window) {
        Assert.notNull(window, "The application window containing targetable shared commands is required");
        this.window = window;
    }

    public void componentFocusGained(PageComponent component) {
        super.componentFocusGained(component);
        PageComponentContext context = component.getContext();
        for (Iterator i = window.getSharedCommands(); i.hasNext();) {
            TargetableActionCommand globalCommand = (TargetableActionCommand)i.next();
            globalCommand.setCommandExecutor(context.getLocalCommandExecutor(globalCommand.getId()));
        }
    }

}