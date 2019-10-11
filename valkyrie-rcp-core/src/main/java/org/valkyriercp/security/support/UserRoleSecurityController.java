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

import com.google.common.collect.Lists;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * This class controls the authorization of other objects, that implement the
 * {@link org.valkyriercp.core.Authorizable}, according to the roles held
 * by the currently authenticated user. If the current user holds one or more of the
 * configured roles, then the associated objects are authorized. Otherwise, they are not
 * authorized.
 * <p>
 * The roles on which to authorize the controlled objects are specified via the
 * <code>roles</code> property. It should be a comma-separated list of role names.
 * <p>
 * No secured object is used in preparing the ConfigAttributeDefinition. This means that
 * the configuration is not specific to a given object (no per-object ACL's). The access
 * decision is made strictly on the roles held by the user.
 * <p>
 * The default access decision manager is an {@link AffirmativeBased} instance using a
 * plain {@link RoleVoter}. You can override this by setting the
 * <code>accessDecisionManager</code> property.
 * <p>
 * Below is an example configuration for this class:
 *
 * <pre>
 *   &lt;bean id=&quot;adminController&quot;
 *         class=&quot;org.springframework.richclient.security.support.UserRoleSecurityController&quot;&gt;
 *       &lt;property name=&quot;authorizingRoles&quot; value=&quot;ROLE_ADMIN&quot; /&gt;
 *   &lt;/bean&gt;
 *
 *   &lt;bean id=&quot;writeController&quot;
 *         class=&quot;org.springframework.richclient.security.support.UserRoleSecurityController&quot;&gt;
 *       &lt;property name=&quot;authorizingRoles&quot; value=&quot;ROLE_WRITE,ROLE_ADMIN&quot; /&gt;
 *   &lt;/bean&gt;
 *
 * </pre>
 *
 * @author Larry Streepy
 *
 */
public class UserRoleSecurityController extends AbstractSecurityController {

    /** The ConfigAttributeDefinition controlling our authorization decision. */
    private List<ConfigAttribute> roles = null;

    /** Roles as a string. */
    private String rolesString;

    /**
     * Constructor.
     */
    public UserRoleSecurityController() {
        // Install the default decision manager
        List<AccessDecisionVoter<?>> voters = Lists.newArrayList();
        voters.add(new RoleVoter());
        AffirmativeBased adm = new AffirmativeBased(voters);
        setAccessDecisionManager( adm );
    }

    /**
     * Set the roles to compare against the current user's authenticated roles. The
     * secured objects will be authorized if the user holds one or more of these roles.
     * This should be specified as a simple list of comma separated role names.
     * @param roles
     */
    public void setAuthorizingRoles(String roles) {
        // The ConfigAttributeEditor is named incorrectly, so you can't use it
        // to automatically convert the string to a ConfigAttributeDefinition.
        // So, we do it manually :-(
        this.roles = SecurityConfig.createList(StringUtils.commaDelimitedListToStringArray(roles));
        rolesString = roles;
    }

    /**
     * Get the config attributes.
     * @return config attrributes
     */
    public String getAuthorizingRoles() {
        return rolesString;
    }

    /**
     * Get the secured object on which we are making the authorization decision. We return
     * null as no specific object is to be considered in the decision.
     * @return secured object
     */
    protected Object getSecuredObject() {
        return null;
    }

    /**
     * Get the ConfigAttributeDefinition for the secured object. This will provide the
     * authorization infoConfigAttributeDefinitionrmation to the access decision manager.
     * @param securedObject Secured object for whom the config attribute definition is to
     *            be retrieved. This may be null.
     * @return attribute definition for the provided secured object
     */
    protected List<ConfigAttribute> getConfigAttributeDefinition(Object securedObject) {
        return roles;
    }

}

