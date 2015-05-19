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

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.List;

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
        this(Lists.newArrayList(throwableClassFQN), exceptionHandler);
    }

    public LenientSimpleExceptionHandlerDelegate(List<String> throwableClassFQNList,
                                                 Thread.UncaughtExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.throwableClassFQNList = throwableClassFQNList;
    }

    public void setThrowableClass(String throwableClassFQN) {
        setThrowableClassList(Lists.newArrayList(throwableClassFQN));
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
        List<Class> transformedList = Lists.transform(throwableClassFQNList, new Function<String, Class>() {
            @Override
            public Class apply(String input) {
                try {
                    return Class.forName(input);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
        });
        for (Class throwableClass : Collections2.filter(transformedList, Predicates.notNull())) {
            if (throwableClass.isInstance(throwable)) {
                return true;
            }
        }
        return false;
    }

}

