package org.valkyriercp.application.exceptionhandling;

/**
 * Superclass for delegate implementations
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public abstract class AbstractExceptionHandlerDelegate implements ExceptionHandlerDelegate {

    protected Thread.UncaughtExceptionHandler exceptionHandler;
    protected ExceptionPurger exceptionPurger = null;
    protected boolean purgeOnAppropriateCheck = true;
    protected boolean purgeOnHandling = true;

    public AbstractExceptionHandlerDelegate() {
    }

    public AbstractExceptionHandlerDelegate(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * If set the throwable will first be purged before doing the approriate check or handling it.
     * @param exceptionPurger
     */
    public void setExceptionPurger(ExceptionPurger exceptionPurger) {
        this.exceptionPurger = exceptionPurger;
    }

    public void setPurgeOnAppropriateCheck(boolean purgeOnAppropriateCheck) {
        this.purgeOnAppropriateCheck = purgeOnAppropriateCheck;
    }

    public void setPurgeOnHandling(boolean purgeOnHandling) {
        this.purgeOnHandling = purgeOnHandling;
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

