package org.valkyriercp.security;

import org.springframework.security.core.Authentication;

/**
 * Event fired when the user's authentication token changes.
 * <p>
 * The source of this event is the <code>Authentication</code> token returned by a
 * successful call to
 * {@link org.springframework.security.AuthenticationManager#authenticate(org.springframework.security.Authentication)}
 * or {@link ClientSecurityEvent#NO_AUTHENTICATION} if no authentication is in place (such
 * as after the user logs out).
 *
 * @author Larry Streepy
 */
public class AuthenticationEvent extends ClientSecurityEvent {

    /**
     * Constructor.
     * @param authentication new authentication token, may be null
     */
    public AuthenticationEvent(Authentication authentication) {
        super( authentication );
    }
}

