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
