package org.valkyriercp.application.exceptionhandling;

/**
 * Uses 1.5 API.
 */
public abstract class AbstractRegisterableExceptionHandler implements Thread.UncaughtExceptionHandler,
        RegisterableExceptionHandler {

    /**
     * Registers the exception handler for all threads and the event thread specifically.
     */
    public void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        AwtExceptionHandlerAdapterHack.registerExceptionHandler(this);
    }

}
