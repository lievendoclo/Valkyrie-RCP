package org.valkyriercp.binding.format;

/**
 * A lightweight interface for formatting a value and parsing a value from its formatted form.
 *
 * @author Keith Donald
 */
public interface Formatter {

	/**
	 * Format the value.
	 * @param value the value to format
	 * @return the formatted string, fit for display in a UI
	 * @throws IllegalArgumentException the value could not be formatted
	 */
	public String formatValue(Object value) throws IllegalArgumentException;

	/**
	 * Parse the formatted string representation of a value, restoring the value.
	 * @param formattedString the formatted string representation
	 * @param targetClass the target class to convert the formatted value to
	 * @return the parsed value
	 * @throws InvalidFormatException the string was in an invalid form
	 */
	public Object parseValue(String formattedString, Class targetClass) throws InvalidFormatException;

}