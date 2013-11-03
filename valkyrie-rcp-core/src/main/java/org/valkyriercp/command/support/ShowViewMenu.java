package org.valkyriercp.command.support;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.ViewDescriptorRegistry;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * A menu containing a collection of sub-menu items that each display a given view.
 *
 * @author Keith Donald
 */
public class ShowViewMenu extends CommandGroup implements ApplicationWindowAware {

    /** The identifier of this command. */
    public static final String ID = "showViewMenu";

    private ApplicationWindow window;

    /**
     * Creates a new {@code ShowViewMenu} with an id of {@value #ID}.
     */
    public ShowViewMenu() {
        super(ID);
    }

    /**
     * {@inheritDoc}
     */
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
        populate();
    }

    private void populate() {
        ViewDescriptor[] views = getViewDescriptorRegistry().getViewDescriptors();
        for( int i = 0; i < views.length; i++ ) {
            ViewDescriptor view = views[i];
            ActionCommand showViewCommand = view.createShowViewCommand(window);
            getCommandConfigurer().configure(showViewCommand);
            addInternal(showViewCommand);
        }
    }

    public ViewDescriptorRegistry getViewDescriptorRegistry() {
        return ValkyrieRepository.getInstance().getApplicationConfig().viewDescriptorRegistry();
    }

    public CommandConfigurer getCommandConfigurer() {
        return ValkyrieRepository.getInstance().getApplicationConfig().commandConfigurer();
    }
}
