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
package org.valkyriercp.command.support;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.command.SecuredActionCommandExecutor;

import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * @author Keith Donald
 */
public class AbstractActionCommandExecutor implements ParameterizableActionCommandExecutor,
        SecuredActionCommandExecutor {

    private ValueModel enabled = new ValueHolder(Boolean.FALSE);

    private boolean authorized = true;
    private boolean maskedEnabledState = false;
    private String securityControllerId = null;
    private String[] authorities;

    /**
     * Set the Id of the security controller that should manage this object.
     * @param controllerId Id (bean name) of the security controller
     */
    public void setSecurityControllerId(String controllerId) {
        this.securityControllerId = controllerId;
    }

    /**
     * Get the id (bean name) of the security controller that should manage this object.
     * @return controller id
     */
    public String getSecurityControllerId() {
        return securityControllerId;
    }

    /**
     * Set the authorized state.  Setting authorized to false will override any call
     * to {@link #setEnabled(boolean)}.  As long as this object is unauthorized,
     * it can not be enabled.
     * @param authorized Pass <code>true</code> if the object is to be authorized
     */
    public void setAuthorized( boolean authorized ) {

        if( isAuthorized() != authorized ) {

            this.authorized = authorized;

            // We need to apply a change to our enabled state depending on our
            // new authorized state.
            if( authorized ) {
                // install the last requested enabled state
                setEnabled(maskedEnabledState);
            } else {
                // Record the current enabled state and then disable
                maskedEnabledState = isEnabled();
                internalSetEnabled(false);
            }
        }
    }

    /**
     * Get the authorized state.
     * @return authorized
     */
    public boolean isAuthorized() {
        return authorized;
    }

    public boolean isEnabled() {
        return ((Boolean)enabled.getValue()).booleanValue();
    }

    /**
     * Set the enabled state of this command.  Note that if we are currently not
     * authorized, then the new value will just be recorded and no change in the
     * current enabled state will be made.
     * @param enabled state
     */
    public void setEnabled(boolean enabled) {
        maskedEnabledState = enabled;
        if( isAuthorized() ) {
            internalSetEnabled( enabled );
        }
    }

    /**
     * Internal method to set the enabled state.  This is needed so that calls
     * made to the public setEnabled and this method can be handled differently.
     * @param enabled state
     * @see #setAuthorized(boolean)
     */
    protected void internalSetEnabled( boolean enabled ) {
        this.enabled.setValue(Boolean.valueOf(enabled));
    }

    public void addEnabledListener(PropertyChangeListener listener) {
        enabled.addValueChangeListener(listener);
    }

    public void removeEnabledListener(PropertyChangeListener listener) {
        enabled.removeValueChangeListener(listener);
    }

    public void execute(Map parameters) {
        execute();
    }

    public void execute() {
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String... authorities) {
        this.authorities = authorities;
    }
}
