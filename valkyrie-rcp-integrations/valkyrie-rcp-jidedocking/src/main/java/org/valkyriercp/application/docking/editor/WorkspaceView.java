package org.valkyriercp.application.docking.editor;

import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.jidesoft.docking.DockingManager;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentComponentAdapter;
import com.jidesoft.document.DocumentComponentEvent;
import com.jidesoft.document.DocumentComponentListener;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.document.IDocumentGroup;
import com.jidesoft.document.PopupMenuCustomizer;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.StringConverter;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.docking.JideApplicationPage;
import org.valkyriercp.application.docking.JideApplicationWindow;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Encapsulates the Jide mechanism for opening editors in their
 * so called workspace. Basically adapts the Jide workspace
 * concept (ie document pane and DocumentComponent) to the
 * Spring RCP architecture.
 * 
 * @author Jonny Wray
 *
 */
public class WorkspaceView extends AbstractView {
	private static final String PROFILE_KEY = "WorkspaceView";

    private Map pageComponentMap = new HashMap();
	private MouseListener tabDoubleClickListener = new DoubleClickListener();
	private boolean autohideAll = false;
	
	private byte[] fullScreenLayout = null;
    private DocumentPane contentPane;
    private DropTargetListener dropTargetListener;
    
    private boolean closeAndReopenEditor = true;
    private boolean groupsAllowed = true;
    private boolean heavyweightComponentEnabled = false;
    private boolean doubleClickMaximizeEnabled = true;
    private boolean reorderAllowed = true;
    private boolean showContextMenu = true;
    private int maxGroupCount = 0;
    private int tabPlacement = SwingConstants.TOP;
    private StringConverter titleConverter = null;
    private boolean updateTitle = true;
    private DocumentPane.TabbedPaneCustomizer tabbedPaneCustomizer = new DefaultCustomizer();
    private PopupMenuCustomizer popupMenuCustomizer = null;

    protected WorkspaceView(String id) {
        super(id);
    }

    public void setCloseAndReopenEditor(boolean closeAndReopenEditor){
    	this.closeAndReopenEditor = closeAndReopenEditor;
    }
    
    public void setTabDoubleClickMouseListener(MouseListener tabDoubleClickListener){
    	this.tabDoubleClickListener = tabDoubleClickListener;
    }
    
    /**
     * Configures the maximum group count for the document pane. Default is zero which
     * implies unlimited.
     * 
     * @param maxGroupCount
     */
    public void setMaximumGroupCount(int maxGroupCount){
    	this.maxGroupCount = maxGroupCount;
    }
    
    /**
     * Configure the groupsAllowed property of the document pane;
     * @param groupsAllowed
     */
    public void setGroupsAllowed(boolean groupsAllowed){
    	this.groupsAllowed = groupsAllowed;
    }
    
    /**
     * Configure the updateTitle property of the document pane.
     * 
     * @param updateTitle
     */
    public void setUpdateTitle(boolean updateTitle){
    	this.updateTitle = updateTitle;
    }
    
    /**
     * Configure the title converter of the document pane
     * @param titleConverter
     */
    public void setTitleConverter(StringConverter titleConverter){
    	this.titleConverter = titleConverter;
    }
    
    /**
     * Configures the tab placement of the underlying document pane
     * 
     * @param tabPlacement
     */
    public void setTabPlacement(int tabPlacement){
    	this.tabPlacement = tabPlacement;
    }
    
    /**
     * Specifies a PopumMenuCustomizer to be used by the document pane.
     * 
     * @param customizer
     */
    public void setPopupMenuCustomizer(PopupMenuCustomizer customizer){
    	this.popupMenuCustomizer = customizer;
    }
    
    /**
     * Specifies a TabbedPaneCustomizer that allows the appearence of the tabbed pane used
     * in the workspace to be customized. Note, the method call setRequestFocusEnabled(true);
     * should be made to ensure correct JIDE to Spring RCP event translation. If not 
     * customizer is specified then this requestFocusEnabled is set to true by default.
     * 
     * @param tabbedPaneCustomizer the specific customizer
     */
    public void setTabbedPaneCustomizer(DocumentPane.TabbedPaneCustomizer tabbedPaneCustomizer){
    	this.tabbedPaneCustomizer = tabbedPaneCustomizer;
    }
    
    /**
     * Specifies if the show context menu flag is set on the underlying document pane, default if
     * not set is true.
     * 
     * @param showContextMenu
     */
    public void setShowContextMenu(boolean showContextMenu){
    	this.showContextMenu = showContextMenu;
    }
    
