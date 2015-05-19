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
 * Indicates that an application-level programming error or a runtime
 * configuration error has occurred.
 *
 * <p>
 * This exception should not be thrown for errors such as invalid user
 * input or data access errors. Basically, this represents a defect in the
 * program or its runtime configuration. This is a very general top-level
 * exception. Before creating and throwing a new instance of this type,
 * consider if a more specific subclass would be more appropriate.
 * </p>
 *
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1732836822446152658L;

    /**
     * Creates a new {@code ApplicationException}.
     */
    public ApplicationException() {
        super();
    }

    /**
	 * Creates a new {@code ApplicationException} with the specified message.
	 *
	 * @param message The detail message.
	 */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code ApplicationException} with the specified message
     * and nested exception.
     *
     * @param message The detail message.
     * @param cause The nested exception.
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@code ApplicationException} with the specified nested exception.
     *
     * @param cause The nested exception.
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }

}
