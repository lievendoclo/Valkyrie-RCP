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

