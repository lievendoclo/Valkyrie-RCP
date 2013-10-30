package org.valkyriercp.security.support;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.valkyriercp.core.Authorizable;
import org.valkyriercp.security.SecurityController;
import org.valkyriercp.security.SecurityControllerManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSecurityControllerManager implements SecurityControllerManager {

    private Map securityControllerMap = new HashMap();
    private SecurityController fallbackController = null;

    @Autowired
    private ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * @see org.springframework.richclient.security.SecurityControllerManager#setSecurityControllerMap(java.util.Map)
     */
    public void setSecurityControllerMap(Map map) {
        for( Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            registerSecurityControllerAlias( (String) entry.getKey(), (SecurityController) entry.getValue() );
        }
    }

    /**
     * Get the security controller for the given id. If the id is registered in our map,
     * then return the registered controller. If not, then try to obtain a bean with the
     * given id if it implements the SecurityController interface.
     * @param id of controller to retrieve
     * @return controller, null if not found
     */
    public SecurityController getSecurityController(String id) {
        SecurityController sc = (SecurityController) securityControllerMap.get( id );
        if( sc == null ) {
            // Try for a named bean
            try {
                sc = applicationContext.getBean( id,
                    SecurityController.class );
            } catch( NoSuchBeanDefinitionException e ) {
                // Try for a fallback
                sc = getFallbackSecurityController();
            }
        }
        return sc;
    }

    @Override
    public void addSecuredObject(Authorizable auth) {
        fallbackController.addControlledObject(auth);
    }

    @Override
    public void addSecuredObject(String securityControllerId, Authorizable auth) {
        getSecurityController(securityControllerId).addControlledObject(auth);
    }

    /**
     * Register an alias for a SecurityController.
     * @param aliasId to register
     * @param securityController to register under given alias Id
     */
    public void registerSecurityControllerAlias(String aliasId, SecurityController securityController) {
        securityControllerMap.put( aliasId, securityController );
    }

    /**
     * Set the fallback security controller. This controller will be returned for any
     * requested controller Id that is not found in the registry.
     * @param fallbackController
     */
    public void setFallbackSecurityController(SecurityController fallbackController) {
        this.fallbackController = fallbackController;
    }

    /**
     * Get the fallback security controller, if any.
     * @return fallback security controller, null if not defined
     */
    public SecurityController getFallbackSecurityController() {
        return fallbackController;
    }
}

