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

import java.util.Iterator;

/**
 * CommandGroupModelBuilder is a helper class that allows to build Object Models
 * derived from ready command-group structures.
 *
 * These command-group structures are tree-like structures that this class will
 * traverse. Actual building of specific matching object models (often also
 * trees) is done through callbacks to specific buildXXXModel methods. (which
 * are abstract on this generic traversing base-class)
 *
 * Actual use assumes one sublasses and implements those required abstract
 * methods.
 *
 * Internally this class will traverse the commandGroup-structure and offer
 * subclasses the opportunity to build up his matching model matching the
 * command-group nesting by calling the various buildXXXModel methods.
 */
public abstract class CommandGroupModelBuilder
{

    /**
     * Builds the real root object that will be returned from
     * {@link #buildModel(CommandGroup)}.
     *
     * @param commandGroup
     *            at the root of the structure
     * @return the top level object model to build.
     */
    protected abstract Object buildRootModel(CommandGroup commandGroup);

    /**
     * Allows the implementation subclass to decide (by overriding) if
     * traversing the structure should continue deeper down.
     *
     * Implementations can decide based on the current visited commandGroup and
     * the level information.
     *
     * Default implementation at this abstract class level, is to keep on going
     * down the structure. (subclasses should only override when needing to
     * change that.)
     *
     * @param commandGroup
     *            currently visited.
     * @param level
     *            in the structure we are at ATM
     * @return <code>true</code> if children of the group should be visted,
     *         <code>false</code> if not.
     */
    protected boolean continueDeeper(CommandGroup commandGroup, int level)
    {
        return true;
    }

    /**
     * Allows the implementation subclass to build a mapping object-model
     * corresponding to a visited leaf node in the command-group structure.
     * <i>(Note: for non-leaf nodes the
     * {@link #buildGroupModel(Object, CommandGroup, int) version is called)}</i>
     *
     * Since the parentModel is also passed in, the implementation can use it to
     * downcast that and possibly hook up the new client-structure.
     *
     * @param parentModel
     *            the objectmodel that was created for the parent-command.
     * @param command
     *            currently visited command in the structure.
     * @param level
     *            in the structure we are at ATM
     * @return the top level object model to build.
     */
    protected abstract Object buildChildModel(Object parentModel, AbstractCommand command, int level);

    /**
     * Allows the implementation subclass to build a mapping object-model
     * corresponding to a visited non-leaf node in the command-group structure.
     * <i>(Note: for leaf nodes the
     * {@link #buildChildModel(Object, AbstractCommand, int)}  version is called)}</i>
     *
     * Since the parentModel is also passed in, this implementation can use it
     * to downcast and decide to hook up the new client-structure.
     *
     * In a same manner the object-structure returned by this method will be
     * passed down in the tree as the parentModel for nested nodes.
     *
     * In general, if an implementation subclass is not building extra stuff for
     * a particular command at a particular level, then it is generally wise to
     * just pass down the parentModel.
     *
     * @param parentModel
     *            the objectmodel that was created for the parent-command.
     * @param commandGroup
     *            currently visited command in the structure.
     * @param level
     *            in the structure we are at ATM
     * @return the top level object model to build.
     */
    protected abstract Object buildGroupModel(Object parentModel, CommandGroup commandGroup, int level);

    /**
     * Main service method of this method to call.
     *
     * This builds the complete mapping object-model by traversing the complete
     * passed in command-group structure by performing the appropriate callbacks
     * to the subclass implementations of {@link #buildRootModel(CommandGroup)},
     * {@link #buildChildModel(Object, AbstractCommand, int)}, and
     * {@link #buildGroupModel(Object, CommandGroup, int)}.
     *
     * Additionally,
     *
     * @param commandGroup
     *            the root of the structure for which an mapping objectmodel
     *            will be built.
     * @return the build object model
     */
    public final Object buildModel(CommandGroup commandGroup)
    {
        Object model = buildRootModel(commandGroup);

        recurse(commandGroup, model, 0);

        return model;
    }

    private void recurse(AbstractCommand childCommand, Object parentModel, int level)
    {
        if (childCommand instanceof CommandGroup)
        {
            CommandGroup commandGroup = (CommandGroup) childCommand;
            parentModel = buildGroupModel(parentModel, commandGroup, level);
            if (continueDeeper(commandGroup, level))
            {
                Iterator members = commandGroup.memberIterator();
                while (members.hasNext())
                {
                    GroupMember member = (GroupMember) members.next();
                    AbstractCommand memberCommand = member.getCommand();
                    recurse(memberCommand, parentModel, level + 1);
                }
            }

        }
        else
        {
            buildChildModel(parentModel, childCommand, level);

        }

    }

}

