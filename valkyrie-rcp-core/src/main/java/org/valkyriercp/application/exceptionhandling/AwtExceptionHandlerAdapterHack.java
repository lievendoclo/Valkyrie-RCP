package org.valkyriercp.application.exceptionhandling;

import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

/**
 * Uncaught exception handler designed to work with JDK 1.4 and 1.5's primitive API for registering
 * exception handlers for the event thread.
 *
 * It's impossible to set an exception handler for the event thread in jdk 1.4 (and 1.5).
 * See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4714232
 * So this effectively only works in Sun's JDK.
 *
 * @author Geoffrey De Smet
 * @author Keith Donald
 * @since 0.3
 */
public class AwtExceptionHandlerAdapterHack {

    private static final String SUN_AWT_EXCEPTION_HANDLER_KEY = "sun.awt.exception.handler";

    /**
     * Since Sun's JDK constructs the instance, its impossible to inject dependencies into it,
     * except by a static reference like this.
     */
    private static RegisterableExceptionHandler exceptionHandlerDelegate = null;

    /**
     * Sets the {@link #SUN_AWT_EXCEPTION_HANDLER_KEY} system property to register this class as the event thread's
     * exception handler.  When called back, this class simply forwards to the delegate.
     * @param exceptionHandlerDelegate the "real" exception handler to delegate to when an uncaught exception occurs.
     */
    public static void registerExceptionHandler(RegisterableExceptionHandler exceptionHandlerDelegate) {
        AwtExceptionHandlerAdapterHack.exceptionHandlerDelegate = exceptionHandlerDelegate;
        // Registers this class with the system properties so Sun's JDK can pick it up.  Always sets even if previously set.
        System.getProperties().put(SUN_AWT_EXCEPTION_HANDLER_KEY, AwtExceptionHandlerAdapterHack.class.getName());
    }

    /**
     * No-arg constructor required so Sun's JDK can construct the instance.
     */
    public AwtExceptionHandlerAdapterHack() {
    }

    public void handle(Throwable throwable) {
        if (exceptionHandlerDelegate == null) {
            LoggerFactory.getLogger(getClass()).error("No uncaughtExceptionHandler set while handling throwable.", throwable);
        }
        exceptionHandlerDelegate.uncaughtException(Thread.currentThread(), throwable);
    }

}
