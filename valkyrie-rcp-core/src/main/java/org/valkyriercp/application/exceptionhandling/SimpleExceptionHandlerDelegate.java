package org.valkyriercp.application.exceptionhandling;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Handles the thrownTrowable by the exception handler if it is an instance of one of the throwableClassList.
 * Note: Also subclasses of the classes in the throwableClassList will be handled by the exception handler.
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class SimpleExceptionHandlerDelegate<SELF extends SimpleExceptionHandlerDelegate<SELF>> extends AbstractExceptionHandlerDelegate<SELF> {

    private List<Class<? extends Throwable>> throwableClassList;

    public SimpleExceptionHandlerDelegate() {
    }

    public SimpleExceptionHandlerDelegate(Class<? extends Throwable> throwableClass,
                                          Thread.UncaughtExceptionHandler exceptionHandler) {
        this(Lists.<Class<? extends Throwable>>newArrayList(throwableClass), exceptionHandler);
    }

    public SimpleExceptionHandlerDelegate(List<Class<? extends Throwable>> throwableClassList,
                                          Thread.UncaughtExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.throwableClassList = throwableClassList;
    }

    public void setThrowableClass(Class throwableClass) {
        setThrowableClassList(Lists.<Class<? extends Throwable>>newArrayList(throwableClass));
    }

    public SELF withThrowableClass(Class throwableClass) {
        setThrowableClass(throwableClass);
        return self();
    }


    public void setThrowableClassList(List<Class<? extends Throwable>> throwableClassList) {
        this.throwableClassList = throwableClassList;
    }

    public SELF withThrowableClassList(List<Class<? extends Throwable>> throwableClassList) {
        setThrowableClassList(throwableClassList);
        return self();
    }

    public boolean hasAppropriateHandlerPurged(Throwable throwable) {
        for (Class throwableClass : throwableClassList) {
            if (throwableClass.isInstance(throwable)) {
                return true;
            }
        }
        return false;
    }

}

