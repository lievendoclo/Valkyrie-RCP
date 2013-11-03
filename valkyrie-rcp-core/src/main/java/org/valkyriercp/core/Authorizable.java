package org.valkyriercp.core;

/**
 * Interface to be implemented by objects whose operations can be authorized or not.
 *
 * @author Larry Streepy
 */
public interface Authorizable {

    /** Property notified on change. */
    public static final String AUTHORIZED_PROPERTY = "authorized";

    /**
     * Set the authorized state.
     * @param authorized Pass <code>true</code> if the object is to be authorized
     */
    public void setAuthorized( boolean authorized );

    /**
     * Get the authorized state.
     * @return authorized
     */
    public boolean isAuthorized();
}
