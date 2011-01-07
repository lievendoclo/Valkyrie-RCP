package org.valkyriercp.command.support;

/**
 * Indicates that an encoded string specifying a command group member type is not valid.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class InvalidGroupMemberEncodingException extends CommandException {

    private static final long serialVersionUID = 4716510307315369998L;

    private final String encodedString;

    private static String createDefaultMessage(String message, String encodedString) {

        if (message != null) {
            return message + " [encodedString = '" + encodedString + "']";
        }
        else {
            return "The given string [" + encodedString + "] is not a valid command group member encoding.";
        }

    }

    /**
     * Creates a new {@code InvalidGroupMemberEncodingException} with the given detail message
     * and encoded string. The encoded string will be appended to the given message.
     *
     * @param message The detail message. If null, a default message will be created.
     * @param encodedString The encoded string that is invalid.
     *
     */
    public InvalidGroupMemberEncodingException(String message, String encodedString) {
        super(createDefaultMessage(message, encodedString));
        this.encodedString = encodedString;
    }


    /**
     * Returns the string that is not a valid group member encoding.
     * @return Returns the value of the encodedString field, possibly null.
     */
    public String getEncodedString() {
        return this.encodedString;
    }

}

