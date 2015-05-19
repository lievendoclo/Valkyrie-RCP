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

import org.valkyriercp.application.ApplicationException;

/**
 * Indicates that a runtime or configuration error has occurred within the Command
 * framework. This is a fairly generic exception. Before creating and throwing an instance
 * of this type, consider if a more specific subclass would be more appropriate.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class CommandException extends ApplicationException {

    private static final long serialVersionUID = 7845755447557671461L;

    /**
     * Creates a new {@code CommandException}.
     */
    public CommandException() {
        super();
    }

    /**
     * Creates a new {@code CommandException} with the specified message.
     *
     * @param message The detail message.
     */
    public CommandException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code CommandException} with the specified message
     * and nested exception.
     *
     * @param message The detail mesage.
     * @param cause The nested exception.
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code CommandException} with the specified nested exception.
     *
     * @param cause The nested exception.
     */
    public CommandException(Throwable cause) {
        super(cause);
    }

}

