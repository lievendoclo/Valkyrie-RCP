package org.valkyriercp.binding.validation;

import org.valkyriercp.core.Message;

/**
 * A specific type of message that relates to a property.
 * <code>ValidationMessage</code>s often find their origin in validation
 * triggered by a constraint on a property. This information is additionally
 * kept available in this <code>ValidationMessage</code>.
 */
public interface ValidationMessage extends Message {

	/**
	 * The property name for messages that have a global scope i.e. do not apply
	 * to a specific property.
	 */
	public static final String GLOBAL_PROPERTY = null;

	/**
	 * The property that this validation message applies to; or
	 * <code>GLOBAL_PROPERTY</code> if this message does not apply to a
	 * specific property.
	 */
	String getProperty();

}

