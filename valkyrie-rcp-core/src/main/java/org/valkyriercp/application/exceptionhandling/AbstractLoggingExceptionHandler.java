package org.valkyriercp.application.exceptionhandling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ErrorCoded;

import java.sql.SQLException;

/**
 * Superclass of logging exception handlers.
 * It handles a throwable by logging it and notify it to the user.
 * Subclasses determine how it's notified to the user.
 * @author Geoffrey De Smet
 * @since 0.3
 */
public abstract class AbstractLoggingExceptionHandler extends AbstractRegisterableExceptionHandler {

    protected final transient Log logger = LogFactory.getLog(getClass());

    protected ExceptionPurger exceptionPurger = null;

    private final ThreadLocal<Boolean> alreadyHandlingExceptionOnThisThread = new ThreadLocal<Boolean>();

    /**
     * If set the throwable will first be purged before handling it.
     * @param exceptionPurger
     */
    public void setExceptionPurger(ExceptionPurger exceptionPurger) {
        this.exceptionPurger = exceptionPurger;
    }

    /**
     * Logs an exception and shows it to the user.
     * <p/>
     * Has infinite loop detection due to exception handling throwing an exception.
     * An infinite loop can occur in different cases:
     * <ul>
     * <li>
     *   Case 1: The ExceptionHandler throws an exception, which is handled by the ExceptionHandler,
     *   which throws an exception, ...
     * </li>
     * <li>
     *   Case 2: notifyUserAboutException uses a modal dialog that triggers the event queue to do a focus lost
     *   That focus lost throws a new exception, catched by the event queue, which delegates it to the ExceptionHandler,
     *   which still hasn't returned from handling the original exception!
     *   Then the ExceptionHandler uses a modal dialog again, focus lost, throws exception, ...
     *   This causes a StackOverflowError or a hang-application-by-starvation due to too many dialogs.</li>
     * </ul>
     */
    public final void uncaughtException(Thread thread, Throwable throwable) {
        Boolean infiniteLoopDetected = alreadyHandlingExceptionOnThisThread.get();
        if (infiniteLoopDetected != null && infiniteLoopDetected.booleanValue()) {
            // Preventing infinite loop case 2 (see javadoc)
            // The original uncaughtException method has not yet returned
            String detectionLogMessage = "Infinite exception handling loop detected. "
                    + "The ExceptionHandler has probably thrown the following exception itself:";
            try {
                logger.error(detectionLogMessage, throwable);
            } catch (Throwable ignoredThrowable) {
                System.err.println(detectionLogMessage);
                throwable.printStackTrace();
            }
            // Reset of alreadyHandlingExceptionOnThisThread not needed:
            // the original uncaughtException method will return and reset it.
            // Reset not wanted either: even if it's still looping, it is better to be able
            // to report the first exception and lose performance due to an infinite event loop
            // than to crash by hang-application-by-starvation due to too many dialogs.
            return; // Ignore the throwable
        }
        alreadyHandlingExceptionOnThisThread.set(Boolean.TRUE);
        try {
            processUncaughtException(thread, throwable);
        } catch (Throwable detectionThrowable) {
            // Preventing infinite loop case 1 (see javadoc)
            String detectionLogMessage = "The ExceptionHandler has thrown the following exception itself:";
            try {
                logger.error(detectionLogMessage, detectionThrowable);
            } catch (Throwable ignoredThrowable) {
                System.err.println(detectionLogMessage);
                detectionThrowable.printStackTrace();
            }
        } finally {
            alreadyHandlingExceptionOnThisThread.set(null);
        }
    }

    /**
     * Logs an exception and shows it to the user.
     */
    private void processUncaughtException(Thread thread, Throwable throwable) {
        if (exceptionPurger != null) {
            throwable = exceptionPurger.purge(throwable);
        }
        logException(thread, throwable);
        notifyUserAboutException(thread, throwable);
    }

    protected String extractErrorCode(Throwable throwable) {
        if (throwable instanceof ErrorCoded) {
            return ((ErrorCoded) throwable).getErrorCode();
        } else if (throwable instanceof SQLException) {
            return Integer.toString(((SQLException) throwable).getErrorCode());
        } else {
            return null;
        }
    }

    /**
     * Log an exception
     */
    public void logException(Thread thread, Throwable throwable) {
        String logMessage;
        String errorCode = extractErrorCode(throwable);
        if (errorCode != null) {
            logMessage = "Uncaught throwable handled with errorCode (" + errorCode + ").";
        } else {
            logMessage = "Uncaught throwable handled.";
        }
        logger.error(logMessage, throwable);
    }

    /**
     * Notify user about an exception
     */
    public abstract void notifyUserAboutException(Thread thread, Throwable throwable);

}
