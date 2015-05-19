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
package org.valkyriercp.security.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.core.Authorizable;
import org.valkyriercp.security.SecurityController;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract implementation of a security controller. Derived classes are responsible for
 * providing the {@link ConfigAttribute}s and any secured object that will be
 * used by the decision manager to make the decision to authorize the controlled objects.
 * <p>
 * This class uses weak references to track the the controlled objects, so they can be
 * GCed as needed.
 * <p>
 * If a subclass provides a new post-processor action, then it needs to call
 * {@link #registerPostProcessorAction(String)} during construction and it must override
 * {@link #doPostProcessorAction(String, Object, boolean)}. It is <b>critical</b> that
 * the overridden doPostProcessorAction method call
 * <code>super.doPostProcessorAction</code> for any action id it does not directly
 * handle.
 * <p>
 * This base class provides the following post-processor actions:
 * <p>
 * <b>visibleTracksAuthorized</b> - if the controlled object has a
 * <code>setVisible(boolean)</code> method then it is called with the authorized value.
 * Thus, if the object is not authorized, it will have <code>setVisible(false)</code>
 * called on it.
 *
 * @author Larry Streepy
 * @see #getSecuredObject()
 * @see #getConfigAttributeDefinition(Object)
 *
 */
public abstract class AbstractSecurityController implements SecurityController, InitializingBean {

    private final Log logger = LogFactory.getLog(getClass());

    /** The list of objects that we are controlling. */
    private List controlledObjects = new ArrayList();

    /** The AccessDecisionManager used to make the "authorize" decision. */
    private AccessDecisionManager accessDecisionManager;

    /** Last known authentication token. */
    private Authentication lastAuthentication = null;

    /** All registered post-processor action ids. */
    private HashSet postProcessorActionIds = new HashSet();

    /** Comma-separated list of post-processor actions to run. */
    private String postProcessorActionsToRun = "";

    public static final String VISIBLE_TRACKS_AUTHORIZED_ACTION = "visibleTracksAuthorized";

    /**
     * Constructor.
     */
    protected AbstractSecurityController() {
        registerPostProcessorAction( VISIBLE_TRACKS_AUTHORIZED_ACTION );
    }

    /**
     * Get the secured object on which we are making the authorization decision. This may
     * be null if no specific object is to be considered in the decision.
     * @return secured object
     */
    protected abstract Object getSecuredObject();

    /**
     * Get the ConfigAttributeDefinition for the secured object. This will provide the
     * authorization information to the access decision manager.
     * @param securedObject Secured object for whom the config attribute definition is to
     *            be rretrieved. This may be null.
     * @return attribute definition for the provided secured object
     */
    protected abstract List<ConfigAttribute> getConfigAttributeDefinition(Object securedObject);

    /**
     * Set the list of post-processor actions to be run. This must be a comma-separated
     * list of action names.
     * @param actions Comma-separated list of post-processor action names
     */
    public void setPostProcessorActionsToRun(String actions) {
        postProcessorActionsToRun = actions;
    }

    /**
     * Get the list of post-processor actions to run.
     * @return Comma-separated list of post-processor action names
     */
    public String getPostProcessorActionsToRun() {
        return postProcessorActionsToRun;
    }

    /**
     * Register a post-processor action. The action id specified must not conflict with
     * any other action registered. Subclasses that provide additional post-processor
     * actions MUST call this method to register them.
     * @param actionId Id of post-processor action to register
     */
    protected void registerPostProcessorAction(String actionId) {
        if( postProcessorActionIds.contains( actionId ) ) {
            throw new IllegalArgumentException( "Post-processor Action '" + actionId + "' is already registered" );
        }
        postProcessorActionIds.add( actionId );
    }

    /**
     * The authentication token for the current user has changed. Update all our
     * controlled objects accordingly.
     * @param authentication now in effect, may be null
     */
    public void setAuthenticationToken(Authentication authentication) {
        setLastAuthentication( authentication ); // Keep it for later
        runAuthorization();
    }

    /**
     * Update the authorization of all controlled objects.
     */
    protected void runAuthorization() {
        boolean authorize = shouldAuthorize( getLastAuthentication() );

        // Install the decision
        for( Iterator iter = controlledObjects.iterator(); iter.hasNext(); ) {
            WeakReference ref = (WeakReference) iter.next();

            Authorizable controlledObject = (Authorizable) ref.get();
            if( controlledObject == null ) {
                // Has been GCed, remove from our list
                iter.remove();
            } else {
                updateControlledObject( controlledObject, authorize );
            }
        }
    }

    /**
     * Update a controlled object based on the given authorization state.
     * @param controlledObject Object being controlled
     * @param authorized state that has been installed on controlledObject
     */
    protected void updateControlledObject(Authorizable controlledObject, boolean authorized) {
        if( logger.isDebugEnabled() ) {
            logger.debug( "setAuthorized( " + authorized + ") on: " + controlledObject );
        }
        controlledObject.setAuthorized( authorized );
        runPostProcessorActions( controlledObject, authorized );
    }

    /**
     * Run all the requested post-processor actions.
     * @param controlledObject Object being controlled
     * @param authorized state that has been installed on controlledObject
     */
    protected void runPostProcessorActions(Object controlledObject, boolean authorized) {
        String actions = getPostProcessorActionsToRun();
        if( logger.isDebugEnabled() ) {
            logger.debug( "Run post-processors actions: " + actions );
        }

        String[] actionIds = StringUtils.commaDelimitedListToStringArray(actions);
        for( int i = 0; i < actionIds.length; i++ ) {
            doPostProcessorAction( actionIds[i], controlledObject, authorized );
        }
    }

    /**
     * Post-process a controlled object after its authorization state has been updated.
     * Subclasses that override this method MUST ensure that this method is called id they
     * do not process the given action id.
     *
     * @param actionId Id of the post-processor action to run
     * @param controlledObject Object being controlled
     * @param authorized state that has been installed on controlledObject
     */
    protected void doPostProcessorAction(String actionId, Object controlledObject, boolean authorized) {
        if( VISIBLE_TRACKS_AUTHORIZED_ACTION.equals( actionId ) ) {
            setVisibilityOnControlledObject( controlledObject, authorized );
        }
    }

    /**
     * Set the visible property on a controlled action according to the provided
     * authorization.
     */
    private void setVisibilityOnControlledObject(Object controlledObject, boolean authorized) {
        try {
            Method method = controlledObject.getClass().getMethod( "setVisible", new Class[] { boolean.class } );
            method.invoke( controlledObject, new Object[] { new Boolean( authorized ) } );
        } catch( NoSuchMethodException ignored ) {
            System.out.println( "NO setVisible method on object: " + controlledObject );
            // No method to call, so nothing to do
        } catch( IllegalAccessException ignored ) {
            logger.error( "Could not call setVisible", ignored );
        } catch( InvocationTargetException ignored ) {
            logger.error( "Could not call setVisible", ignored );
        }
    }

    /**
     * Determine if our controlled objects should be authorized based on the provided
     * authentication token.
     * @param authentication token
     * @return true if should authorize
     */
    protected boolean shouldAuthorize(Authentication authentication) {
        Assert.state(getAccessDecisionManager() != null, "The AccessDecisionManager can not be null!");
        boolean authorize = false;
        try {
            if( authentication != null ) {
                Object securedObject = getSecuredObject();
                List<ConfigAttribute> cad = getConfigAttributeDefinition( securedObject );
                getAccessDecisionManager().decide( authentication, getSecuredObject(), cad );
                authorize = true;
            }
        } catch( AccessDeniedException e ) {
            // This means the secured objects should not be authorized
        }
        return authorize;
    }

    /**
     * Set the access decision manager to use
     * @param accessDecisionManager
     */
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    /**
     * Get the access decision manager in use
     */
    public AccessDecisionManager getAccessDecisionManager() {
        return accessDecisionManager;
    }

    /**
     * Set the objects that are to be controlled. Only beans that implement the
     * {@link org.springframework.security.access.event.AuthorizedEvent} interface are processed.
     * @param secured List of objects to control
     */
    public void setControlledObjects(List secured) {
        controlledObjects = new ArrayList( secured.size() );

        // Convert to weak references and validate the object types
        for( Iterator iter = secured.iterator(); iter.hasNext(); ) {
            Object o = iter.next();

            // Ensure that we got something we can control
            if( !(o instanceof Authorizable) ) {
                throw new IllegalArgumentException( "Controlled object must implement Authorizable, got "
                        + o.getClass() );
            }
            addAndPrepareControlledObject( (Authorizable) o );
        }
    }

    /**
     * Add an object to our controlled set.
     * @param object to control
     */
    public void addControlledObject(Authorizable object) {
        addAndPrepareControlledObject( object );
    }

    /**
     * Add a new object to the list of controlled objects. Install our last known
     * authorization decision so newly created objects will reflect the current security
     * state.
     * @param controlledObject to add
     */
    private void addAndPrepareControlledObject(Authorizable controlledObject) {
        controlledObjects.add( new WeakReference( controlledObject ) );

        // Properly configure the new object
        boolean authorize = shouldAuthorize( getLastAuthentication() );
        updateControlledObject( controlledObject, authorize );
    }

    /**
     * Remove an object from our controlled set.
     * @param object to remove
     * @return object removed or null if not found
     */
    public Object removeControlledObject(Authorizable object) {
        Object removed = null;

        for( Iterator iter = controlledObjects.iterator(); iter.hasNext(); ) {
            WeakReference ref = (WeakReference) iter.next();

            Authorizable controlledObject = (Authorizable) ref.get();
            if( controlledObject == null ) {
                // Has been GCed, remove from our list
                iter.remove();
            } else if( controlledObject.equals( object ) ) {
                removed = controlledObject;
                iter.remove();
            }
        }
        return removed;
    }

    protected void setLastAuthentication(Authentication authentication) {
        lastAuthentication = authentication;
    }

    protected Authentication getLastAuthentication() {
        return lastAuthentication;
    }

    /**
     * Validate our configuration.
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        // Ensure that all post-processors requested are registered
        String[] actions = StringUtils.commaDelimitedListToStringArray( getPostProcessorActionsToRun() );
        for( int i = 0; i < actions.length; i++ ) {
            if( !postProcessorActionIds.contains( actions[i] ) ) {
                throw new IllegalArgumentException( "Requested post-processor action '" + actions[i]
                        + "' is not registered." );
            }
        }
    }
}

