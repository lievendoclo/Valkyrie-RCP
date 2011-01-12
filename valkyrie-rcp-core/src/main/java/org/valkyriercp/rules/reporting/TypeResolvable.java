package org.valkyriercp.rules.reporting;

/**
 * Indicates that an object has a logical type identifier (metadata) that can be
 * used, for example, for resolving messages.
 *
 * @author keith
 */
public interface TypeResolvable {

	/**
	 * Returns this object's logical type identifier.
	 *
	 * @return The type identifier
	 */
	public String getType();
}