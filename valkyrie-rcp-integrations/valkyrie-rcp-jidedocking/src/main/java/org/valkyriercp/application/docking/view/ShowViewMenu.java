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
import org.valkyriercp.application.ViewDescriptorRegistry;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Slight change to the Spring RCP show view menu to check if the view is
 * not a workspace view before adding it to menu.
 * 
 * @author Jonny Wray
 *
 */
public class ShowViewMenu extends CommandGroup implements ApplicationWindowAware {

    private static final String ID = "showViewMenu";

    private ApplicationWindow window;

    public ShowViewMenu() {
        super(ID);
    }

    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        populate();
    }

    private void populate() {
        ViewDescriptorRegistry viewDescriptorRegistry = ValkyrieRepository.getInstance().getApplicationConfig().viewDescriptorRegistry();
        ViewDescriptor[] views = viewDescriptorRegistry.getViewDescriptors();
        for (int i = 0; i < views.length; i++) {
            ViewDescriptor view = views[i];
            if(view instanceof JideViewDescriptor){
            	JideViewDescriptor dockingView = (JideViewDescriptor)view;
            	if(!dockingView.isWorkspace()){
            		addInternal(view.createShowViewCommand(window));
            	}
            }
            else{
            	addInternal(view.createShowViewCommand(window));
            }
        }
    }
}
