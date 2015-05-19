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
