package org.valkyriercp.binding.form;

import org.valkyriercp.binding.validation.ValidationMessage;

/**
 * A class that can generate ValidationMessages for exception that occur during
 * a form model's binding process.
 *
 * @author Oliver Hutchison
 */
public interface BindingErrorMessageProvider {

	/**
	 * Translates the provided exception details into a ValidationMessage that
	 * will be used to provide feedback to the end user. Generally these
	 * exceptions results from type conversion problems.
	 *
	 * @param formModel the formModel
	 * @param propertyName the propertyName
	 * @param valueBeingSet the value which triggered the exception
	 */
	ValidationMessage getErrorMessage(FormModel formModel, String propertyName, Object valueBeingSet, Exception e);

}