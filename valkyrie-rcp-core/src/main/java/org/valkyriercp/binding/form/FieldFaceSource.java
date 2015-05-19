/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
