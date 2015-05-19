package org.valkyriercp.application.perspective;

import com.jidesoft.docking.DockingManager;

/**
 * A null, or do nothing, implementation of the perspective
 * concept.
 * 
 * @author Jonny Wray
 *
 */
public class NullPerspective extends Perspective {

	public static final NullPerspective NULL_PERSPECTIVE = new NullPerspective();
	private static final String ID = "nullPerspective";
	
	private NullPerspective(){
		super(ID);
	}
	
	public void display(DockingManager manager) {
		// null, so do nothing
	}

}
