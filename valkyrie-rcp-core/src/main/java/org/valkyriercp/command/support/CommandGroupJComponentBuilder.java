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


