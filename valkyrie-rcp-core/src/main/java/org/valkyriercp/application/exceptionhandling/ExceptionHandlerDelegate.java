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

