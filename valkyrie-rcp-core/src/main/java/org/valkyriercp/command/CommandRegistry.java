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

import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandNotOfRequiredTypeException;

/**
 * A registry for command objects.
 *
 * <p>
 * The commands to be placed in the registry must have non-null identifiers available via their
 * {@link AbstractCommand#getId()} method, and this id is expected to be unique amongst all the
 * commands in the application. Generally speaking, this uniqueness will not be enforced by the
 * framework but will instead allow commands to be overwritten by others with the same id.
 * </p>
 *
 * <p>
 * Implementations may allow for the creation of hierarchical registries whereby a registry may have
 * a parent registry. In this case, if a registry does not contain a requested command it will
 * delegate to its parent to find the command. Commands in a child registry will override commands
 * with the same ID in any ancestor registries.
 * </p>
 *
 * @author Keith Donald
 * @author Kevin Stembridge
 *
 */
public interface CommandRegistry {

    /**
     * Returns true if the registry contains a command with the given identifier.
     *
     * @param commandId The ID of the command to search for.
     * @return true if the registry contains the command, false otherwise.
     *
     * @throws IllegalArgumentException if {@code commandId} is null.
     */
    boolean containsCommand(String commandId);

    /**
     * Retrieves from the registry the command with the given identifier.
     *
     * @param commandId The ID of the command to be retrieved.
     * @return The command with the given ID, or null if no such command could be found.
     *
     * @throws IllegalArgumentException if {@code commandId} is null.
     */
    Object getCommand(String commandId);

    /**
     * Retrieves from the registry the command with the given identifier. An exception is thrown
     * if the retrieved command is not assignable to the required type.
     *
     * @param commandId The identifier of the command to be retrieved. Must not be null.
     * @param requiredType The required type of the command with the given id.
     *
     * @return The command with the given id if it exists in the registry and is of the required type.
     *
     * @throws CommandNotOfRequiredTypeException if the retrieved command is not assignable to
     * the required type.
     */
    Object getCommand(String commandId, Class requiredType) throws CommandNotOfRequiredTypeException;

    /**
     * Returns the type of the command with the given identifier, if it is contained in the
     * registry.
     *
     * @param commandId The ID of the command whose type is to be returned. Must not be null.
     * @return The type of the command with the given ID, or null if no such command exists in
     * the registry.
     *
     * @throws IllegalArgumentException if {@code commandId} is null.
     */
    Class getType(String commandId);

    /**
     * Returns true if the command with the given identifier is assignable to the given type.
     *
     * @param commandId The ID of the command whose type will be checked. Must not be null.
     * @param targetType The type to be checked against the type of the command. Must not be null.
     * @return true if a command with the given ID exists in the registry and it is assignable to
     * the given target type, false otherwise.
     *
     * @throws IllegalArgumentException if either argument is null.
     */
    boolean isTypeMatch(String commandId, Class targetType);

    /**
     * Returns the {@link ActionCommand} that has the given id.
     *
     * @param commandId The id of the action command to be returned.
     * @return The action command with the given id, or null if no such command exists in the
     * registry.
     *
     * @throws IllegalArgumentException if {@code commandId} is null.
     * @throws CommandNotOfRequiredTypeException if there is a command with the given id in the
     * registry but it is not of type {@link ActionCommand}.
     *
     * @deprecated use {@link #getCommand(String, Class)} instead. You may choose to either catch
     * the {@link CommandNotOfRequiredTypeException} or call {@link #isTypeMatch(String, Class)} first.
     */
    ActionCommand getActionCommand(String commandId);

    /**
     * Returns the {@link CommandGroup} that has the given id.
     *
     * @param groupId The id of the command group to be returned.
     * @return The command group with the given id, or null if no such command group exists in the
     * registry.
     *
     * @throws IllegalArgumentException if {@code commandId} is null.
     * @throws CommandNotOfRequiredTypeException if there is a command with the given id in the
     * registry but it is not of type {@link CommandGroup}.
     *
     * @deprecated use {@link #getCommand(String, Class)} instead. You may choose to either catch
     * the {@link CommandNotOfRequiredTypeException} or call {@link #isTypeMatch(String, Class)} first.
     */
    CommandGroup getCommandGroup(String groupId);

    /**
     * Returns true if the registry contains a command of type {@link ActionCommand} with the
     * given id.
     *
     * @param commandId The id of the command to be searched for.
     * @return true if the registry contains the command and it is assignable to {@link ActionCommand}.
     *
     * @deprecated Replaced by {@link #isTypeMatch(String, Class)}
     */
    boolean containsActionCommand(String commandId);

    /**
     * Returns true if the registry contains a command of type {@link CommandGroup} with the
     * given id.
     *
     * @param groupId The id of the command group to be searched for.
     * @return true if the registry contains the command and it is assignable to {@link CommandGroup}.
     *
     * @deprecated Replaced by {@link #isTypeMatch(String, Class)}
     */
    boolean containsCommandGroup(String groupId);

    /**
     * Registers the given command with the registry. Neither the command nor its id can be null.
     * All registered listeners will be notified of the newly registered command via their
     * {@link CommandRegistryListener#commandRegistered(CommandRegistryEvent)} method. If the
     * given command is an instance of {@link CommandGroup}, its
     * {@link CommandGroup#setCommandRegistry(CommandRegistry)} method must be called to set this
     * instance as the registry for the command group.
     *
     * @param command The command to be registered. Must not be null.
     *
     * @throws IllegalArgumentException if {@code command} is null or if its id is null.
     */
    void registerCommand(AbstractCommand command);

    /**
     * Sets a command executor for the command with the given id. The actual type of the command
     * must be assignable to {@link org.valkyriercp.command.support.TargetableActionCommand}.
     *
     * @param targetableCommandId The id of the targetable command that will have its executor set.
     * Must not be null.
     * @param commandExecutor The command executor. May be null.
     *
     * @throws IllegalArgumentException if {@code targetableCommandId} is null.
     * @throws CommandNotOfRequiredTypeException if the command with the given id is not a
     * {@link org.valkyriercp.command.support.TargetableActionCommand}.
     */
    void setTargetableActionCommandExecutor(String targetableCommandId, ActionCommandExecutor commandExecutor);

    /**
     * Adds the given listener to the colleciton of listeners that will be notified of registry events.
     *
     * @param listener The listener to be added. Must not be null.
     */
    void addCommandRegistryListener(CommandRegistryListener listener);

    /**
     * Remove the given listener from the collection of listeners that will be notified of registry events.
     *
     * @param listener The listener to be removed.
     */
    void removeCommandRegistryListener(CommandRegistryListener listener);

}
