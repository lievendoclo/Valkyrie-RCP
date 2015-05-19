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
