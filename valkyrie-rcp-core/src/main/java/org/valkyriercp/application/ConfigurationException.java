package org.valkyriercp.application;

/**
 * <p>
 * Indicates that there is a problem with the runtime configuration of the
 * application. This is a fairly general top-level exception. Before creating
 * and throwing an instance of this type, consider if a more specific subclass
 * would be appropriate.
 * </p>
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class ConfigurationException extends ApplicationException {

	private static final long serialVersionUID = 1210397078030323683L;

	/**
	 * Creates a new {@code ConfigurationException}.
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified message.
	 *
	 * @param message The detail message.
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified message
	 * and nested exception.
	 *
	 * @param message The detail message.
	 * @param cause The nested exception.
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified nested
	 * exception.
	 *
	 * @param cause The nested exception.
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}


