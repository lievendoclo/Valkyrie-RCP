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

import org.valkyriercp.core.PropertyChangePublisher;

import java.util.Map;

/**
 * Encapsulates the state of an individual property of a form model.
 *
 * @author Oliver Hutchison
 */
public interface FieldMetadata extends PropertyChangePublisher {

	/** The name of the bound property <code>enabled</code>. */
	public static final String ENABLED_PROPERTY = "enabled";

	/** The name of the bound property <code>readOnly</code>. */
	public static final String READ_ONLY_PROPERTY = "readOnly";

	/** The name of the bound property <code>dirty</code>. */
	public static final String DIRTY_PROPERTY = "dirty";

	/**
	 * Return the type of this property.
	 */
	Class getPropertyType();

	/**
	 * Returns custom metadata that may be associated with this property.
	 */
	Object getUserMetadata(String key);

	/**
	 * Returns all custom metadata associated with this property in the form of
	 * a Map.
	 *
	 * @return Map containing String keys
	 */
	Map getAllUserMetadata();

	/**
	 * Sets whether or not this property is read only.
	 * <p>
	 * It's expected that controls bound to this form property will listen for
	 * changes to this value and if possible modify their display/behaviour to
	 * reflect the new state. e.g. When this property becomes true a text
	 * component would grey its self out and prevent any editing.
	 * <p>
	 * This value will be propagated up to any descendants.
	 *
	 * @param readOnly should this property be read only
	 */
	void setReadOnly(boolean readOnly);

	/**
	 * Returns whether or not the property is read only.
	 * <p>
	 * A property is read only if any of the following are true:
	 * <ul>
	 * <li>It is read only at the PropertyAccessStrategy level</li>
	 * <li>It is marked as read only by a call to the setReadOnly method of
	 * this class</li>
	 * <li>It is marked as read only by a call to the setReadOnly method of the
	 * metadata of any ancestor of the form model which contains this property</li>
	 * </ul>
	 */
	boolean isReadOnly();

	/**
	 * Sets the enabled value for this property.
	 * <p>
	 * It's expected that controls bound to this form property will listen for
	 * changes to this value and if possible modify their display/behaviour to
	 * reflect the new state.
	 * <p>
	 * This value will be propagated up to any descendants.
	 *
	 * @param enabled should this property be enabled
	 */
	void setEnabled(boolean enabled);

	/**
	 * Returns whether or not the property is enabled.
	 * <p>
	 * A property is enabled if all of the following are true:
	 * <ul>
	 * <li>The owning form model is enabled</li>
	 * <li>It has not been marked as disabled by a call to the setEnabled
	 * method of this class</li>
	 * <li>It has not been marked as disabled by by a call to the setEnabled
	 * method of the metadata of any ancestor of the form model which contains
	 * this property</li>
	 * </ul>
	 */
	boolean isEnabled();

	/**
	 * Returns whether or not the property is dirty.
	 */
	boolean isDirty();
}
