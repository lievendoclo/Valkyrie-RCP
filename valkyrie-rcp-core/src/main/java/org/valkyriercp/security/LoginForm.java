package org.valkyriercp.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.builder.TableFormBuilder;

import javax.swing.*;

/**
 * This class provides a simple form for capturing a username and password from the user.
 * It also generates an {@link Authentication} token from the entered values.
 *
 * @author Larry Streepy
 * @see #getAuthentication()
 */
public class LoginForm extends AbstractForm {
    private static final String FORM_ID = "credentials";

    private LoginDetails loginDetails;

    private JComponent usernameField;
    private JComponent passwordField;

    /**
     * Constructor.
     */
    public LoginForm() {
        setId(FORM_ID);
        loginDetails = createLoginDetails();
    }

    /**
     * Set the user name in the form.
     * @param userName to install
     */
    public void setUserName(String userName) {
        if( isControlCreated() ) {
            getValueModel( LoginDetails.PROPERTY_USERNAME ).setValue( userName );
        } else {
            loginDetails.setUsername( userName );
        }
    }

    /**
     * Set the password in the form.
     * @param password to install
     */
    public void setPassword(String password) {
        if( isControlCreated() ) {
            getValueModel( LoginDetails.PROPERTY_PASSWORD ).setValue( password );
        } else {
            loginDetails.setPassword( password );
        }
    }

    /**
     * Get an Authentication token that contains the current username and password.
     * @return authentication token
     */
    public Authentication getAuthentication() {
        String username = loginDetails.getUsername().trim();
        String password = loginDetails.getPassword().trim();
        return new UsernamePasswordAuthenticationToken( username, password );
    }

    /**
     * Create the form object to hold our login information.
     * @return constructed form object
     */
    protected LoginDetails createLoginDetails() {
        return new LoginDetails();
    }

    @Override
    public FormModel createFormModel() {
        return formModelFactory.createUnbufferedFormModel(loginDetails);
    }

    /**
     * Construct the form with the username and password fields.
     */
    protected JComponent createFormControl() {
        TableFormBuilder formBuilder = new TableFormBuilder( getBindingFactory() );
        usernameField = formBuilder.add( LoginDetails.PROPERTY_USERNAME )[1];
        formBuilder.row();
        passwordField = formBuilder.addPasswordField( LoginDetails.PROPERTY_PASSWORD )[1];
        return formBuilder.getForm();
    }

    public boolean requestFocusInWindow() {
        // Put the focus on the right field
        String username = loginDetails.getUsername();
        JComponent field = (username != null && username.length() > 0) ? passwordField : usernameField;
        return field.requestFocusInWindow();
    }

}