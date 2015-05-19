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
 * change in the user's Authentication token. This would happen after various security
 * related operations like login and logout. Any bean implementing this interface will
 * have {@link #setAuthenticationToken(Authentication)} called when it is created and
 * subsequently whenever the user's authentication token changes.
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
 * @see org.springframework.security.AuthenticationManager#authenticate(org.springframework.security.Authentication)
 * @see org.springframework.context.ApplicationListener
 * @see LoginAware
 * @see ClientSecurityEvent
 * @see SecurityAwareConfigurer
 */
public interface AuthenticationAware {

    /**
     * Notifies listener of the new (current) Authentication token for the user. This may
     * be null if the authentication state is unknown (as it may be when the Application
     * context is being constructed) or after a user logs out.
     * @param authentication token
     */
    public void setAuthenticationToken(Authentication authentication);
}

