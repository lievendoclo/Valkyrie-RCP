package org.valkyriercp.security;

import org.springframework.security.core.Authentication;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;

/**
 * Provides a command to log the current user out.
 * <p>
 * Logout handling is performed by calling {@link ApplicationSecurityManager#doLogout()}.
 * See that class for details.
 * <P>
 * No server-side call will occur to indicate logout. If this is required, you
 * should extend this class and use the {@link #onLogout} method.
 *
 * @author Ben Alex
 * @author Larry Streepy
 */
public class LogoutCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "logoutCommand";

    private LoginCommand loginCommand;

    public LogoutCommand(LoginCommand loginCommand) {
        super(ID);
        this.loginCommand = loginCommand;
    }

    private boolean displaySuccess = true;

    public ApplicationSecurityManager getApplicationSecurityManager() {
        return ValkyrieRepository.getInstance().getApplicationConfig().applicationSecurityManager();
    }

    /**
     * Indicates whether an information message is displayed to the user upon
     * successful logout. Defaults to true.
     *
     * @param displaySuccess
     *            displays an information message upon successful logout if
     *            true, otherwise false
     */
    public void setDisplaySuccess(boolean displaySuccess) {
        this.displaySuccess = displaySuccess;
    }

    protected void doExecuteCommand() {
        Authentication loggedOutAuth = getApplicationSecurityManager().doLogout();
        if (displaySuccess) {
            JOptionPane.showMessageDialog(getParentWindowControl(), "You have been logged out.", "Logout Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        onLogout(loggedOutAuth);
        loginCommand.execute();
    }

    /**
     * Can be extended by subclasses to perform additional logout processing,
     * such as notifying a server etc.
     */
    public void onLogout(Authentication loggedOut) {
    }

}
