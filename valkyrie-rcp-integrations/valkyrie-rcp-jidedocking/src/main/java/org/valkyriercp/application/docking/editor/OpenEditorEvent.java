package org.valkyriercp.application.docking.editor;

import org.valkyriercp.application.event.LifecycleApplicationEvent;

public class OpenEditorEvent extends LifecycleApplicationEvent {
	private static final long serialVersionUID = 4370859684282898113L;
	public static final String EVENT_TYPE = "lifecycleEvent.openEditor";
	
	public OpenEditorEvent(Object editorEvent){
		super(EVENT_TYPE, editorEvent);
	}
}
