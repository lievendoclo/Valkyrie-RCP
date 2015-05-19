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
package org.valkyriercp.application.docking.editor;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.valkyriercp.application.Editor;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.PageComponentDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.docking.JideApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.AbstractActionCommandExecutor;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.GlobalCommandIds;
import org.valkyriercp.progress.ProgressMonitor;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Abstract base class for an editor. Provides ability to specify editor
 * specific toolbar and menubar as well as providing convience methods
 * for getting values from the descriptor, registering property change
 * listeners. Specifies a template method that should be overridden to
 * allow registration of editor specific command executors, and preregisters
 * a save and a saveAs executor that simply call the relevant editor methods
 * 
 * @author Jonny Wray
 *
 */
public abstract class AbstractEditor implements Editor {

	private static final Logger logger = LoggerFactory.getLogger(AbstractEditor.class);
	private static final String TOOLBAR_SUFFIX = ".editorToolBar";
	private static final String MENU_SUFFIX = ".editorMenuBar";

	private String toolBarCommandGroupName = null;
	private String menuBarCommandGroupName = null;
	
	private PageComponentDescriptor descriptor;
	private PageComponentContext context;
	private Object editorObject;
	
	public void setEditorInput(Object editorObject){
		this.editorObject = editorObject;
		initialize(editorObject);
	}
	
	public Object getEditorInput(){
		return editorObject;
	}

	/**
	 * This should be implemented to do the actual initialization 
	 * code with the specific editor object. Called after setEditorObject
	 * is called.
	 * 
	 * @param editorObject
	 */
	public abstract void initialize(Object editorObject);
	
	/**
	 * Implement to provide editor specific control
	 * 
	 * @return The component that is the central control in the
	 * 			editor. It has toolbar
	 * 			and menubar added to it by the framework if specified
	 */
	public abstract JComponent getControl();
	
	/**
	 * Implement to provide editor specific id. As one editor descriptor
	 * can give rise to multiple editors this needs to be unique for each
	 * editor instance.
	 * 
	 * @return The identifing string of the editor instance. Each editor in
	 * 		the collection displayed is unique.
	 */
	public abstract String getId();

	/**
	 * The display name is used as the editor tab title
	 * 
	 * @return The display name, if not overridden comes from the descriptor
	 */
	public String getDisplayName() {
		return descriptor.getDisplayName();
	}
	
	/**
	 * The caption is used by the status bar as an opened message
	 * 
	 * @return The caption, if not overridden comes from the descriptor
	 */
	public String getCaption() {
		return descriptor.getCaption();
	}
	
	/**
	 * The description is used by the editor tab tooltip
	 * 
	 * @return The description, if not overridden comes from the descriptor
	 */
	public String getDescription() {
		return descriptor.getDescription();
	}
	
	/**
	 * Default comes from the descriptor
	 */
	public Image getImage() {
		return descriptor.getImage(); 
	}
	
	/**
	 * Default comes from the descriptor
	 * 
	 * @return the icon used in the editor document tab
	 */
	public Icon getIcon() {
		return descriptor.getIcon();
	}
	
	public PageComponentDescriptor getDescriptor(){
		return descriptor;
	}

	public PageComponentContext getContext() {
		return context;
	}

    public boolean canClose(){
    	return true;
    }
    
	public void componentOpened() {

	}

	public void componentFocusGained() {

	}

	public void componentFocusLost() {

	}

	public void componentClosed() {

	}
	
	/**
	 * Method to obtain a message from the message source
	 * defined via the services locator, at the default locale.
	 */
	protected String getMessage(String key, Object[] params){
		MessageSource messageSource = ValkyrieRepository.getInstance().getApplicationConfig().messageSource();
		return messageSource.getMessage(key, params, Locale.getDefault());
	}

	/**
	 * Calls close on the active windows page
	 */
	public void close(){
		ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow().getPage().close(this);
	}
	
	/**
	 * This method is called when an editor is removed from the 
	 * workspace. 
	 */
	public void dispose(){
		if(logger.isDebugEnabled()){
			logger.debug("Disposing of editor "+getId());
		}
		context.register(GlobalCommandIds.SAVE, null);
		context.register(GlobalCommandIds.SAVE_AS, null);
		//descriptor.removePropertyChangeListener((PropertyChangeListener)context.getPane());
		concreteDispose();
	}
	
	/**
	 * This method is called by the dispose method as intended to be 
	 * overridden by the concrete implementations of actual editors. They
	 * should unregister any listeners etc. to avoid memory leaks.
	 *
	 */
	protected void concreteDispose(){}

    public void setDescriptor(PageComponentDescriptor descriptor) {
        Assert.notNull(descriptor, "The editor descriptor is required");
        Assert.state(this.descriptor == null, "An editor's descriptor may only be set once");
        this.descriptor = descriptor;
    }

    public final void setContext(PageComponentContext context) {
        Assert.notNull(context, "This editors's page component context is required");
        Assert.state(this.context == null, "An editor's context may only be set once");
        this.context = context;
        registerCommandExecutors(context);
        registerLocalCommandExecutors(context);
    }

    /*
     * Registers some editor specific command executors for save and
     * save as that just call the relevant methods within the editor. 
     */
    private void registerCommandExecutors(PageComponentContext context){
    	context.register(GlobalCommandIds.SAVE, new SaveCommandExecutor());
    	context.register(GlobalCommandIds.SAVE_AS, new SaveAsCommandExecutor());
    }
    
