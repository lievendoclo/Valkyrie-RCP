package org.valkyriercp.binding.validation;

/**
 * Interface to be implemented by classes capable of validating domain objects.
 *
 * @author Oliver Hutchison
 */
public interface Validator<T> {
	/**
	 * Validates the provided object.
	 *
	 * @param object the object to validate
	 * @return the results on the validation
	 */
	ValidationResults validate(T object);
}
