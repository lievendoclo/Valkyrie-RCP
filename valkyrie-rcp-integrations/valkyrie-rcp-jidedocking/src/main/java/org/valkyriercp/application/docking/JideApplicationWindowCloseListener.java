package org.valkyriercp.application.docking;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jidesoft.docking.DockingManager;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.perspective.Perspective;

/**
 * A window closing listener that can (and does by default)
 * save the layout of the current page before closing.
 * 
 * @author Jonny Wray
 *
 */
public class JideApplicationWindowCloseListener extends WindowAdapter{

	private ApplicationWindow window;
	private DockingManager manager;
	private boolean saveLayoutOnClose = true;

	public JideApplicationWindowCloseListener(ApplicationWindow window, DockingManager manager){
		this(window, manager, true);
	}
	
	public JideApplicationWindowCloseListener(ApplicationWindow window, DockingManager manager, boolean saveLayoutOnClose){
		this.window = window;
		this.manager = manager;
		this.saveLayoutOnClose = saveLayoutOnClose;
	}
	
	public void windowClosing(WindowEvent event) {
		if(saveLayoutOnClose){
			Perspective perspective = ((JideApplicationPage)window.getPage()).getPerspectiveManager().getCurrentPerspective();
			LayoutManager.savePageLayoutData(manager, window.getPage().getId(), perspective.getId());
		}
		window.close();
	}
}
