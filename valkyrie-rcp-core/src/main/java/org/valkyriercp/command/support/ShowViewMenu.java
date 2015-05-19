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

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.ViewDescriptorRegistry;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * A menu containing a collection of sub-menu items that each display a given view.
 *
 * @author Keith Donald
 */
public class ShowViewMenu extends CommandGroup implements ApplicationWindowAware {

    /** The identifier of this command. */
    public static final String ID = "showViewMenu";

    private ApplicationWindow window;

    /**
     * Creates a new {@code ShowViewMenu} with an id of {@value #ID}.
     */
    public ShowViewMenu() {
        super(ID);
    }

    /**
     * {@inheritDoc}
     */
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
        populate();
    }

    private void populate() {
        ViewDescriptor[] views = getViewDescriptorRegistry().getViewDescriptors();
        for( int i = 0; i < views.length; i++ ) {
            ViewDescriptor view = views[i];
            ActionCommand showViewCommand = view.createShowViewCommand(window);
            getCommandConfigurer().configure(showViewCommand);
            addInternal(showViewCommand);
        }
    }

    public ViewDescriptorRegistry getViewDescriptorRegistry() {
        return ValkyrieRepository.getInstance().getApplicationConfig().viewDescriptorRegistry();
    }

    public CommandConfigurer getCommandConfigurer() {
        return ValkyrieRepository.getInstance().getApplicationConfig().commandConfigurer();
    }
}
