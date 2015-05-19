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
