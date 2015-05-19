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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * This interface defines the operations required of an Application Security Manager for
 * the RCP framework. The security manager is responsible for handling login and logout
 * requests, interacting with the {@link org.springframework.security.AuthenticationManager} that
 * will perform the actual user authentication, and firing the events associated with
 * application security lifecycle. See {@link ClientSecurityEvent} and its subclasses.
 * <p>
 * The Security Manager is available as an application service via
 * {@link org.springframework.richclient.application.ApplicationServices#getSecurityManager}.
 * <p>
 * See {@link SecurityAwareConfigurer} for more details on how to configure components for
 * automatic notification of security events.
 *
 * @author Larry Streepy
 * @see AuthenticationAware
 * @see LoginAware
 * @see SecurityAwareConfigurer
 *
 */
public interface ApplicationSecurityManager {

    /**
     * Process a login attempt and fire all related events. If the authentication fails,
     * then a {@link AuthenticationFailedEvent} is published and the exception is
     * rethrown. If the authentication succeeds, then an {@link AuthenticationEvent} is
     * published, followed by a {@link LoginEvent}.
     *
     * @param authentication token to use for the login attempt
     * @return Authentication token resulting from a successful call to
     *         {@link AuthenticationManager#authenticate(org.springframework.security.Authentication)}.
     * @throws AuthenticationException If the authentication attempt fails
     */
    public Authentication doLogin(Authentication authentication) throws AuthenticationException;

    /**
     * Return if a user is currently logged in, meaning that a previous call to doLogin
     * resulted in a valid authentication request.
     * @return true if a user is logged in
     */
    public boolean isUserLoggedIn();

    /**
     * Get the authentication token for the currently logged in user.
     * @return authentication token, null if not logged in
     */
    public Authentication getAuthentication();

    /**
     * Determine if the currently authenticated user has the role provided.
     * @param role to check
     * @return true if the user has the role requested
     */
    public boolean isUserInRole(String role);

    /**
     * Perform a logout.  Set the current authentication token to null (in both the
     * per-thread security context and the global context), then publish an
     * {@link AuthenticationEvent} followed by a {@link LogoutEvent}.
     * @return Authentication token that was in place prior to the logout.
     */
    public Authentication doLogout();

    /**
     * Set the authentication manager to use.
     * @param authenticationManager instance to use for authentication requests
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager);

    /**
     * Get the authentication manager in use.
     * @return authenticationManager instance used for authentication requests
     */
    public AuthenticationManager getAuthenticationManager();

    public boolean isSecuritySupported();
}
