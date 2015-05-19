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
package org.valkyriercp.application.perspective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;

import com.jidesoft.docking.DockingManager;
import org.valkyriercp.application.docking.JideApplicationPage;
import org.valkyriercp.application.docking.JideApplicationWindow;
import org.valkyriercp.application.docking.LayoutManager;

/**
 * Defines the concept of a perspective as a particular
 * arrangement of views on a specific page. Concrete implementations
 * provide a display method that uses the JIDE docking manager
 * methods to arrange the views programatically. This display
 * method is used the first time the perspective is activated, after
 * which the layout manager is used.
 * 
 * @author Jonny Wray
 *
 */
public abstract class Perspective implements BeanNameAware{
	
	private static final Logger logger = LoggerFactory.getLogger(Perspective.class);

	private String id;
	
	public Perspective(){}
	
	public Perspective(String id){
		this.id = id;
	}
	
	public void setBeanName(String beanName) {
		this.id = beanName;
	}

	/**
	 * This should return the unique id of the perspective
	 * 
	 * @return
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * This should contain methods, probably using the docking manager, that
	 * arrange views for that perspective. This will be used if the perspective
	 * has never been used before and so saved in the layout manager. Otherwise,
	 * the layout from the layout manager will be used.
	 *
	 */
	public abstract void display(DockingManager manager);

	public void switchPerspective(JideApplicationWindow window){
		
		String pageId = window.getPage().getId();if(
		logger.isDebugEnabled()){
			logger.debug("Switching perspective for page "+pageId+" to "+getId());
		}
		switchPerspective(window, pageId, true);
	}

	/*
	 * To switch a perspective is a three stage process:
	 * 		i) Possibly save the layout of the current perspective
	 * 		ii) Change the layout to that of the new perspective using an
	 * 			existing layout if it exists or the display definition if not
	 * 		iii) Set the current perspective in the perspective manager to 
	 * 			the new one.
	 * 
	 */
	public void switchPerspective(JideApplicationWindow window, String pageId, boolean saveCurrent){
		DockingManager manager = window.getDockingManager();
		PerspectiveManager perspectiveManager = 
			((JideApplicationPage)window.getPage()).getPerspectiveManager();
		if(saveCurrent){
			LayoutManager.savePageLayoutData(manager, pageId, perspectiveManager.getCurrentPerspective().getId());
		}
		if(!LayoutManager.loadPageLayoutData(manager, pageId, this)){
			display(manager);
		}
		perspectiveManager.setCurrentPerspective(this);
	}
}
