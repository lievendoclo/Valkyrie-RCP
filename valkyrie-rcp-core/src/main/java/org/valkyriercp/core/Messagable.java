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
