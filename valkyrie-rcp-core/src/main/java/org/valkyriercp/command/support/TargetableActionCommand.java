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

import org.springframework.util.ObjectUtils;
import org.valkyriercp.command.ActionCommandExecutor;
import org.valkyriercp.command.GuardedActionCommandExecutor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An {@link ActionCommand} that delegates to an internal {@link ActionCommandExecutor}.
 * The executor can be provided at construction but can also be provided
 * programmatically at runtime and replaced during program execution. This enables a shared
 * global command feature whereby a single command is specified that can perform different
 * actions depending on the currently active context.
 *
 * @author Keith Donald
 */
public class TargetableActionCommand extends ActionCommand {

    private ActionCommandExecutor commandExecutor;

    private PropertyChangeListener guardRelay;

    /**
     * Creates a new uninitialized {@code TargetableActionCommand}. If defined
     * in a Spring bean factory, the name of the bean will become the id of this instance.
     */
    public TargetableActionCommand() {
        this(null);
    }

    /**
     * Creates a new {@code TargetableActionCommand} with the given identifier.
     * The instance will be initialized in a disabled state.
     *
     * @param commandId The identifier for this instance.
     */
    public TargetableActionCommand(String commandId) {
        this(commandId, null);
    }

    /**
     * Creates a new {@code TargetableActionCommand} with the given identifier and initial
     * executor. The instance will be initialized in a disabled state.
     *
     * @param commandId The identifier for this instance.
     * @param commandExecutor The initial command executor.
     */
    public TargetableActionCommand(String commandId, ActionCommandExecutor commandExecutor) {
        super(commandId);
        setEnabled(false);
        setCommandExecutor(commandExecutor);
    }

    /**
     * Attaches the given executor to this command instance, detaching the current executor in the process.
     *
     * @param commandExecutor The executor to be attached. May be null, in which case this command
     * will be disabled.
     */
    public void setCommandExecutor(ActionCommandExecutor commandExecutor) {
        if (ObjectUtils.nullSafeEquals(this.commandExecutor, commandExecutor)) {
            return;
        }
        if (commandExecutor == null) {
            detachCommandExecutor();
        }
        else {
            if (this.commandExecutor instanceof GuardedActionCommandExecutor) {
                unsubscribeFromGuardedCommandDelegate();
            }
            this.commandExecutor = commandExecutor;
            attachCommandExecutor();
        }
    }

    /**
     * Attaches the currently assigned command executor to this instance. The command will
     * be enabled by default unless the executor is a {@link GuardedActionCommandExecutor},
     * in which case the command will be assigned the enabled state of the executor.
     */
    private void attachCommandExecutor() {
        if (this.commandExecutor instanceof GuardedActionCommandExecutor) {
            GuardedActionCommandExecutor dynamicHandler = (GuardedActionCommandExecutor)commandExecutor;
            setEnabled(dynamicHandler.isEnabled());
            subscribeToGuardedCommandDelegate();
        }
        else {
            setEnabled(true);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Command executor '" + this.commandExecutor + "' attached.");
        }
    }

    private void subscribeToGuardedCommandDelegate() {
        this.guardRelay = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(((GuardedActionCommandExecutor)commandExecutor).isEnabled());
            }
        };
        ((GuardedActionCommandExecutor)commandExecutor).addEnabledListener(guardRelay);
    }

    /**
     * Detaches the current executor from this command and sets the command to disabled state.
     */
    public void detachCommandExecutor() {
        if (this.commandExecutor instanceof GuardedActionCommandExecutor) {
            unsubscribeFromGuardedCommandDelegate();
        }
        this.commandExecutor = null;
        setEnabled(false);
        logger.debug("Command delegate detached.");
    }

    private void unsubscribeFromGuardedCommandDelegate() {
        ((GuardedActionCommandExecutor)this.commandExecutor).removeEnabledListener(guardRelay);
    }

    /**
     * Executes this command by delegating to the currently assigned executor.
     */
    protected void doExecuteCommand() {
        if (this.commandExecutor instanceof ParameterizableActionCommandExecutor) {
            ((ParameterizableActionCommandExecutor) this.commandExecutor).execute(getParameters());
        }
        else {
        	if (this.commandExecutor != null) {
        		this.commandExecutor.execute();
        	}
        }
    }

}