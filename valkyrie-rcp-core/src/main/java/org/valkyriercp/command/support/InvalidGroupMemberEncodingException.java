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

