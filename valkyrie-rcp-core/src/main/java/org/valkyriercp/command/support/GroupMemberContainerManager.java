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
