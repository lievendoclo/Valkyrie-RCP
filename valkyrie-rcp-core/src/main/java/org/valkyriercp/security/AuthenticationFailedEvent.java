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

