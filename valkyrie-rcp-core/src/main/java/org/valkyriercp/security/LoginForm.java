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
package org.valkyriercp.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.builder.TableFormBuilder;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;

/**
 * This class provides a simple form for capturing a username and password from the user.
 * It also generates an {@link Authentication} token from the entered values.
 *
 * @author Larry Streepy
 * @see #getAuthentication()
 */
public class LoginForm extends AbstractForm {
    private JComponent usernameField;
    private JComponent passwordField;

    /**
     * Set the user name in the form.
     * @param userName to install
     */
    public void setUserName(String userName) {
        getValueModel( LoginDetails.PROPERTY_USERNAME, String.class ).setValue( userName );
    }

    /**
     * Set the password in the form.
     * @param password to install
     */
    public void setPassword(String password) {
        getValueModel( LoginDetails.PROPERTY_PASSWORD, String.class ).setValue( password );
    }

    /**
     * Get an Authentication token that contains the current username and password.
     * @return authentication token
     */
    public Authentication getAuthentication() {
        String username = getValueModel(LoginDetails.PROPERTY_USERNAME, String.class).getValue();
        String password = getValueModel(LoginDetails.PROPERTY_PASSWORD, String.class).getValue();
        return new UsernamePasswordAuthenticationToken( username, password );
    }

    @Override
    public FormModel createFormModel() {
        return ValkyrieRepository.getInstance().getApplicationConfig().formModelFactory().createUnbufferedFormModel(new LoginDetails(), "credentials");
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
        String username = getValueModel(LoginDetails.PROPERTY_USERNAME, String.class).getValue();
        JComponent field = (username != null && username.length() > 0) ? passwordField : usernameField;
        return field.requestFocusInWindow();
    }

}