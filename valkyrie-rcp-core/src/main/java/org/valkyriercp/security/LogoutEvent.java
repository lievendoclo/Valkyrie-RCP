package org.valkyriercp.security;

import org.springframework.security.core.Authentication;

/**
 * Event fired when a user logs out.
 * <p>
 * The old <code>Authentication</code> token (if any) is provided as the
 * event source. If no existing <code>Authentication</code> token is
 * available, then {@link ClientSecurityEvent#NO_AUTHENTICATION} object will be used.
 *
 * @author Ben Alex
 */
public class LogoutEvent extends ClientSecurityEvent {
    public LogoutEvent(Authentication authentication) {
        super(authentication);
    }
}
