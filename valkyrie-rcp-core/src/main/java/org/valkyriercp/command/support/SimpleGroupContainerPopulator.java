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
package org.valkyriercp.command.support;

import org.springframework.util.Assert;
import org.valkyriercp.command.GroupContainerPopulator;

import javax.swing.*;
import java.awt.*;

/**
 * A default implementation of the {@link GroupContainerPopulator} interface.
 *
 */
public class SimpleGroupContainerPopulator implements GroupContainerPopulator {

    private final Container container;

    /**
     * Creates a new {@code SimpleGroupContainerPopulator} that will populate the given container.
     *
     * @param container The container to be populated. Must not be null.
     *
     * @throws IllegalArgumentException if {@code container} is null.
     */
    public SimpleGroupContainerPopulator(Container container) {
        Assert.notNull(container, "container");
        this.container = container;
    }

    /**
     * {@inheritDoc}
     */
    public Container getContainer() {
        return container;
    }

    /**
     * {@inheritDoc}
     */
    public void add(Component c) {
        container.add(c);
    }

    /**
     * {@inheritDoc}
     */
    public void addSeparator() {
        if (container instanceof JMenu) {
            ((JMenu)container).addSeparator();
        }
        else if (container instanceof JPopupMenu) {
            ((JPopupMenu)container).addSeparator();
        }
        else if (container instanceof JToolBar) {
            ((JToolBar)container).addSeparator();
        }
        else {
            container.add(new JSeparator(SwingConstants.VERTICAL));
        }
    }

    /**
     * Default implementation, no action is performed.
     */
    public void onPopulated() {
        //do nothing
    }

}

