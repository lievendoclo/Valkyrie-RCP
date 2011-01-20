package org.valkyriercp.command.support;

import javax.swing.*;

/**
 * CommandGroupComponentBuilder is a s-pecial case of the
 * {@link CommandGroupModelBuilder}that
 * is specifically designed to build swing GUI components based on the passed in
 * command-group.
 *
 * Basically it wraps the buildXXXModel method hierarchy of the
 * {@link CommandGroupModelBuilder}into
 * a buildXXXComponent structure that does the necessary typecasting allong the
 * way.
 *
 * @see CommandGroupModelBuilder
 */
public abstract class CommandGroupJComponentBuilder extends CommandGroupModelBuilder
{

    /**
     * JComponent-building variant of the
     * {@link CommandGroupModelBuilder#buildModel(CommandGroup)}
     */
    public JComponent buildComponent(CommandGroup commandGroup)
    {
        return (JComponent) buildModel(commandGroup);
    }

    /**
     * JComponent-building variant of the
     * {@link CommandGroupModelBuilder#buildRootModel(CommandGroup)}
     */
    protected abstract JComponent buildRootComponent(AbstractCommand command);

    /**
     * JComponent-building variant of the
     * {@link CommandGroupModelBuilder#buildChildModel(Object, AbstractCommand, int)}
     */
    protected abstract JComponent buildChildComponent(JComponent parentComponent, AbstractCommand command,
            int level);

    /**
     * JComponent-building variant of the
     * {@link CommandGroupModelBuilder#buildGroupModel(Object, CommandGroup, int)}
     */
    protected abstract JComponent buildGroupComponent(JComponent parentComponent, CommandGroup command,
            int level);

    /**
     * Implementation wrapping around the
     * {@link #buildRootComponent(AbstractCommand)}
     */
    protected final Object buildRootModel(CommandGroup commandGroup)
    {
        return buildRootComponent(commandGroup);
    }

    /**
     * Implementation wrapping around the
     * {@link #buildGroupComponent(JComponent, CommandGroup, int)}
     */
    protected final Object buildGroupModel(Object parentModel, CommandGroup commandGroup, int level)
    {
        return buildGroupComponent((JComponent) parentModel, commandGroup, level);
    }

    /**
     * Implementation wrapping around the
     * {@link #buildChildComponent(JComponent, AbstractCommand, int)}
     */
    protected final Object buildChildModel(Object parentModel, AbstractCommand command, int level)
    {
        return buildChildComponent((JComponent) parentModel, command, level);
    }
}


