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
package org.valkyriercp.core;

public interface DescribedElement {
    /**
	 * The property name used when firing events for the {@code displayName}
	 * property.
	 */
	public static final String DISPLAY_NAME_PROPERTY = "displayName";

	/**
	 * The property name used when firing events for the {@code caption}
	 * property.
	 */
	public static final String CAPTION_PROPERTY = "caption";

	/**
	 * The property name used when firing events for the {@code description}
	 * property.
	 */
	public static final String DESCRIPTION_PROPERTY = "description";

	/**
	 * Returns the display name of this object.
	 *
	 * @return The display name, or <code>null</code>.
	 */
	public String getDisplayName();

	/**
	 * Returns the caption for this object.
	 *
	 * @return The caption, or <code>null</code>.
	 */
	public String getCaption();

	/**
	 * Returns a description of this object.
	 *
	 * @return The description, or <code>null</code>.
	 */
	public String getDescription();
}
