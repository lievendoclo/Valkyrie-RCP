package org.valkyriercp.binding.validation;

import org.valkyriercp.core.Severity;

import java.util.Set;

/**
 * Interface to be implemented by objects that hold a list of validation results
 * for a specific object.
 *
 * @author Oliver Hutchison
 * @see org.springframework.binding.validation.support.DefaultValidationResults
 */
public interface ValidationResults {

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.ERROR</code>.
	 */
	boolean getHasErrors();

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.WARNING</code>.
	 */
	boolean getHasWarnings();

	/**
	 * Returns <code>true</code> of there are any validation messages of
	 * <code>Severity.INFO</code>.
	 */
	boolean getHasInfo();

	/**
	 * Returns the total number of validation messages.
	 */
	int getMessageCount();

	/**
	 * Returns the total number of validation messages of the specified
	 * Severity.
	 */
	int getMessageCount(Severity severity);

	/**
	 * Returns the total number of validation messages that apply to the
	 * specified property name.
	 */
	int getMessageCount(String propertyName);

	/**
	 * Returns a set holding all of the validation messages.
	 */
	Set getMessages();

	/**
	 * Returns a set holding all of the validation messages of the specified
	 * Severity.
	 */
	Set getMessages(Severity severity);

	/**
	 * Returns a set holding all of the validation messages that apply to the
	 * specified property name.
	 */
	Set getMessages(String propertyName);
}