    /**
     * Template method called once when this editor is initialized; allows
     * subclasses to register local executors for shared commands with the view
     * context.
     *
     * @param context the view context
     */
    protected void registerLocalCommandExecutors(PageComponentContext context) {

    }

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		descriptor.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String name,
			PropertyChangeListener listener) {
		descriptor.addPropertyChangeListener(name, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		descriptor.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String name,
			PropertyChangeListener listener) {
		descriptor.removePropertyChangeListener(name, listener);
	}

	/**
	 * Default is true which means editors can always
	 * be saved.
	 */
	public boolean isDirty() {
		return true;
	}

	/**
	 * Null implementation does not do anything but the method
	 * is registered to the local save executor and 
	 * GlobalCommandIds.SAVE
	 */
	public void save() {
		
	}
	
	/**
	 * Null implementation does not do anything and is not
	 * registered to an executor by default
	 */
	public void save(ProgressMonitor monitor) {
		
	}

	/**
	 * Null implementation does not do anything but the method
	 * is registered to the local save executor and 
	 * GlobalCommandIds.SAVE_AS
	 */
	public void saveAs() {
		
	}

	/**
	 * Default is true
	 */
	public boolean isSaveAsSupported() {
		return true;
	}

	/**
	 * Default is false
	 */
	public boolean isSaveOnCloseRecommended() {
		return false;
	}

	/* 
	 * The code below supports declarative editor specific commands
	 * that can be turned into toolbars and/or menubars and added to
	 * the editor window.
	 * 
	 *  TODO: This shares a lot of code with the same concept for
	 *  	views. Either move to a common super class 
	 *  	(eg AbstractPageComponent) or to a helper class to 
	 *  	extend by composition.
	*/
	/**
	 * Injects a user defined name for the toolbar command group. If not
	 * definied then the default is constructed from
	 * descriptor.getId() + ".editorToolBar". Note the descriptor id
	 * is used as this is constant for one editor prototype whereas the
	 * actual editor id will change with the instance and so cannot
	 * be used here.
	 * 
	 * @param toolBarCommandGroupName
	 */
	public void setToolBarCommandGroupName(String toolBarCommandGroupName){
		this.toolBarCommandGroupName = toolBarCommandGroupName;
	}
	/**
	 * Injects a user defined name for the menubar command group. If not
	 * definied then the default is constructed from
	 * descriptor.getId() + ".editorMenuBar". Note the descriptor id
	 * is used as this is constant for one editor prototype whereas the
	 * actual editor id will change with the instance and so cannot
	 * be used here.
	 * 
	 * @param menuBarCommandGroupName
	 */
	public void setMenuBarCommandGroupName(String menuBarCommandGroupName){
		this.menuBarCommandGroupName = menuBarCommandGroupName;
	}
	
	public String getToolBarCommandGroupName(){
		if(toolBarCommandGroupName == null){
			return descriptor.getId() + TOOLBAR_SUFFIX;
 		}
		return toolBarCommandGroupName;
	}

	public String getMenuBarCommandGroupName(){
		if(menuBarCommandGroupName == null){
			return descriptor.getId() + MENU_SUFFIX;
 		}
		return menuBarCommandGroupName;
	}

	
	public CommandGroup getCommandGroup(String name){
		
		ApplicationLifecycleAdvisor advisor = ValkyrieRepository.getInstance().getApplicationConfig().applicationLifecycleAdvisor();
		if(advisor instanceof JideApplicationLifecycleAdvisor){
			JideApplicationLifecycleAdvisor dockingAdvisor = 
				(JideApplicationLifecycleAdvisor)advisor;
			CommandGroup commandGroup = dockingAdvisor.getSpecificCommandGroup(name);
			
			return commandGroup;
		}
		return null;
	}
	
	/**
	 * Returns the view specific menu bar constructed from
	 * the command group given by the menuBarCommandGroupName or
	 * its default
	 * 
	 * @return
	 */
	public JComponent getEditorMenuBar(){
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
	public JComponent getEditorToolBar(){
		CommandGroup commandGroup = getCommandGroup(getToolBarCommandGroupName());
		if(commandGroup == null){
			return null;
		}
		return commandGroup.createToolBar();
	}
	
/**
 * Local command executor that simply calls the saveAs command for the 
 * underlying editor. It is the responsibility of the editor to manage
 * the save process, threads, progress monitoring etc.
 * 
 * @author Jonny Wray
 *
 */
    private class SaveAsCommandExecutor extends AbstractActionCommandExecutor {
    	
    	/**
    	 * Command is enabled if the underlying editor is dirty and save
    	 * as is supported
    	 */
        public boolean isEnabled() {
			return isDirty() && isSaveAsSupported();
		}

        /**
         * Calls saveAs on the editor
         */
		public void execute() {
			saveAs();
        }
    }
    
    private class SaveCommandExecutor extends AbstractActionCommandExecutor {
    	
    	/**
    	 * Command is enabled if the underlying editor is dirty
    	 */
        public boolean isEnabled() {
			return isDirty();
		}

        /**
         * Calls save on the editor
         */
		public void execute() {
			save();
        }
    }
}
