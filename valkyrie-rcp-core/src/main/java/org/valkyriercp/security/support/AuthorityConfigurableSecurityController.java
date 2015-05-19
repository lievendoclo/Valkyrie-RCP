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

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.core.AuthorityConfigurable;
import org.valkyriercp.core.Authorizable;
import org.valkyriercp.core.Secured;
import org.valkyriercp.security.SecurityController;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Class geinspireerd door
 * {@link AbstractSecurityController}.
 *
 * Verschilt in opzet doordat deze elk controlledObject bevraagt op zijn
 * Authorities om daarna deze af te checken aan de
 * {@link AccessDecisionManager}. Dit in tegenstelling tot
 * AbstractSecurityController die een set van regels afchecked voor alle
 * controlledObjects.
 *
 * @author jh
 *
 */
public class AuthorityConfigurableSecurityController implements SecurityController
{

    private final Log logger = LogFactory.getLog(getClass());

    /** The list of objects that we are controlling. */
    private List<WeakReference<Authorizable>> controlledObjects = new ArrayList<WeakReference<Authorizable>>();

    /** The AccessDecisionManager used to make the "authorize" decision. */
    private AccessDecisionManager accessDecisionManager;

    /** Last known authentication token. */
    private Authentication lastAuthentication = null;

    /** Specific configAttributeDefinition overriding others. */
    private  List<ConfigAttribute> configAttributeDefinition;

    /** Id bound configAttributeDefinitions. */
    private Map<String,  List<ConfigAttribute>> idConfigAttributeDefinitionMap = Maps.newHashMap();

    /**
     * Last authentication token = currently used.
     */
    protected void setLastAuthentication(Authentication authentication)
    {
        lastAuthentication = authentication;
    }

    /**
     * Each entry of the idConfigAttributeDefinitionMap contains a SecurityControllerId
     * together with its actionClusters. When using this SecurityController as
     * FallBack, this map can be used to configure commands that are
     * declared/used in code.
     *
     * @param idAuthorityMap
     */
    public void setIdAuthorityMap(Map<String, String> idAuthorityMap)
    {
        idConfigAttributeDefinitionMap = new HashMap<String, List<ConfigAttribute>>(idAuthorityMap
                .size());
        for (Map.Entry<String, String> entry : idAuthorityMap.entrySet())
        {
            idConfigAttributeDefinitionMap.put(entry.getKey(), SecurityConfig.createListFromCommaDelimitedString(entry.getValue()));
        }
    }

    /**
     * Last authentication token = currently used.
     */
    protected Authentication getLastAuthentication()
    {
        return lastAuthentication;
    }

    /**
     * Add an object to our controlled set.
     *
     * @param object
     *            to control
     */
    public void addControlledObject(Authorizable object)
    {
        addAndPrepareControlledObject(object);
    }

    /**
     * Add a new object to the list of controlled objects. Install our last
     * known authorization decision so newly created objects will reflect the
     * current security state.
     *
     * @param controlledObject
     *            to add
     */
    private void addAndPrepareControlledObject(Authorizable controlledObject)
    {
        controlledObjects.add(new WeakReference<Authorizable>(controlledObject));

        // Properly configure the new object
        boolean authorize = shouldAuthorize(getLastAuthentication(), controlledObject);
        updateControlledObject(controlledObject, authorize);
    }

