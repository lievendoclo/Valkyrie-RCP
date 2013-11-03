package org.valkyriercp.security;

import org.springframework.security.core.Authentication;

/**
 * Event fired when a user logs in.
 *
 * @author Ben Alex
 * @see org.springframework.security.authentication.AuthenticationEventPublisher
 */
public class LoginEvent extends ClientSecurityEvent {
    public LoginEvent(Authentication authentication) {
        super(authentication);
    }
}