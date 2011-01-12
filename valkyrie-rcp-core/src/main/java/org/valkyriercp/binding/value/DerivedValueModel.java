package org.valkyriercp.binding.value;

/**
 * A value model that derives its value by applying some transformation to one
 * or more source value models.
 *
 * <p>
 * Typical usage would be a type converter or string formatter.
 *
 * @author Oliver Hutchison
 */
public interface DerivedValueModel extends ValueModel {

	/**
	 * Returns an array of all values models that are used to derive the value
	 * represented by this value model.
	 */
	ValueModel[] getSourceValueModels();

	/**
	 * Returns true if the implementation does not support calls setValue.
	 */
	boolean isReadOnly();
}
