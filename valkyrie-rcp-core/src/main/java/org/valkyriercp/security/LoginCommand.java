package org.valkyriercp.security;

import org.springframework.security.core.Authentication;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;
import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.dialog.CompositeDialogPage;
import org.valkyriercp.dialog.TabbedDialogPage;
import org.valkyriercp.dialog.TitledPageApplicationDialog;

public class LoginCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "loginCommand";

    private boolean displaySuccessMessage = true;

    private boolean closeOnCancel = true;

    private boolean clearPasswordOnFailure = true;

    private String defaultUserName = null;

    private ApplicationDialog dialog = null;

    /**
     * Constructor.
     */
    public LoginCommand() {
        super( ID );
    }

    /**
     * Indicates whether an information message is displayed to the user upon successful
     * authentication. Defaults to true.
     *
     * @param displaySuccessMessage displays an information message upon successful login if
     *            true, otherwise false
     */
    public void setDisplaySuccess(boolean displaySuccessMessage) {
        this.displaySuccessMessage = displaySuccessMessage;
    }

    /**
     * Execute the login command. Display the dialog and attempt authentication.
     */
    protected void doExecuteCommand() {
        CompositeDialogPage tabbedPage = new TabbedDialogPage( "loginForm" );

        final LoginForm loginForm = createLoginForm();

        tabbedPage.addForm( loginForm );

        if( getDefaultUserName() != null ) {
            loginForm.setUserName( getDefaultUserName() );
        }

        dialog = new TitledPageApplicationDialog( tabbedPage ) {
            protected boolean onFinish() {
                loginForm.commit();
                Authentication authentication = loginForm.getAuthentication();

                // Hand this token to the security manager to actually attempt the login
                ApplicationSecurityManager sm = applicationConfig.applicationSecurityManager();
                try {
                    sm.doLogin( authentication );
                    postLogin();
                    return true;
                } finally {
                    if( isClearPasswordOnFailure() ) {
                        loginForm.setPassword("");
                    }
                    loginForm.requestFocusInWindow();
                }
            }

            protected void onCancel() {
                super.onCancel(); // Close the dialog

                // Now exit if configured
                if( isCloseOnCancel() ) {
                    ApplicationSecurityManager sm = applicationConfig.applicationSecurityManager();
                    Authentication authentication = sm.getAuthentication();
                    if( authentication == null ) {
                        LoginCommand.this.logger.info( "User canceled login; close the application." );
                        applicationConfig.application().close();
                    }
                }
            }

            protected ActionCommand getCallingCommand() {
                return LoginCommand.this;
            }

            protected void onAboutToShow() {
                loginForm.requestFocusInWindow();
            }
        };
        dialog.setDisplayFinishSuccessMessage( displaySuccessMessage );
        dialog.showDialog();
    }

    /**
     * Construct the Form to place in the login dialog.
     * @return form to use
     */
    protected LoginForm createLoginForm() {
        return new LoginForm();
    }

    /**
     * Get the dialog in use, if available.
     * @return dialog instance in use
     */
    protected ApplicationDialog getDialog() {
        return dialog;
    }

    /**
     * Called to give subclasses control after a successful login.
     */
    protected void postLogin() {
    }

    /**
     * Get the "close on cancel" setting.
     * @return close on cancel
     */
    public boolean isCloseOnCancel() {
        return closeOnCancel;
    }

    /**
     * Indicates if the application should be closed if the user cancels the login
     * operation. Default is true.
     * @param closeOnCancel
     */
    public void setCloseOnCancel(boolean closeOnCancel) {
        this.closeOnCancel = closeOnCancel;
    }

    /**
     * Get the "clear password on failure" setting.
     * @return clear password
     */
    public boolean isClearPasswordOnFailure() {
        return clearPasswordOnFailure;
    }

    /**
     * Indicates if the password field should be cleared after a login failure. Default is
     * true.
     * @param clearPasswordOnFailure
     */
    public void setClearPasswordOnFailure(boolean clearPasswordOnFailure) {
        this.clearPasswordOnFailure = clearPasswordOnFailure;
    }

    /**
     * Get the default user name.
     * @return default user name.
     */
    public String getDefaultUserName() {
        return defaultUserName;
    }

    /**
     * Set the default user name.
     * @param defaultUserName to use as default, null indicates no default
     */
    public void setDefaultUserName(String defaultUserName) {
        this.defaultUserName = defaultUserName;
    }

}