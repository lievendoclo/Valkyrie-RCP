package org.valkyriercp.command.support;

import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import java.util.List;

/**
 * A command group member that represents a separator between other members of the group.
 */
public class SeparatorGroupMember extends GroupMember {

    /**
     * Creates a new {@code SeparatorGroupMember}.
     */
    public SeparatorGroupMember() {
        //do nothing
    }

    /**
     * Asks the given container populator to add a separator to its underlying container.
     */
    protected void fill(GroupContainerPopulator container,
                        Object factory,
                        CommandButtonConfigurer configurer,
                        List previousButtons) {
        container.addSeparator();
    }

    /**
     * Always returns false.
     * @return false always.
     */
    public final boolean managesCommand(String commandId) {
        return false;
    }

    /**
     * Default implementation, performs no operation.
     */
    public void setEnabled(boolean enabled) {
        //do nothing
    }

}

