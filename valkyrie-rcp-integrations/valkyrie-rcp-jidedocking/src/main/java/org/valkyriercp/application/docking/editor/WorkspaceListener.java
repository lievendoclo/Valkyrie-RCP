package org.valkyriercp.application.docking.editor;

import javax.swing.SwingUtilities;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * A simple listener that reacts to OpenEditorEvents and
 * calls openEditor on the currently active page. 
 * 
 * @author Jonny Wray
 *
 */
public class WorkspaceListener implements ApplicationListener{
	
    public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof OpenEditorEvent){
			final OpenEditorEvent editorEvent = (OpenEditorEvent)event;
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow().getPage().openEditor(editorEvent.getObject());
				}
			});
		}
	}
}
