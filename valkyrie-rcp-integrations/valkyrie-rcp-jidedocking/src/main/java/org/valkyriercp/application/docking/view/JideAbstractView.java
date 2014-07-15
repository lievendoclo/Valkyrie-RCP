/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.valkyriercp.application.docking.view;

import org.valkyriercp.application.docking.JideApplicationLifecycleAdvisor;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;


/**
 * An extension of the Spring RCP abstract view to implement
 * the ability to specify view specific toolbars and menu bar.
 * 
 * @author Jonny Wray
 *
 */
public abstract class JideAbstractView extends AbstractView {

	private static final String TOOLBAR_SUFFIX = ".viewToolBar";
	private static final String MENU_SUFFIX = ".viewMenuBar";
	
	private String toolBarCommandGroupName = null;
	private String menuBarCommandGroupName = null;

    protected JideAbstractView(String id) {
        super(id);
    }

    /**
	 * Injects a user defined name for the toolbar command group. If not
	 * definied then the default is constructed from
	 * getId() + ".viewToolBar"
	 * 
	 * @param toolBarCommandGroupName
	 */
	public void setToolBarCommandGroupName(String toolBarCommandGroupName){
		this.toolBarCommandGroupName = toolBarCommandGroupName;
	}
	/**
	 * Injects a user defined name for the menubar command group. If not
	 * definied then the default is constructed from
	 * getId() + ".viewMenuBar"
	 * 
	 * @param menuBarCommandGroupName
	 */
	public void setMenuBarCommandGroupName(String menuBarCommandGroupName){
		this.menuBarCommandGroupName = menuBarCommandGroupName;
	}
	
	public String getToolBarCommandGroupName(){
		if(toolBarCommandGroupName == null){
			return getId() + TOOLBAR_SUFFIX;
 		}
		return toolBarCommandGroupName;
	}

	public String getMenuBarCommandGroupName(){
		if(menuBarCommandGroupName == null){
			return getId() + MENU_SUFFIX;
 		}
		return menuBarCommandGroupName;
	}

	
	public CommandGroup getCommandGroup(String name){
		JideApplicationLifecycleAdvisor advisor =
			((JideApplicationLifecycleAdvisor) ValkyrieRepository.getInstance().getApplicationConfig().applicationLifecycleAdvisor());
		CommandGroup commandGroup = advisor.getSpecificCommandGroup(name);
		return commandGroup;
	}
	
	/**
	 * Returns the view specific menu bar constructed from
	 * the command group given by the menuBarCommandGroupName or
	 * its default
	 * 
	 * @return
	 */
	public JMenuBar getViewMenuBar(){

		CommandGroup commandGroup = getCommandGroup(getMenuBarCommandGroupName());
		if(commandGroup == null){
			return null;
		}
		return commandGroup.createMenuBar();
	}

	/**
	 * Returns the view specific menu bar constructed from
	 * the command group given by the toolBarCommandGroupName or
	 * its default
	 * 
	 * @return
	 */
	public JComponent getViewToolBar(){

		CommandGroup commandGroup = getCommandGroup(getToolBarCommandGroupName());
		if(commandGroup == null){
			return null;
		}
		return commandGroup.createToolBar();
	}

	/**
	 * Activates this view taking care of EDT issues
	 *
	 */
	public void activateView(){
		if(SwingUtilities.isEventDispatchThread()){
			ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow().getPage().showView(getId());
		}
		else{
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
                    ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow().getPage().showView(getId());
				}
				
			});
		}
	}
}
