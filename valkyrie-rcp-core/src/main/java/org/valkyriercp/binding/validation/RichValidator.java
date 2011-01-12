package org.valkyriercp.binding.validation;

/**
 * Interface to be implemented by classes capable of incrementally validating
 * domain objects. This interface is intended for validators that are able to
 * validate a subset of the domain objects properties.
 *
 * @author Oliver Hutchison
 */
public interface RichValidator extends Validator {

	/**
	 * Validates the given property of the provided object.
	 *
	 * @param object the object to validate (may be an implementation of
	 * @link org.springframework.binding.PropertyAccessStrategy)
	 * @param property the name of the only property that has changed since the
	 * last call to validate.
	 * @return the results on the validation
	 */
	ValidationResults validate(Object object, String property);
}
