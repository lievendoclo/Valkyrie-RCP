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
 * Interface to be implemented by objects that can be enabled or disabled. For example, these
 * objects will typically be user interface controls that the application will want
 * to disable in certain contexts.
 *
 * @author Keith Donald
 */
public interface Guarded {

    /**
     * Indicates if the object is in an enabled state.
     * @return {@true} if the object is in an enabled state, {@false} otherwise.
     */
    public boolean isEnabled();

    /**
     * Sets the enabled state of the object.
     * @param enabled {@code true} to enable the object, {@code false} to disable it.
     */
    public void setEnabled(boolean enabled);

}