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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * This class is responsible for saving and retreiving the current
 * perspectives including across restarts. Each page uses its own
 * perspective manager.
 * 
 * @author Jonny Wray
 *
 */
public class PerspectiveManager  {

	private List perspectives = new ArrayList();
	private static final Preferences prefs = Preferences.userNodeForPackage(PerspectiveManager.class);
	
	private String pageName = "";
	private static final String CURRENT_PERSPECTIVE_KEY = "CURRENT_PERSPECTIVE_{0}";
	private static final String DEFAULT_PERSPECTIVE_KEY = "defaultPerspective";
	
	public void setPageName(String pageName){
		this.pageName = pageName;
	}
	
	public void setPerspectives(List perspectives){
		this.perspectives = perspectives;
	}
	
	public Perspective getDefaultPerspective(){

		String id = MessageFormat.format(DEFAULT_PERSPECTIVE_KEY, new Object[]{pageName});
		Iterator it = perspectives.iterator();
		while(it.hasNext()){
			Perspective perspective = (Perspective)it.next();
			if(id.equals(perspective.getId())){
				return perspective;
			}
		}
		return NullPerspective.NULL_PERSPECTIVE;
	}
	
	/**
	 * Returns the current perspective, or the NullPerspective instance if 
	 * no current perspective is defined.
	 */
	public Perspective getCurrentPerspective(){
		String key = MessageFormat.format(CURRENT_PERSPECTIVE_KEY, new Object[]{pageName});
		String id = prefs.get(key, DEFAULT_PERSPECTIVE_KEY);
		Iterator it = perspectives.iterator();
		while(it.hasNext()){
			Perspective perspective = (Perspective)it.next();
			if(id.equals(perspective.getId())){
				return perspective;
			}
		}
		return NullPerspective.NULL_PERSPECTIVE;
	}
	
	public void setCurrentPerspective(Perspective perspective){
		String key = MessageFormat.format(CURRENT_PERSPECTIVE_KEY, new Object[]{pageName});
		prefs.put(key, perspective.getId());
	}
}
