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

/**
 * Indicates that a command object was not of the expected type.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class CommandNotOfRequiredTypeException extends CommandException {

    private static final long serialVersionUID = 6891900212653390852L;

    private final String commandId;
    private final Class requiredType;
    private final Class actualType;

    private static String createMessage(String commandId, Class requiredType, Class actualType) {

        return "The command with id ["
               + commandId
               + "] was expected to be of type ["
               + requiredType
               + "] but was of type ["
               + actualType
               + "]";

    }

    /**
     * Creates a new {@code CommandNotOfRequiredTypeException}.
     *
     * @param commandId The id of the command.
     * @param requiredType The required type of the command.
     * @param actualType The actual type of the command.
     */
    public CommandNotOfRequiredTypeException(String commandId, Class requiredType, Class actualType) {
        super(createMessage(commandId, requiredType, actualType));
        this.commandId = commandId;
        this.requiredType = requiredType;
        this.actualType = actualType;
    }

    /**
     * Returns the actual type of the command.
     * @return Returns the value of the actualType field, possibly null.
     */
    public Class getActualType() {
        return this.actualType;
    }

    /**
     * Returns the id of the command
     * @return Returns the value of the commandId field, possibly null.
     */
    public String getCommandId() {
        return this.commandId;
    }

    /**
     * Returns the type that the command was expected to be.
     * @return Returns the value of the requiredType field, possibly null.
     */
    public Class getRequiredType() {
        return this.requiredType;
    }

}

