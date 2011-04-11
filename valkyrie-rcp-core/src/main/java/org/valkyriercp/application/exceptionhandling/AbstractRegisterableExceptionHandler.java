package org.valkyriercp.application.exceptionhandling;

/**
 * Uses 1.5 API.
 */
public abstract class AbstractRegisterableExceptionHandler<SELF_TYPE extends AbstractRegisterableExceptionHandler<SELF_TYPE>> implements Thread.UncaughtExceptionHandler,
        RegisterableExceptionHandler {

    protected final SELF_TYPE self() {
        return (SELF_TYPE) this;
    }

    /**
     * Registers the exception handler for all threads and the event thread specifically.
     */
    public void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        AwtExceptionHandlerAdapterHack.registerExceptionHandler(this);
    }

}
