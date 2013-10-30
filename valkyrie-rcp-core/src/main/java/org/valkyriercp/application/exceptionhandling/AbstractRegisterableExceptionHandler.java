package org.valkyriercp.application.exceptionhandling;

/**
 * Uses 1.5 API.
 */
public abstract class AbstractRegisterableExceptionHandler<SELF extends AbstractRegisterableExceptionHandler<SELF>> implements Thread.UncaughtExceptionHandler,
        RegisterableExceptionHandler {

    protected final SELF self() {
        return (SELF) this;
    }

    /**
     * Registers the exception handler for all threads and the event thread specifically.
     */
    public void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        AwtExceptionHandlerAdapterHack.registerExceptionHandler(this);
    }

}
