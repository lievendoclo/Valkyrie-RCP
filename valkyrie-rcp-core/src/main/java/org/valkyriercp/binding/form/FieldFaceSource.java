package org.valkyriercp.binding.form;

/**
 * Interface to be implemented by objects that can resolve a FieldFace for a
 * field path and optionaly a given context.
 *
 * @author Oliver Hutchison
 * @author Mathias Broekelmann
 */
public interface FieldFaceSource {

	/**
	 * Return the FieldFace for the field.
	 *
	 * @param field the form field
	 * @return the FieldFace for the given field (never null).
	 */
	FieldFace getFieldFace(String field);

	/**
	 * Return the FieldFace for the given field name and a context.
	 *
	 * @param field the field name
	 * @param context optional context for the field face
	 * @return the FieldFace for the given field (never null).
	 * @throws IllegalArgumentException if field is null or empty
	 */
	FieldFace getFieldFace(String field, Object context);
}
