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
