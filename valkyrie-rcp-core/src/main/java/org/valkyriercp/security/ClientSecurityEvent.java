package org.valkyriercp.security;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Parent for all RCP security related application events.
 *
 * @author Ben Alex
 * @author Larry Streepy
 */
public abstract class ClientSecurityEvent extends ApplicationEvent {
    /**
     * This token is used when the real authentication token is null and it needs to be
     * used as the source of an event.
     */
    public static final Authentication NO_AUTHENTICATION = new UsernamePasswordAuthenticationToken(
        "NO_AUTHENTICATION", "NO_AUTHENTICATION" );

    /**
     * Constructor. Use the given authentication token as the source of the event, if this
     * is null, then the {@link #NO_AUTHENTICATION} token is used instead.
     *
     * @param authentication token, may be null
     */
    public ClientSecurityEvent(Authentication authentication) {
        super( authentication == null ? NO_AUTHENTICATION : authentication );
    }
}