    /**
     * Update a controlled object based on the given authorization state.
     *
     * @param controlledObject
     *            Object being controlled
     * @param authorized
     *            state that has been installed on controlledObject
     */
    protected void updateControlledObject(Authorizable controlledObject, boolean authorized)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("setAuthorized( " + authorized + ") on: " + controlledObject);
        }
        controlledObject.setAuthorized(authorized);
    }

    /**
     * Determine if our controlled objects should be authorized based on the
     * provided authentication token.
     *
     * @param authentication
     *            token
     * @return true if should authorize
     */
    protected boolean shouldAuthorize(Authentication authentication, Authorizable controlledObject)
    {
        Assert.state(getAccessDecisionManager() != null, "The AccessDecisionManager can not be null!");
        boolean authorize = false;
        try
        {
            if (authentication != null)
            {
                List<ConfigAttribute> cad = getConfigAttributeDefinition(controlledObject);
                if (cad != null)
                {
                    getAccessDecisionManager().decide(authentication, null, cad);
                }
                authorize = true;
            } else {
                // authentication must be disabled, going through
                authorize = true;
            }
        }
        catch (AccessDeniedException e)
        {
            authorize = false;
            // This means the secured objects should not be authorized
        }
        return authorize;
    }

    /**
     * Get the ConfigAttributeDefinitions with following strategy:
     *
     * <ol>
     * <li>Check the property configAttributeDefinition. If set, this one
     * overrides all others.</li>
     * <li>Check the configAttributeDefinitionMap. Use the securityControllerId
     * of the object to find the appropriate configs. When this
     * SecurityController is set up as fallback, the securityControllerId of
     * Authorizable objects can be used to link them to a particular
     * ActionCluster.</li>
     * <li>Check if the controlledObject is actually an
     * ActionClusterConfigurable object. If it is, it probably has it's own set
     * of ActionClusters defined, so return a configAttributeDefinition object
     * conform to this property.
     * </ol>
     *
     */
    private List<ConfigAttribute> getConfigAttributeDefinition(Authorizable controlledObject)
    {
        // first check on global override
        if (configAttributeDefinition != null)
            return configAttributeDefinition;

        if (controlledObject instanceof Secured)
        {
            Secured securedObject = (Secured) controlledObject;
            if(securedObject.getSecurityControllerId() != null) {
                String securityControllerId = securedObject.getSecurityControllerId();
                List<ConfigAttribute> cad = idConfigAttributeDefinitionMap.get(securityControllerId);
                if (cad != null)
                     return cad;
            }
            if(securedObject.getAuthorities() != null) {
                return SecurityConfig.createList(((AuthorityConfigurable) controlledObject).getAuthorities());
            }
        } else if(controlledObject instanceof FormModel) {
            FormModel formModel = (FormModel) controlledObject;
            if(formModel.getId() != null) {
                String securityControllerId = formModel.getId() + ".edit";
                List<ConfigAttribute> cad = idConfigAttributeDefinitionMap.get(securityControllerId);
                if (cad != null)
                    return cad;
            }
        }
        return null;
    }

    public void setConfigAttributeDefinition(List<ConfigAttribute> configAttributeDefinition)
    {
        this.configAttributeDefinition = configAttributeDefinition;
    }

    /**
     * Set the access decision manager to use
     *
     * @param accessDecisionManager
     */
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager)
    {
        this.accessDecisionManager = accessDecisionManager;
    }

    /**
     * Get the access decision manager in use
     */
    public AccessDecisionManager getAccessDecisionManager()
    {
        return accessDecisionManager;
    }

    /**
     * Remove an object from our controlled set.
     *
     * @param object
     *            to remove
     * @return object removed or null if not found
     */
    public Object removeControlledObject(Authorizable object)
    {
        Object removed = null;

        for (Iterator iter = controlledObjects.iterator(); iter.hasNext();)
        {
            WeakReference ref = (WeakReference) iter.next();

            Authorizable controlledObject = (Authorizable) ref.get();
            if (controlledObject == null)
            {
                // Has been GCed, remove from our list
                iter.remove();
            }
            else if (controlledObject.equals(object))
            {
                removed = controlledObject;
                iter.remove();
            }
        }
        return removed;
    }

    /**
     * Set the objects that are to be controlled. Only beans that implement the
     * {@link org.springframework.security.access.event.AuthorizedEvent} interface are processed.
     *
     * @param secured
     *            List of objects to control
     */
    public void setControlledObjects(List secured)
    {
        controlledObjects = new ArrayList<WeakReference<Authorizable>>(secured.size());

        // Convert to weak references and validate the object types
        for (Iterator iter = secured.iterator(); iter.hasNext();)
        {
            Object o = iter.next();

            // Ensure that we got something we can control
            if (!(o instanceof Authorizable))
            {
                throw new IllegalArgumentException("Controlled object must implement Authorizable, got "
                        + o.getClass());
            }
            addAndPrepareControlledObject((Authorizable) o);
        }
    }

    public void setAuthenticationToken(Authentication authentication)
    {
        setLastAuthentication(authentication); // Keep it for later
        runAuthorization();
    }

    /**                          securityAwareConfigurer
     * Update the authorization of all controlled objects.
     */
    protected void runAuthorization()
    {
        // Install the decision
        for (Iterator iter = controlledObjects.iterator(); iter.hasNext();)
        {
            WeakReference ref = (WeakReference) iter.next();

            Authorizable controlledObject = (Authorizable) ref.get();
            if (controlledObject == null)
            {
                // Has been GCed, remove from our list
                iter.remove();
            }
            else
            {
                updateControlledObject(controlledObject, shouldAuthorize(getLastAuthentication(),
                        controlledObject));
            }
        }
    }
}

