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
package org.valkyriercp.command;

import java.awt.*;

/**
 * A strategy interface for adding components to an underlying {@link java.awt.Container}.
 *
 * @author Keith Donald
 *
 * @see java.awt.Container
 */
public interface GroupContainerPopulator {

    /**
     * Returns the underlying container that this instance is responsible for populating.
     *
     * @return The underlying container, never null.
     */
    public Container getContainer();

    /**
     * Adds the given component to the underlying container.
     *
     * @param component The component to be added. Must not be null.
     *
     * @throws IllegalArgumentException if {@code component} is null.
     */
    public void add(Component component);

    /**
     * Adds a separator to the underlying container.
     */
    public void addSeparator();

    /**
     * Called to perform any required actions once the container has been populated.
     */
    public void onPopulated();

}