    /**
     * Specifies if reordering is allowed in the underlying document pane. If not
     * set then default is true.
     * 
     * @param reorderAllowed
     */
    public void setReorderAllowed(boolean reorderAllowed){
    	this.reorderAllowed = reorderAllowed;
    }
    
    /**
     * Specified whether the documents should maximize on double click, and
     * minimize again on next double click. Default is true.
     * 
     * @param doubleClickMaximizedEnabled maximize on double click. Default is true.
     */
    public void setDoubleClickMaximizeEnabled(boolean doubleClickMaximizedEnabled){
    	this.doubleClickMaximizeEnabled = doubleClickMaximizedEnabled;
    }
    
    /**
     * Specifies if heavyweight components are enabled for the workspace. Default is
     * false.
     * 
     * @param heavyweightComponentEnabled enable heavyweight components.
     */
    public void setHeavyweightComponentEnabled(boolean heavyweightComponentEnabled){
    	this.heavyweightComponentEnabled = heavyweightComponentEnabled;
    }
    
	public void setDropTargetListener(DropTargetListener dropTargetListener){
    	this.dropTargetListener = dropTargetListener;
    }
	
	/**
	 * Overridden close method to avoid memory leaks by Mikael Valot
	 */
	public void dispose(){
		Collection pageComponents = new ArrayList(pageComponentMap.values());
		Iterator it = pageComponents.iterator();
		while(it.hasNext()){
			PageComponent pageComponent = (PageComponent)it.next();
			remove(pageComponent);
		}
		contentPane.dispose();
		contentPane = null;
		super.dispose();
	}
	
	/**
	 * Returns a map of document names to the page component, which will
	 * be concrete editors.
	 * @return
	 */
	public Map getPageComponentMap(){
		return Collections.unmodifiableMap(pageComponentMap);
	}
	
	public PageComponent getActiveComponent(){
    	String documentName = contentPane.getActiveDocumentName();
		PageComponent component = (PageComponent)pageComponentMap.get(documentName);
		return component;
	}
	
    /**
     * This is a bit of a hack to get over a limitation in the JIDE
     * docking framework. When focus is regained to the workspace by
     * activation the currently activated document the
     * documentComponentActivated is not fired. This needs to be fired
     * when we know the workspace has become active. For some reason
     * it dosen't work when using the componentFocusGained method
     */
    public void fireFocusGainedOnActiveComponent(){
    	String documentName = contentPane.getActiveDocumentName();
    	if(documentName != null){
    		PageComponent component = (PageComponent)pageComponentMap.get(documentName);
    		JideApplicationPage page = (JideApplicationPage)getActiveWindow().getPage();
    		page.fireFocusGained(component);
    	}
    }

    private ApplicationWindow getActiveWindow() {
        return ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow();
    }
    
    private void fireFocusGainedOnWorkspace(){
    	JideApplicationPage page = (JideApplicationPage)getActiveWindow().getPage();
    	page.fireFocusGained(this);
    }
    
    
    private void registerDropTargetListeners(Component component){
    	if(dropTargetListener != null){
    		new DropTarget(component, dropTargetListener);
    	}
    }
    
	protected JComponent createControl() {
		if(contentPane == null){
	    	contentPane = constructDocumentPane();
	        registerDropTargetListeners(contentPane);
	    	contentPane.getLayoutPersistence().setProfileKey(PROFILE_KEY);
		}
    	return contentPane;
    }
	
	private DocumentPane constructDocumentPane(){
		DocumentPane documentPane;
		if(doubleClickMaximizeEnabled){
			documentPane = new DocumentPane(){
	            // add function to maximize (autohideAll) the document pane when mouse 
	    		// double clicks on the tabs of DocumentPane. This comes from the JIDE
	    		// demos
	            protected IDocumentGroup createDocumentGroup() { 
	                IDocumentGroup group = super.createDocumentGroup();
	                if (group instanceof JideTabbedPane) {
	                	JideTabbedPane tabbedPane = (JideTabbedPane)group;
	                	tabbedPane.addMouseListener(tabDoubleClickListener);
	                    //((JideTabbedPaneUI) ((JideTabbedPane) group).getUI()).getTabPanel().
	                    //		addMouseListener(tabDoubleClickListener);
	                }
	                return group;
	            }
			};
		}
		else{
			documentPane = new DocumentPane();
		}
		documentPane.setHeavyweightComponentEnabled(heavyweightComponentEnabled);
		documentPane.setTabbedPaneCustomizer(tabbedPaneCustomizer);
		documentPane.setReorderAllowed(reorderAllowed);
		documentPane.setShowContextMenu(showContextMenu);
		documentPane.setTabPlacement(tabPlacement);
		documentPane.setUpdateTitle(updateTitle);
		documentPane.setGroupsAllowed(groupsAllowed);
		documentPane.setMaximumGroupCount(maxGroupCount);
		if(titleConverter != null){
			documentPane.setTitleConverter(titleConverter);
		}
		if(popupMenuCustomizer != null){
			documentPane.setPopupMenuCustomizer(popupMenuCustomizer);
		}
		return documentPane;
	}
	
