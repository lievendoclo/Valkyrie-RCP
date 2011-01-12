package org.valkyriercp.binding;

import java.util.Map;

/**
 * Simple interface for accessing metadata about a particular property.
 *
 * @author Keith Donald
 */
public interface PropertyMetadataAccessStrategy {

	/**
	 * Determine if the given property is readable.
	 *
	 * @param propertyName property to examine.
	 * @return <code>true</code> if the property is readable.
	 */
	boolean isReadable(String propertyName);

	/**
	 * Determine if the given property is writeable.
	 *
	 * @param propertyName property to examine.
	 * @return <code>true</code> if the property is writeable.
	 */
	boolean isWriteable(String propertyName);

	/**
	 * Get the type of the given property.
	 *
	 * @param propertyName property to examine.
	 * @return the type of the property.
	 */
	Class getPropertyType(String propertyName);

	/**
	 * Returns custom metadata that may be associated with the specified
	 * property path.
	 *
	 * @param propertyName property to examine.
	 * @param key used to retreive the metadata.
	 * @return Object stored under the given key.
	 */
	Object getUserMetadata(String propertyName, String key);

	/**
	 * Returns all custom metadata associated with the specified property in the
	 * form of a Map.
	 *
	 * @param propertyName property to examine.
	 * @return Map containing String keys - this method may or may not return
	 * <code>null</code> if there is no custom metadata associated with the
	 * property.
	 */
	Map getAllUserMetadata(String propertyName);
}
