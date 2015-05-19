package org.valkyriercp.application.perspective;


import org.valkyriercp.application.docking.JideApplicationWindow;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Command to change perspectives to the one passed as a parameter
 * 
 * @author Jonny Wray
 *
 */
public class SwitchPerspectiveCommand extends ApplicationWindowAwareCommand {

	private Perspective perspective;
	
	public void setPerspective(Perspective perspective){
		this.perspective = perspective;
	}
	
    protected void doExecuteCommand() {
    	JideApplicationWindow window =
    		(JideApplicationWindow) ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow();
    	perspective.switchPerspective(window);
    }
}
