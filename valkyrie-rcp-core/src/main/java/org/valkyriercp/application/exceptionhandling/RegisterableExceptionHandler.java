package org.valkyriercp.application.exceptionhandling;

/**
 * An exception handler which can be registered (to for example the EDT and all threads).
 */
public interface RegisterableExceptionHandler extends Thread.UncaughtExceptionHandler {

    void registerExceptionHandler();

}
