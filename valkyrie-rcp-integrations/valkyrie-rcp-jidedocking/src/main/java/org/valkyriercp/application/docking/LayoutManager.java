package org.valkyriercp.application.docking;

import com.jidesoft.docking.DockingManager;
import org.valkyriercp.application.perspective.Perspective;

import java.text.MessageFormat;


/**
 * A simple manager of JIDE layouts that has the ability to save and restore
 * layouts based on page ids and a perspective id, allowing multiple saved
 * layouts per page.
 * 
 * @author Jonny Wray
 *
 */
public class LayoutManager {
	private static final String PAGE_LAYOUT = "page_{0}_layout_{1}";
	
	public static boolean isValidLayout(DockingManager manager, String pageId, Perspective perspective){

		String pageLayout = MessageFormat.format(PAGE_LAYOUT, pageId, perspective.getId());
		return manager.isLayoutAvailable(pageLayout) && 
			manager.isLayoutDataVersionValid(pageLayout);
	}
	
	/**
	 * Loads a the previously saved layout for the current page. If no 
	 * previously persisted layout exists for the given page the built 
	 * in default layout is used.
	 * 
	 * @param manager The docking manager to use
	 * @param pageId The page to get the layout for
	 * @return a boolean saying if the layout requested was previously saved
	 */
	public static boolean loadPageLayoutData(DockingManager manager, String pageId, Perspective perspective){
		manager.beginLoadLayoutData();
		try{
			if(isValidLayout(manager, pageId, perspective)){
				String pageLayout = MessageFormat.format(PAGE_LAYOUT, pageId, perspective.getId());
				manager.loadLayoutDataFrom(pageLayout);
				return true;
			}
			else{
				manager.loadLayoutData();
				return false;
			}
		}
		catch(Exception e){
			manager.loadLayoutData();
			return false;
		}
	}
	
	/**
	 * Saves the current page layout.
	 * 
	 * @param manager The current docking manager
	 * @param pageId The page to saved the layout for
	 */
	public static void savePageLayoutData(DockingManager manager, String pageId, String perspectiveId){
		manager.saveLayoutDataAs(MessageFormat.format(PAGE_LAYOUT,
                pageId, perspectiveId));
	}
}
