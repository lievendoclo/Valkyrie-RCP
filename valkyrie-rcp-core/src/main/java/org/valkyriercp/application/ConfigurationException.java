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
package org.valkyriercp.application;

/**
 * <p>
 * Indicates that there is a problem with the runtime configuration of the
 * application. This is a fairly general top-level exception. Before creating
 * and throwing an instance of this type, consider if a more specific subclass
 * would be appropriate.
 * </p>
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class ConfigurationException extends ApplicationException {

	private static final long serialVersionUID = 1210397078030323683L;

	/**
	 * Creates a new {@code ConfigurationException}.
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified message.
	 *
	 * @param message The detail message.
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified message
	 * and nested exception.
	 *
	 * @param message The detail message.
	 * @param cause The nested exception.
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new {@code ConfigurationException} with the specified nested
	 * exception.
	 *
	 * @param cause The nested exception.
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}
}


