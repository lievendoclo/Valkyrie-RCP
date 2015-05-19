package org.valkyriercp.application.docking;

import java.awt.BorderLayout;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.utils.Lm;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.perspective.NullPerspective;
import org.valkyriercp.application.perspective.Perspective;
import org.valkyriercp.application.perspective.PerspectiveManager;
import org.valkyriercp.application.support.AbstractApplicationWindow;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * An implementation of the Spring RCP ApplicationWindow that uses
 * the JIDE docking framework as the underlying manager. This class
 * adds the ability to specify the active page from the collection
 * of configured pages, and to reload the layout data for a specific
 * page and perspective.
 * 
 * @author Tom Corbin
 * @author Jonny Wray
 *
 */
public class JideApplicationWindow extends AbstractApplicationWindow {
	private DefaultDockableHolder dockableHolder;

	public JideApplicationWindow(ApplicationConfig config, DefaultDockableHolder dockableHolder){
        this(config, dockableHolder, ValkyrieRepository.getInstance().getApplicationConfig().windowManager().size());
	}
	
	public JideApplicationWindow(ApplicationConfig config, DefaultDockableHolder dockableHolder, int number){
		super(number, config);
		this.dockableHolder = dockableHolder;
	}
	
	/**
	 * Overridden close method to avoid memory leaks by Mikael Valot
	 */
	public boolean close(){
		if(super.close()){
			dockableHolder.dispose();
			dockableHolder.removeWindowFocusListener(this);
			WindowListener[] listeners = dockableHolder.getWindowListeners();
            for (WindowListener listener : listeners) {
                dockableHolder.removeWindowListener(listener);
            }
			Lm.setParent(null);
			dockableHolder.removeAll();
			dockableHolder.getRootPane().removeAll();
			dockableHolder = null;
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
     * Overrides the applyStandardLayout by removing the 
     * setting of the layout manager and the insertion of
     * the center part of the frame. The JIDE docking framework
     * actually sets these, via the DefaultDockableHolder.
     */
    protected void applyStandardLayout(JFrame windowControl,
                                       ApplicationWindowConfigurer configurer) {
    	windowControl.setTitle(configurer.getTitle());
        windowControl.setIconImage(configurer.getImage());
        windowControl.setJMenuBar(createMenuBarControl());
        windowControl.getContentPane().add(createToolBarControl(), BorderLayout.NORTH);
        windowControl.getContentPane().add(createStatusBarControl(), BorderLayout.SOUTH);
    }

    /**
     * This returns null since it is not actually used as the applyStandardLayout has
     * been overridden to pass control for the standard layout to the JIDE framework
     */
	protected JComponent createWindowContentPane() {
		return null;
	}
	
	/**
	 * The window control is the JIDE dockable holder, so return that.
	 */
    protected JFrame createNewWindowControl() {
		return dockableHolder;
    }
    
    /**
     * Sets the active page by loading that page's components and
     * applying the layout. Also updates the show view command menu
     * to list the views within the page.
     */
	protected void setActivePage(ApplicationPage page) {
    	getPage().getControl();
    	loadLayoutData(page.getId());
    	((JideApplicationPage)getPage()).updateShowViewCommands();
	}

    public DockingManager getDockingManager(){
    	return dockableHolder.getDockingManager();
    }
    
    public void loadLayoutData(String pageId) {
	/*
	 * Logic: if the current perspective is either the null on (first time
	 * 		use) or the layout is invalid then use the default perspective.
	 */
		PerspectiveManager perspectiveManager = ((JideApplicationPage)getPage()).getPerspectiveManager();
		Perspective perspective = perspectiveManager.getCurrentPerspective();
		if(perspective == NullPerspective.NULL_PERSPECTIVE ||
				!LayoutManager.isValidLayout(dockableHolder.getDockingManager(), pageId, perspective)){
			perspective = perspectiveManager.getDefaultPerspective();
		}
		perspective.switchPerspective(this, pageId, false);
	}
}
