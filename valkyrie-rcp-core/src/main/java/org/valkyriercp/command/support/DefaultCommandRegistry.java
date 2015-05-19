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
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.command.ActionCommandExecutor;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandRegistryListener;

import java.util.*;

/**
 * The default implementation of the {@link CommandRegistry} interface. This implementation 
 * may act as the child of another registry, allowing for a hierarchy of registries to be created.
 * If a command is requested from this registry but cannot be found, the request will be delegated
 * to the parent registry.
 * 
 * 
 * @author Keith Donald
 * @author Kevin Stembridge
 */
public class DefaultCommandRegistry implements CommandRegistry, CommandRegistryListener {
    
    /**
     * Class logger, available to subclasses.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private final List commandRegistryListeners = new LinkedList();

    private final Map commandMap = new Hashtable();

    private CommandRegistry parent;

    /**
     * Creates a new uninitialized {@code DefaultCommandRegistry}.
     */
    public DefaultCommandRegistry() {
        //do nothing
    }

    /**
     * Creates a new {@code DefaultCommandRegistry} as a child of the given registry. The newly
     * created instance will be added as a listener on the parent registry.
     *
     * @param parent The parent registry. May be null.
     */
    public DefaultCommandRegistry(CommandRegistry parent) {
        internalSetParent(parent);
    }

    /**
     * Sets the given registry to be the parent of this instance. If the new parent is not null,
     * this instance will act as a registry listener on it.
     *
     * @param parent The parent registry. May be null.
     */
    public void setParent(CommandRegistry parent) {
        internalSetParent(parent);
    }
    
    /**
     * This method is provided as a private helper so that it can be called by the constructor, 
     * instead of the constructor having to call the public overridable setParent method. 
     */
    private void internalSetParent(CommandRegistry parentRegistry) {
        
        if (!ObjectUtils.nullSafeEquals(this.parent, parentRegistry)) {
            
            if (this.parent != null) {
                this.parent.removeCommandRegistryListener(this);
            }
            
            this.parent = parentRegistry;
            
            if (this.parent != null) {
                this.parent.addCommandRegistryListener(this);
            }
            
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public void commandRegistered(CommandRegistryEvent event) {
        Assert.notNull(event, "event");
        fireCommandRegistered(event.getCommand());
    }

    /**
     * {@inheritDoc}
     * @deprecated
     */
    public ActionCommand getActionCommand(String commandId) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to retrieve ActionCommand with id ["
                         + commandId
                         + "] from the command registry.");
        }
        
        Object command = getCommand(commandId, ActionCommand.class);
        
        return (ActionCommand) command;
        
    }

    /**
     * {@inheritDoc}
     * @deprecated
     */
    public CommandGroup getCommandGroup(String groupId) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to retrieve command group with id ["
                         + groupId
                         + "] from the command registry.");
        }
        
        Object command = getCommand(groupId, CommandGroup.class);
        
        return (CommandGroup) command;
        
    }

    /**
     * {@inheritDoc}
     * @deprecated
     */
    public boolean containsActionCommand(String commandId) {
        
        if (commandMap.containsKey(commandId)) {
            return true;
        }
        
        if (parent != null) {
            return parent.containsActionCommand(commandId);
        }
        
        return false;
        
    }

    /**
     * {@inheritDoc}
     * @deprecated
     */
    public boolean containsCommandGroup(String groupId) {
        if (commandMap.get(groupId) instanceof CommandGroup) {
            return true;
        }
        if (parent != null) {
            return parent.containsCommandGroup(groupId);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void registerCommand(AbstractCommand command) {
        
        Assert.notNull(command, "Command cannot be null.");
        Assert.isTrue(command.getId() != null, "A command must have an identifier to be placed in a registry.");
        
        Object previousCommand = this.commandMap.put(command.getId(), command);
        
        if (previousCommand != null && logger.isWarnEnabled()) {
            logger.info("The command ["
                        + previousCommand
                        + "] was overwritten in the registry with the command ["
                        + command
                        + "]");
        }
        
        if (command instanceof CommandGroup) {
            ((CommandGroup) command).setCommandRegistry(this);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Command registered '" + command.getId() + "'");
        }
        
        fireCommandRegistered(command);
        
    }

    /**
     * Fires a 'commandRegistered' {@link CommandRegistryEvent} for the given command to all 
     * registered listeners. 
     *
     * @param command The command that has been registered. Must not be null.
     * 
     * @throws IllegalArgumentException if {@code command} is null.
     */
    protected void fireCommandRegistered(AbstractCommand command) {
        
        Assert.notNull(command, "command");
        
        if (commandRegistryListeners.isEmpty()) {
            return;
        }
        
        CommandRegistryEvent event = new CommandRegistryEvent(this, command);
        
        for (Iterator i = commandRegistryListeners.iterator(); i.hasNext();) {
            ((CommandRegistryListener)i.next()).commandRegistered(event);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetableActionCommandExecutor(String commandId, ActionCommandExecutor executor) {
        
        Assert.notNull(commandId, "commandId");
        
        TargetableActionCommand command 
                = (TargetableActionCommand) getCommand(commandId, TargetableActionCommand.class);
            
        if (command != null) {
            command.setCommandExecutor(executor);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public void addCommandRegistryListener(CommandRegistryListener listener) {

        if (logger.isDebugEnabled()) {
            logger.debug("Adding command registry listener " + listener);
        }
        
        commandRegistryListeners.add(listener);
        
    }

    /**
     * {@inheritDoc}
     */
    public void removeCommandRegistryListener(CommandRegistryListener listener) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Removing command registry listener " + listener);
        }
        
        commandRegistryListeners.remove(listener);
        
    }

    /**
     * Returns the parent registry of this instance.
     *
     * @return The parent registry, or null.
     */
    public CommandRegistry getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsCommand(String commandId) {
        
        Assert.notNull(commandId, "commandId");
        
        if (this.commandMap.containsKey(commandId)) {
            return true;
        }
        
        if (this.parent != null) {
            return this.parent.containsCommand(commandId);
        }
        
        return false;
        
    }

    /**
     * {@inheritDoc}
     */
    public Object getCommand(String commandId) {
        return getCommand(commandId, null);
    }

    /**
     * {@inheritDoc}
     */
    public Object getCommand(String commandId, Class requiredType) throws CommandNotOfRequiredTypeException {
        
        Assert.notNull(commandId, "commandId");
        
        Object command = this.commandMap.get(commandId);
        
        if (command == null && this.parent != null) {
            command = this.parent.getCommand(commandId);
        }
        
        if (command == null) {
            return null;
        }
        
        if (requiredType != null && !ClassUtils.isAssignableValue(requiredType, command)) {
            throw new CommandNotOfRequiredTypeException(commandId, requiredType, command.getClass());
        }
        
        return command;
        
    }

    /**
     * {@inheritDoc}
     */
    public Class getType(String commandId) {
        
        Assert.notNull(commandId, "commandId");
        
        Object command = getCommand(commandId);
        
        if (command == null) {
            return null;
        }
        else {
            return command.getClass();
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTypeMatch(String commandId, Class targetType) {
        
        Assert.notNull(commandId, "commandId");
        Assert.notNull(targetType, "targetType");
        
        Class commandType = getType(commandId);
        
        if (commandType == null) {
            return false;
        }
        else {
            return ClassUtils.isAssignable(targetType, commandType);
        }
        
    }

}
