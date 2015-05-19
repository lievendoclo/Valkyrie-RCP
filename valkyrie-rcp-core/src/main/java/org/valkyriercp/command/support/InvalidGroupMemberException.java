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
 * Indicates that an object is not a valid member of a {@link CommandGroup}.
 *
 * <p>
 * Usually, a command group member will be a subclass of {@link AbstractCommand}, however some
 * command group implementations may define more specific rules about what types of members they
 * will accept.
 * </p>
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class InvalidGroupMemberException extends CommandException {

    private static final long serialVersionUID = 7891614557214887191L;

    private final Class invalidMemberClass;
    private final Class commandGroupClass;

    private static String createDefaultMessage(Class invalidMemberClass, Class commandGroupClass) {

        return "An object of type ["
               + invalidMemberClass
               + "] is not a valid member for a group of type ["
               + commandGroupClass
               + "]";

    }

    /**
     * Creates a new {@code InvalidGroupMemberException}.
     *
     * @param invalidMemberClass The class of the invalid member.
     * @param commandGroupClass The class of the command group that the member is not valid for.
     */
    public InvalidGroupMemberException(Class invalidMemberClass, Class commandGroupClass) {
        super(createDefaultMessage(invalidMemberClass, commandGroupClass));
        this.invalidMemberClass = invalidMemberClass;
        this.commandGroupClass = commandGroupClass;
    }

    /**
     * Creates a new {@code InvalidGroupMemberException}.
     *
     * @param message The detail message.
     * @param invalidMemberClass The class of the invalid member.
     * @param commandGroupClass The class of the command group that the member is invalid for.
     */
    public InvalidGroupMemberException(String message, Class invalidMemberClass, Class commandGroupClass) {
        this(message, invalidMemberClass, commandGroupClass, null);
    }

    /**
     * Creates a new {@code InvalidGroupMemberException}.
     *
     * @param message The detail message.
     * @param invalidMemberClass The class of the invalid member.
     * @param commandGroupClass The class of the command group that the member is invalid for.
     * @param cause The nested exception.
     */
    public InvalidGroupMemberException(String message,
                                       Class invalidMemberClass,
                                       Class commandGroupClass,
                                       Throwable cause) {

        super(message, cause);
        this.invalidMemberClass = invalidMemberClass;
        this.commandGroupClass = commandGroupClass;

    }

    /**
     * Returns the class of the command group that the member is invalid for.
     * @return Returns the value of the commandGroupClass field, possibly null.
     */
    public Class getCommandGroupClass() {
        return this.commandGroupClass;
    }

    /**
     * Returns the class of the invalid member.
     * @return Returns the value of the invalidMemberClass field, possibly null.
     */
    public Class getInvalidMemberClass() {
        return this.invalidMemberClass;
    }

}

