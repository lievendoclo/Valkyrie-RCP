package org.valkyriercp.rules.reporting;

/**
 * Implementations of this interface are capable to resolve a given field name
 * to a user friendly display name
 *
 * @author Mathias Broekelmann
 *
 */
public interface ObjectNameResolver {

	/**
	 * resolves a field name to a user friendly display name
	 */
	String resolveObjectName(String objectName);
}
