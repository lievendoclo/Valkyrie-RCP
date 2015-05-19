package org.valkyriercp.application.docking.editor;

import java.util.Iterator;
import java.util.Map;

/**
 * A default implementation of an editor factory that can
 * be configured with an injected map that maps a Class to
 * an editor descriptor.
 * 
 * @author Jonny Wray
 *
 */
public class DefaultEditorRegistry implements EditorRegistry {


	private Map editorMap;

	public void setEditorMap(Map editorMap){
		this.editorMap = editorMap;
	}
	
	/**
	 * Returns an EditorDescriptor keyed by the class of the
	 * injected object. If non exists null is returned.
	 */
	public EditorDescriptor getEditorDescriptor(Object editorObject) {
		Class editorClass = editorObject.getClass();
		EditorDescriptor descriptor = (EditorDescriptor)editorMap.get(editorClass);
		if(descriptor == null){
			Iterator it = editorMap.keySet().iterator();
			while(it.hasNext()){
				Class klass = (Class)it.next();
				if(klass.isAssignableFrom(editorClass)){
					descriptor = (EditorDescriptor)editorMap.get(klass);
				}
			}
		}
		return descriptor;
	}

}
