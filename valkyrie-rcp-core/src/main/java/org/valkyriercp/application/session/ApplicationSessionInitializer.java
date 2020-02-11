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
package org.valkyriercp.application.session;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApplicationSessionInitializer
{

    /**
     * Extra user attributes to be added to the ApplicationSession after login
     */
    private Map<String, Object> userAttributes;

    /**
     * Extra session attributes to be added to the ApplicationSession after login
     */
    private Map<String, Object> sessionAttributes;

    /**
     * List of command ids to be executed before startup of the application window
     */
    private List<String> preStartupCommands;

     /**
     * List of command ids to be executed after startup of the application window
     */
    private List<String> postStartupCommands;

    /**
     * Sets extra user attributes to be added to the ApplicationSession after login
     */
    public void setUserAttributes(Map<String, Object> attributes)
    {
        this.userAttributes = attributes;
    }

    /**
     * @return extra user attributes to be added to the ApplicationSession after login
     */
    public Map<String, Object> getUserAttributes()
    {
        return userAttributes;
    }

    /**
     * Sets extra session attributes to be added to the ApplicationSession after login
     */
    public void setSessionAttributes(Map<String, Object> attributes)
    {
        this.sessionAttributes = attributes;
    }

    /**
     * @return extra session attributes to be added to the ApplicationSession after login
     */
    public Map<String, Object> getSessionAttributes()
    {
        return sessionAttributes;
    }

     /**
     * Sets the list of command ids to be executed before startup of the application window
     */
    public void setPreStartupCommandIds(List<String> commandIds)
    {
        this.preStartupCommands = commandIds;
    }

    /**
     * Sets the list of command ids to be executed before startup of the application window
     */
    public void setPreStartupCommandIds(String... commandIds)
    {
        this.preStartupCommands = Arrays.asList(commandIds);
    }

    /**
     * @return the list of command ids to be executed before startup of the application window
     */
    public List<String> getPreStartupCommands()
    {
        return preStartupCommands;
    }

    /**
     * Sets the list of command ids to be executed after startup of the application window
     */
    public void setPostStartupCommands(List<String> commandIds)
    {
        this.postStartupCommands = commandIds;
    }

     /**
     * @return the list of command ids to be executed after startup of the application window
     */
    public List<String> getPostStartupCommands()
    {
        return postStartupCommands;
    }

    /**
     * Hook that is called before the session attributes are retrieved. Here you can
     * set session attributes in code.
     */
    public void initializeSession()
    {
    }

    /**
     * Hook that is called before the user attributes are retrieved. Here you can
     * set user attributes in code.
     */
    public void initializeUser()
    {
    }
}
