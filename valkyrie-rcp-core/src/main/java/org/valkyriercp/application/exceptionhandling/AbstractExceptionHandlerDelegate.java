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
 * Superclass for delegate implementations
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public abstract class AbstractExceptionHandlerDelegate<SELF extends AbstractExceptionHandlerDelegate<SELF>> implements ExceptionHandlerDelegate {

    protected Thread.UncaughtExceptionHandler exceptionHandler;
    protected ExceptionPurger exceptionPurger = null;
    protected boolean purgeOnAppropriateCheck = true;
    protected boolean purgeOnHandling = true;

    public AbstractExceptionHandlerDelegate() {
    }

    protected final SELF self() {
        return (SELF) this;
    }

    public AbstractExceptionHandlerDelegate(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public SELF handledBy(Thread.UncaughtExceptionHandler exceptionHandler) {
        setExceptionHandler(exceptionHandler);
        return self();
    }

    /**
     * If set the throwable will first be purged before doing the approriate check or handling it.
     * @param exceptionPurger
     */
    public void setExceptionPurger(ExceptionPurger exceptionPurger) {
        this.exceptionPurger = exceptionPurger;
    }

    public SELF purgedBy(ExceptionPurger exceptionPurger) {
        setExceptionPurger(exceptionPurger);
        return self();
    }

    public void setPurgeOnAppropriateCheck(boolean purgeOnAppropriateCheck) {
        this.purgeOnAppropriateCheck = purgeOnAppropriateCheck;
    }

    public SELF purgingOnAppropriateCheck(boolean purgeOnAppropriateCheck) {
        setPurgeOnAppropriateCheck(purgeOnAppropriateCheck);
        return self();
    }

    public void setPurgeOnHandling(boolean purgeOnHandling) {
        this.purgeOnHandling = purgeOnHandling;
    }

    public SELF purgingOnHandling(boolean purgeOnHandling) {
        setPurgeOnHandling(purgeOnHandling);
        return self();
    }


    /**
     * {@inheritDoc}
     */
    public final boolean hasAppropriateHandler(Throwable throwable) {
        if (exceptionPurger != null && purgeOnAppropriateCheck) {
            throwable = exceptionPurger.purge(throwable);
        }
        return hasAppropriateHandlerPurged(throwable);
    }


    public abstract boolean hasAppropriateHandlerPurged(Throwable throwable);

    /**
     * {@inheritDoc}
     */
    public final void uncaughtException(Thread thread, Throwable throwable) {
        if (exceptionPurger != null && purgeOnHandling) {
            throwable = exceptionPurger.purge(throwable);
        }
        uncaughtExceptionPurged(thread, throwable);
    }

    public void uncaughtExceptionPurged(Thread thread, Throwable throwable) {
        exceptionHandler.uncaughtException(thread, throwable);
    }

}

