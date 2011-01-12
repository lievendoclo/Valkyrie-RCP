package org.valkyriercp.binding.validation;

/**
 * Specific listener interface to facilitate passing of
 * {@link ValidationResults}.
 *
 * @author Keith Donald
 */
public interface ValidationListener {

	/**
	 * Fired whenever there is a change to set of validation results.
	 *
	 * @param results the set of valiation results, this set will incude results
	 * for all properties even if the listener is only listening for changes to
	 * a specific properties results.
	 */
	public void validationResultsChanged(ValidationResults results);
}
