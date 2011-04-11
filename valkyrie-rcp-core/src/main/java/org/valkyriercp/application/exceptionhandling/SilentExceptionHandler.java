package org.valkyriercp.application.exceptionhandling;

/**
 * Logs a throwable but does not notify the user in any way.
 * Normally it is a bad practice not to notify the user if something goes wrong.
 * @author Geoffrey De Smet
 * @since 0.3
 */
public class SilentExceptionHandler<T extends SilentExceptionHandler<T>> extends AbstractLoggingExceptionHandler<T> {

    /**
     * Does nothing.
     */
    public void notifyUserAboutException(Thread thread, Throwable throwable) {
    }

}
