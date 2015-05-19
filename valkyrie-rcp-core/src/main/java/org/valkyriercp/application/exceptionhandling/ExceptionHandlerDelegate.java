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
package org.valkyriercp.application.exceptionhandling;

/**
 * A delegate that might want to handle a throwable.
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public interface ExceptionHandlerDelegate extends Thread.UncaughtExceptionHandler {

    /**
     * Checks if the delegate wants to handle the throwable.
     *
     * @param thrownTrowable the thrown exception or error.
     * @return true if this exception handler wants to handle the throwable.
     */
    boolean hasAppropriateHandler(Throwable thrownTrowable);

}

