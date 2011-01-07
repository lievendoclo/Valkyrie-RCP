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

