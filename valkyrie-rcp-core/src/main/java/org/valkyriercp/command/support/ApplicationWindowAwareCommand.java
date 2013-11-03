package org.valkyriercp.command.support;

import org.jdesktop.swingx.JXFrame;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * A skeleton implementation of an action command that needs to be aware of the
 * {@link ApplicationWindow} in which it resides.
 *
 * @author Keith Donald
 */
public abstract class ApplicationWindowAwareCommand extends ActionCommand implements ApplicationWindowAware {

    private ApplicationWindow window;

    /**
     * Creates a new uninitialized {@code ApplicationWindowAwareCommand}.
     *
     */
    protected ApplicationWindowAwareCommand() {
        //do nothing
    }

    /**
     * Creates a new {@code ApplicationWindowAwareCommand} with the given command identifier.
     *
     * @param commandId The identifier of this command instance. This should be unique amongst
     * all comands within the application.
     */
    protected ApplicationWindowAwareCommand(String commandId) {
        super(commandId);
    }

    /**
     * {@inheritDoc}
     */
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
    }

    /**
     * Returns the application window that this component was created within.
     * @return The application window, or null if this property has not yet been initialized.
     */
    protected ApplicationWindow getApplicationWindow() {
        return window;
    }

    /**
     * Returns the {@link javax.swing.JFrame} of the application window that this command belongs to.
     *
     * @return The control component of the application window, never null.
     */
    protected JXFrame getParentWindowControl() {
        // allow subclasses to derive where the application window comes from
        final ApplicationWindow applicationWindow = getApplicationWindow();
        if (applicationWindow == null) {
            return ValkyrieRepository.getInstance().getApplicationConfig().windowManager().getActiveWindow().getControl();
        }
        return applicationWindow.getControl();
    }

}

