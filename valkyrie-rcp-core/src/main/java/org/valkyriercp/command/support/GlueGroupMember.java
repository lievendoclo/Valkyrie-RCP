package org.valkyriercp.command.support;

import org.valkyriercp.command.GroupContainerPopulator;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import javax.swing.*;
import java.util.List;

/**
 * A member of a {@link CommandGroup} that represents a 'glue' component between other members
 * of the group.
 *
 * <p>
 * A glue component is most often used as a filler between other components in a layout where those
 * components cannot expand beyound a maximum height or width. As the layout area expands, the glue
 * component will expand to take up the space.
 * </p>
 *
 * @see javax.swing.Box#createGlue()
 */
public class GlueGroupMember extends GroupMember {

    /**
     * Creates a new uninitialized {@code GlueGroupMember}.
     */
    public GlueGroupMember() {
        //do nothing
    }

    /**
     * Adds a glue component using the given container populator.
     *
     * {@inheritDoc}
     */
    protected void fill(GroupContainerPopulator parentContainer,
                        Object factory,
                        CommandButtonConfigurer configurer,
                        List previousButtons) {
        parentContainer.add(Box.createGlue());
    }

    /**
     * Always returns false. A glue group member does not manage any commands.
     * @return false always.
     */
    public final boolean managesCommand(String commandId) {
        return false;
    }

    /**
     * Default implemenation, performs no operation.
     */
    public void setEnabled(boolean enabled) {
        // do nothing
    }

}
