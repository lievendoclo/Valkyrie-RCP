package org.valkyriercp.application.docking;

import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.docking.editor.DefaultEditorRegistry;
import org.valkyriercp.application.docking.editor.EditorRegistry;
import org.valkyriercp.application.perspective.PerspectiveManager;
import org.valkyriercp.application.support.AbstractPageDescriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of a Spring RCP PageDescriptor that
 * describes a Spring Page within a JideApplicationWindow. 
 * 
 * @author Tom Corbin
 * @author Jonny Wray
 * 
 */
public class JidePageDescriptor extends AbstractPageDescriptor {

	private List _viewDescriptors = new ArrayList();
	private EditorRegistry editorFactory = new DefaultEditorRegistry();
    private Object initialEditorContents = null;

    private PerspectiveManager perspectiveManager = new PerspectiveManager();
    
	public PerspectiveManager getPerspectiveManager(){
    	return perspectiveManager;
    }
    
    /**
	 * The bean name is the default id
	 */
	public void setBeanName(String beanName){
		super.setBeanName(beanName);
		perspectiveManager.setPageName(beanName);
	}
	
	/**
	 * Set a list of perspectives for the page described by this class
	 * 
	 * @param perspectives
	 */
	public void setPerspectives(List perspectives){
		perspectiveManager.setPerspectives(perspectives);
	}
	
    /**
     * This sets the editor object that should be opened when the
     * application is started.
     * 
     * @param initialEditorContents
     */
    public void setInitialEditorContents(Object initialEditorContents){
    	this.initialEditorContents = initialEditorContents;
    }
    
    public Object getInitialEditorContents(){
    	return initialEditorContents;
    }
	
	public EditorRegistry getEditorFactory(){
		return editorFactory;
	}
	
	/**
	 * Injects an editor factory to be used to obtain editor descriptors
	 * from a given editor object, the details of which is of course 
	 * application specific. If an editor factory is not injected the
	 * default is used which simply returns nothing for every editor 
	 * object
	 * 
	 * @param editorFactory
	 */
	public void setEditorFactory(EditorRegistry editorFactory){
		this.editorFactory = editorFactory;
	}
	
	/**
	 * Builds the initial page layout by iterating the
	 * collection of view descriptors.
	 */
	public void buildInitialLayout(PageLayoutBuilder pageLayout) {
		for (Iterator iter = _viewDescriptors.iterator(); iter.hasNext();) {
			String viewDescriptorId = (String) iter.next();
			pageLayout.addView(viewDescriptorId); 
		}
	}
	
	public List getViewDescriptors() {
		return _viewDescriptors;
	}

	public void setViewDescriptors(List viewDescriptors) {
		_viewDescriptors = viewDescriptors;
	}
}
