package org.valkyriercp.binding.format;

import org.springframework.core.NestedRuntimeException;

/**
 * Thrown when a formatted value is of the wrong form.
 *
 * @author Keith Donald
 */
public class InvalidFormatException extends NestedRuntimeException {

	private String invalidValue;

	private String expectedFormat;

	/**
	 * Create a new invalid format exception.
	 * @param invalidValue the invalid value
	 * @param expectedFormat the expected format
	 */
	public InvalidFormatException(String invalidValue, String expectedFormat) {
		this(invalidValue, expectedFormat, null);
	}

	/**
	 * Create a new invalid format exception.
	 * @param invalidValue the invalid value
	 * @param expectedFormat the expected format
	 * @param cause the underlying cause of this exception
	 */
	public InvalidFormatException(String invalidValue, String expectedFormat, Throwable cause) {
		super("Invalid format for value '" + invalidValue + "'; the expected format was '" + expectedFormat + "'",
				cause);
		this.invalidValue = invalidValue;
		this.expectedFormat = expectedFormat;
	}

	/**
	 * Create a new invalid format exception.
	 * @param invalidValue the invalid value
	 * @param expectedFormat the expected format
	 * @param message a descriptive message
	 * @param cause the underlying cause of this exception
	 */
	public InvalidFormatException(String invalidValue, String expectedFormat, String message, Throwable cause) {
		super(message, cause);
		this.invalidValue = invalidValue;
		this.expectedFormat = expectedFormat;
	}

	/**
	 * Returns the invalid value.
	 */
	public String getInvalidValue() {
		return invalidValue;
	}

	/**
	 * Returns the expected format.
	 */
	public String getExpectedFormat() {
		return expectedFormat;
	}
}