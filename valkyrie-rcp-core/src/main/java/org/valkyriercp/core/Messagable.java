package org.valkyriercp.core;

/**
 * An interface to be implemented by objects that are capable of receiving messages to be
 * provided to the user.
 *
 * @author Keith Donald
 */
public interface Messagable extends PropertyChangePublisher {

	/** The name of the message property, to be used for publishing update events. */
	public static final String MESSAGE_PROPERTY = "message";

    /**
     * Set the message.
     * @param message The message.
     */
    public void setMessage(Message message);

}
