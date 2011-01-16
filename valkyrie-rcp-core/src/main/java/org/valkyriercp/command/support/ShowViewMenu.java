package org.valkyriercp.command.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.ViewDescriptorRegistry;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.command.CommandConfigurer;

/**
 * A menu containing a collection of sub-menu items that each display a given view.
 *
 * @author Keith Donald
 */
@Configurable
public class ShowViewMenu extends CommandGroup implements ApplicationWindowAware {

    /** The identifier of this command. */
    public static final String ID = "showViewMenu";

    private ApplicationWindow window;

    @Autowired
    private ViewDescriptorRegistry viewDescriptorRegistry;

    @Autowired
    private CommandConfigurer commandConfigurer;

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
        ViewDescriptor[] views = viewDescriptorRegistry.getViewDescriptors();
        for( int i = 0; i < views.length; i++ ) {
            ViewDescriptor view = views[i];
            ActionCommand showViewCommand = view.createShowViewCommand(window);
            commandConfigurer.configure(showViewCommand);
            addInternal(showViewCommand);
        }
    }

}