	/**
	 * By default constructs an EditorLifecycleListener, override to provide an application
	 * specific document component listener factory method.
	 * 
	 * @param pageComponent
	 * @return
	 */
	protected DocumentComponentListener constructLifecycleListener(PageComponent pageComponent){
		return new EditorLifecycleListener(this, pageComponent);
	}
    

	/**
	 * Calls addDocumentComponent with activateAfterOpen as true
	 * 
	 */
    public void addDocumentComponent(final PageComponent pageComponent){
    	addDocumentComponent(pageComponent, true);
    }
	
    /**
     * Adds a document to the editor workspace. The behaviour when an
     * editor is already open, with editor identity defined by id property,
     * is determined by the closeAndReopenEditor property. If this property
     * is true, the default, the editor is closed and reopened. If false, the
     * existing editor becomes the active one.
     * 
     * @param pageComponent The page component to be added as a document
     * @param activateAfterOpen specifies if the component should be activated after opening
     */
    public void addDocumentComponent(final PageComponent pageComponent, boolean activateAfterOpen){
    	String id = pageComponent.getId();
    	if(!closeAndReopenEditor && contentPane.getDocument(id) != null){
    		contentPane.setActiveDocument(id);
    	}
    	else{
	    	if(contentPane.getDocument(id) != null){
	    		contentPane.closeDocument(id);
	    	}
	    	DocumentComponent document = constructDocumentComponent(pageComponent);
	    	DocumentComponentListener lifecycleListener = constructLifecycleListener(pageComponent);
	    	document.addDocumentComponentListener(lifecycleListener);
	    	if(contentPane.getDocument(id) == null){
	    		contentPane.openDocument(document);
	    	}
	    	if(activateAfterOpen){
	    		contentPane.setActiveDocument(id);
	    	}
	    	registerDropTargetListeners(pageComponent.getControl());
	    // This listener ensures that the focus is transfered to the workspace
	    //	itself if the number of documents becomes zero.
	    	document.addDocumentComponentListener(new DocumentComponentAdapter(){
	        	public void documentComponentClosed(DocumentComponentEvent event) {
	        		int count = contentPane.getDocumentCount();
	        		if(count == 0){
	        			fireFocusGainedOnWorkspace();
	        		}
	        	}
	    	});
	    	pageComponentMap.put(id, pageComponent);
    	}
    }
    
    public void remove(PageComponent pageComponent){
    	contentPane.closeDocument(pageComponent.getId());
    	pageComponentMap.remove(pageComponent.getId());
    	pageComponent.dispose();
    }
    
    /*
	 * Actually constructs a Jide document component from the page component
	 */
    private DocumentComponent constructDocumentComponent(final PageComponent pageComponent) {
    	String id = pageComponent.getId();
		String title = pageComponent.getDisplayName();
		Icon icon = pageComponent.getIcon();
		DocumentComponent document = new DocumentComponent(
				pageComponent.getContext().getPane().getControl(), 
				id, title, icon);
		document.setTooltip(pageComponent.getDescription()); 
    	return document;
    }
    
    private class DoubleClickListener extends MouseAdapter{
    	
    	public void mouseClicked(MouseEvent e) {
    		DockingManager manager = ((JideApplicationWindow)getActiveWindow()).getDockingManager();
    		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                if (!autohideAll) {
                    fullScreenLayout = manager.getLayoutRawData();
                    manager.autohideAll();
                    autohideAll = true;
                }
                else {
                    // call next two methods so that the farme bounds and state will not change.
                    manager.setUseFrameBounds(false);
                    manager.setUseFrameState(false);
                    if (fullScreenLayout != null) {
                        manager.setLayoutRawData(fullScreenLayout);
                    }
                    autohideAll = false;
                }
            }
        }
    }
    
    private static class DefaultCustomizer implements DocumentPane.TabbedPaneCustomizer{
    	//  This is needed for correct JIDE to Spring RCP focus/activation
		//	event translation
		public void customize(JideTabbedPane tabbedPane) {
			tabbedPane.setRequestFocusEnabled(true);
		}
    }
    
}
