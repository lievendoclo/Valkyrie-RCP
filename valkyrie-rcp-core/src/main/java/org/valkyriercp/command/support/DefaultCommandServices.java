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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.*;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.factory.MenuFactory;

/**
 * @author Keith Donald
 */
@Component
public class DefaultCommandServices implements CommandServices {
	private ComponentFactory componentFactory;

	private ButtonFactory toolBarButtonFactory;

    private ButtonFactory buttonFactory;

    private MenuFactory menuFactory;

    private CommandButtonConfigurer defaultButtonConfigurer;

    private CommandButtonConfigurer toolBarButtonConfigurer;

    private CommandButtonConfigurer menuItemButtonConfigurer;

    private CommandButtonConfigurer pullDownMenuButtonConfigurer;

    @Autowired
    private ApplicationConfig applicationConfig;

    public void setComponentFactory(ComponentFactory componentFactory){
    	this.componentFactory = componentFactory;
    }

    public void setToolBarButtonFactory(ButtonFactory buttonFactory){
    	this.toolBarButtonFactory = buttonFactory;
    }

    public void setButtonFactory(ButtonFactory buttonFactory) {
        this.buttonFactory = buttonFactory;
    }

    public void setMenuFactory(MenuFactory menuFactory) {
        this.menuFactory = menuFactory;
    }

    public void setDefaultButtonConfigurer(CommandButtonConfigurer defaultButtonConfigurer) {
        this.defaultButtonConfigurer = defaultButtonConfigurer;
    }

    public void setToolBarButtonConfigurer(CommandButtonConfigurer toolBarButtonConfigurer) {
        this.toolBarButtonConfigurer = toolBarButtonConfigurer;
    }

    public void setMenuItemButtonConfigurer(CommandButtonConfigurer menuItemButtonConfigurer) {
        this.menuItemButtonConfigurer = menuItemButtonConfigurer;
    }

    public void setPullDownMenuButtonConfigurer(CommandButtonConfigurer pullDownMenuButtonConfigurer) {
        this.pullDownMenuButtonConfigurer = pullDownMenuButtonConfigurer;
    }

    public ComponentFactory getComponentFactory(){
    	if(componentFactory == null){
    		componentFactory = applicationConfig.componentFactory();
    	}
    	return componentFactory;
    }

    public ButtonFactory getToolBarButtonFactory(){
    	if(toolBarButtonFactory == null){
    		toolBarButtonFactory = applicationConfig.toolbarButtonFactory();
    	}
        return toolBarButtonFactory;
    }

    public ButtonFactory getButtonFactory() {
        if(buttonFactory == null) {
            buttonFactory = applicationConfig.buttonFactory();
        }
        return buttonFactory;
    }

    public MenuFactory getMenuFactory() {
        if(menuFactory == null) {
            menuFactory = applicationConfig.menuFactory();
        }
        return menuFactory;
    }

    public CommandButtonConfigurer getDefaultButtonConfigurer() {
        if (defaultButtonConfigurer == null) {
            defaultButtonConfigurer = createDefaultButtonConfigurer();
        }
        return defaultButtonConfigurer;
    }

    public CommandButtonConfigurer getToolBarButtonConfigurer() {
        if (toolBarButtonConfigurer == null) {
            toolBarButtonConfigurer = createToolBarButtonConfigurer();
        }
        return toolBarButtonConfigurer;
    }

    public CommandButtonConfigurer getMenuItemButtonConfigurer() {
        if (menuItemButtonConfigurer == null) {
            menuItemButtonConfigurer = createMenuItemButtonConfigurer();
        }
        return menuItemButtonConfigurer;
    }

    public CommandButtonConfigurer getPullDownMenuButtonConfigurer() {
        if (pullDownMenuButtonConfigurer == null) {
            pullDownMenuButtonConfigurer = createPullDownMenuButtonConfigurer();
        }
        return pullDownMenuButtonConfigurer;
    }

    protected CommandButtonConfigurer createDefaultButtonConfigurer() {
        return new DefaultCommandButtonConfigurer();
    }

    protected CommandButtonConfigurer createToolBarButtonConfigurer() {
        return new ToolBarCommandButtonConfigurer();
    }

    protected CommandButtonConfigurer createMenuItemButtonConfigurer() {
        return new MenuItemButtonConfigurer();
    }

    protected CommandButtonConfigurer createPullDownMenuButtonConfigurer() {
        return new PullDownMenuButtonConfigurer();
    }

}
