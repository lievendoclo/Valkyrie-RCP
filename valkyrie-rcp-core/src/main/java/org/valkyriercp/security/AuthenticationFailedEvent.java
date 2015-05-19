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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Event fired when an authentication attempt fails.
 * <p>
 * The source of this event is the <code>Authentication</code> token that resulted in
 * the authentication failure. This event also carries the exception that indicated the
 * failure, {@see #getCause()}.
 *
 * @author Larry Streepy
 */
public class AuthenticationFailedEvent extends ClientSecurityEvent {

    private AuthenticationException cause;

    /**
     * Constructor. Use the given authentication token as the source of the event.
     * @param authentication token
     * @param cause The exception that caused the login failure
     */
    public AuthenticationFailedEvent(Authentication authentication, AuthenticationException cause) {
        super( authentication );
        setCause( cause );
    }

    /**
     * Get the exception that caused the login failure
     * @return causing exception
     */
    public AuthenticationException getCause() {
        return cause;
    }

    /**
     * Set the exception that caused this login failure.
     * @param cause
     */
    public void setCause(AuthenticationException cause) {
        this.cause = cause;
    }
}

