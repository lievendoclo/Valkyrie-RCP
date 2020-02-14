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


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles the thrownTrowable by the exception handler if it is an instance of one of the throwableClassList by its
 * fully qualified name.
 * Note: Also subclasses of the classes in the throwableClassList will be handled by the exception handler.
 *
 * @author Lieven Doclo
 * @since 2.0
 */
public class LenientSimpleExceptionHandlerDelegate<SELF extends LenientSimpleExceptionHandlerDelegate<SELF>> extends AbstractExceptionHandlerDelegate<SELF> {

    private List<String> throwableClassFQNList;

    public LenientSimpleExceptionHandlerDelegate() {
    }

    public LenientSimpleExceptionHandlerDelegate(String throwableClassFQN,
                                                 Thread.UncaughtExceptionHandler exceptionHandler) {
        this(Collections.singletonList(throwableClassFQN), exceptionHandler);
    }

    public LenientSimpleExceptionHandlerDelegate(List<String> throwableClassFQNList,
                                                 Thread.UncaughtExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.throwableClassFQNList = throwableClassFQNList;
    }

    public void setThrowableClass(String throwableClassFQN) {
        setThrowableClassList(Collections.singletonList(throwableClassFQN));
    }

    public SELF forThrowableFQN(String throwableClassFQN) {
        setThrowableClass(throwableClassFQN);
        return self();
    }


    public void setThrowableClassList(List<String> throwableClassFQNList) {
        this.throwableClassFQNList = throwableClassFQNList;
    }

    public SELF forThrowables(List<String> throwableClassFQNList) {
        setThrowableClassList(throwableClassFQNList);
        return self();
    }

    public boolean hasAppropriateHandlerPurged(Throwable throwable) {
        return throwableClassFQNList.stream()
                .map(s -> {
                    try {
                        return Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .anyMatch(throwableClass -> throwableClass.isInstance(throwable));
    }

}

