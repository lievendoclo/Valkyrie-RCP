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

/**
 * A Spring managed bean implementing this interface will be automatically notified of any
 * user login or logout activities. Any bean implementing this interface will have
 * {@link #userLogin(Authentication)} or {@link #userLogout(Authentication)}, called when
 * the user performs a login or logout activity, respectively.
 * <p>
 * In order for this notification to take place, a singleton, non-lazy instance of
 * {@link SecurityAwareConfigurer} must be defined in the Spring ApplicationContext. See
 * {@link SecurityAwareConfigurer} for configuration details.
 * <p>
 * If a class needs to track the actual security event lifecycle, then it should implement
 * {@link org.springframework.context.ApplicationListener} and watch for instances of
 * {@link org.springframework.richclient.security.ClientSecurityEvent} events.
 * <p>
 * @author Larry Streepy
 *
 * @see org.springframework.context.ApplicationListener
 * @see AuthenticationAware
 * @see ClientSecurityEvent
 * @see SecurityAwareConfigurer
 */
public interface LoginAware {

    /**
     * Called when a user has successfully logged in. The authentication token is the token
     * returned from the authenticate method call.
     * @param authentication token
     */
    public void userLogin(Authentication authentication);

    /**
     * Called when a user has logged out.
     * @param authentication token in place prior to the logout
     */
    public void userLogout(Authentication authentication);
}

