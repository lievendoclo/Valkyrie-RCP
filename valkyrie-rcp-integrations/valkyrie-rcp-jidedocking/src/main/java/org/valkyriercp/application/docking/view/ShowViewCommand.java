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
package org.valkyriercp.application.docking.view;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;

/**
 * Slight modification of the Spring RCP ShowViewCommand to
 * use the viewDescriptor.getId as the id for the show view
 * command, as opposed to the viewDescriptor.getDisplayName.
 * Integrates better with command labels and icons then.
 * 
 * @author Jonny Wray
 *
 */
public class ShowViewCommand extends ApplicationWindowAwareCommand {
    private ViewDescriptor viewDescriptor;

    public ShowViewCommand(ViewDescriptor viewDescriptor, ApplicationWindow window) {
        setViewDescriptor(viewDescriptor);
        setApplicationWindow(window);
        setEnabled(true);
    }

    public final void setViewDescriptor(ViewDescriptor viewDescriptor) {
        setId(viewDescriptor.getId()); 
        setLabel(viewDescriptor.getShowViewCommandLabel());
        setIcon(viewDescriptor.getIcon());
        setCaption(viewDescriptor.getCaption());
        this.viewDescriptor = viewDescriptor;
    }

    protected void doExecuteCommand() {
        getApplicationWindow().getPage().showView(viewDescriptor);
    }


}
