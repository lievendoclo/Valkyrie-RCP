package org.valkyriercp.security;

import org.valkyriercp.core.Authorizable;

import java.util.Map;

/**
 * A SecurityControllerManager is responsible for linking a controllable object, one that
 * implements {@link org.springframework.richclient.core.SecurityControllable}, to the
 * appropriate {@link SecurityController} to manage it.
 * <p>
 * The SecurityControllable object will provide a controller Id that needs to be mapped to
 * a specific controller. See {@link SecurityControllable#getSecurityControllerId()}.
 * This id will then be used in a call to {@link #getSecurityController(String)} to find
 * the registered security controller.
 * <p>
 * Each security controller is implicitly registered under its bean context id. Aliases
 * may be registered by calling
 * {@link #registerSecurityControllerAlias(String, SecurityController)}. Subsequently,
 * any call to <code>getSecurityController</code> with that alias id will return the
 * registered security controller. This is useful for mapping generated command security
 * controller ids (which are often a combination of a form id and a command face id) to
 * the actual security controller that should manage the command. This provides a
 * declarative model for linking commands to controllers instead of requiring the
 * subclassing a Form in order to specify the command's security controller id.
 *
 * @author Larry Streepy
 * @see org.springframework.richclient.security.support.DefaultSecurityControllerManager
 *
 */
public interface SecurityControllerManager {

    /**
     * Set the map of controller Ids to controller instances.
     * @param map keyed by controller Id, value is {@link SecurityController} instance
     */
    public void setSecurityControllerMap(Map map);

    /**
     * Register an alias for a SecurityController.
     * @param aliasId to register
     * @param securityController to register under given alias Id
     */
    public void registerSecurityControllerAlias(String aliasId, SecurityController securityController);

    /**
     * Get the security controller for a given Id.
     * @param id of security controller
     * @return controller instance, or null if nothing is registered for the id
     */
    public SecurityController getSecurityController(String id);

    public void addSecuredObject(Authorizable auth);

    public void addSecuredObject(String securityControllerId, Authorizable auth);
}
