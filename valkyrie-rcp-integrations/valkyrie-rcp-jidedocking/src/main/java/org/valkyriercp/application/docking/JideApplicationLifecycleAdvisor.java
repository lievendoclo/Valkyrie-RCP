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
package org.valkyriercp.application.docking;

import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;
import org.valkyriercp.application.support.DefaultStatusBar;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.RepaintManager;

/**
 * Extends the default application lifecycle advisor to allow the
 * injection of any status bar command group implementation. It also
 * changes the repaint manager to use the technique of Scott Deplap
 * to detetect illegal UI updates outside of the EDT
 * 
 * @author Jonny Wray
 */
public class JideApplicationLifecycleAdvisor extends
        DefaultApplicationLifecycleAdvisor {
	
	private StatusBar statusBar = null;
	private RepaintManager repaintManager;

	public CommandGroup getSpecificCommandGroup(String name){
		return (CommandGroup) ValkyrieRepository.getInstance().getApplicationConfig().commandManager().getCommand(name, CommandGroup.class);
	}
	
	public void onPostStartup(){
		initializeRepaintManager();
	}
	
	/**
	 * Allows the injection of a global repaint manager, although
	 * not required, in which case default is used.
	 * 
	 * Initial motivation came from the discussion in 
	 * <a href="http://www.sourcebeat.com/TitleAction.do?id=10">Desktop Java Live</a> by
	 * Scott Deplay about using a custom repaint manager to
	 * check for illegal UI updates outside of the EDT.
	 * 
	 * 
	 * @param repaintManager 
	 */
	public void setRepaintManager(RepaintManager repaintManager){
		this.repaintManager = repaintManager;
	}
	
	/**
	 * Change the default StatusBarCommandGroup by 
	 * providing another implementation.
	 * 
	 * @param statusBar
	 */
	public void setStatusBar(StatusBar statusBar){
		this.statusBar = statusBar;
	}
	
	/**
	 * Returns the current status bar. If one is
	 * not injected in the configuration file the default
	 * is used.
	 */
	public StatusBar getStatusBar(){
		if(statusBar == null){
			statusBar = new DefaultStatusBar();
		}
		return statusBar;
	}

	private void initializeRepaintManager(){
		if(repaintManager != null){
			RepaintManager.setCurrentManager(repaintManager);
		}
	}
	
}
