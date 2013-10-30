package org.valkyriercp.application;

/**
 * Indicates that an application-level programming error or a runtime
 * configuration error has occurred.
 *
 * <p>
 * This exception should not be thrown for errors such as invalid user
 * input or data access errors. Basically, this represents a defect in the
 * program or its runtime configuration. This is a very general top-level
 * exception. Before creating and throwing a new instance of this type,
 * consider if a more specific subclass would be more appropriate.
 * </p>
 *
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1732836822446152658L;

    /**
     * Creates a new {@code ApplicationException}.
     */
    public ApplicationException() {
        super();
    }

    /**
	 * Creates a new {@code ApplicationException} with the specified message.
	 *
	 * @param message The detail message.
	 */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code ApplicationException} with the specified message
     * and nested exception.
     *
     * @param message The detail message.
     * @param cause The nested exception.
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code ApplicationException} with the specified nested exception.
     *
     * @param cause The nested exception.
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }

}
