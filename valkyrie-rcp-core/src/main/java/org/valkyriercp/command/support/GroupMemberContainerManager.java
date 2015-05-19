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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

class GroupMemberContainerManager {
    private static final Log logger = LogFactory.getLog(GroupMemberContainerManager.class);

    private GroupContainerPopulator containerPopulator;

    private Object factory;

    private CommandButtonConfigurer configurer;

    public GroupMemberContainerManager(GroupContainerPopulator containerPopulator, Object factory,
            CommandButtonConfigurer configurer) {
        this.containerPopulator = containerPopulator;
        this.factory = factory;
        this.configurer = configurer;
    }

    public void setVisible(boolean visible) {
        containerPopulator.getContainer().setVisible(visible);
    }

    public void rebuildControlsFor(Collection members) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rebuilding group member controls; members=" + members);
        }
        Component[] components = containerPopulator.getContainer().getComponents();
        java.util.List previousButtons = new ArrayList(components.length);
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof AbstractButton) {
                previousButtons.add(components[i]);
            }
        }
        containerPopulator.getContainer().removeAll();
        for (Iterator iterator = members.iterator(); iterator.hasNext();) {
            GroupMember member = (GroupMember)iterator.next();
            member.fill(containerPopulator, factory, configurer, previousButtons);
        }
        containerPopulator.onPopulated();
        containerPopulator.getContainer().validate();
        containerPopulator.getContainer().repaint();
        if (logger.isDebugEnabled()) {
            logger.debug("Rebuild complete; container control count = "
                    + containerPopulator.getContainer().getComponentCount());
        }
    }

}
