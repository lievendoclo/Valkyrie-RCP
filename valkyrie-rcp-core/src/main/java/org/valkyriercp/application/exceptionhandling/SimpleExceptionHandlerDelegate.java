package org.valkyriercp.application.exceptionhandling;

import java.util.Collections;
import java.util.List;

/**
 * Handles the thrownTrowable by the exception handler if it is an instance of one of the throwableClassList.
 * Note: Also subclasses of the classes in the throwableClassList will be handled by the exception handler.
 *
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class SimpleExceptionHandlerDelegate extends AbstractExceptionHandlerDelegate {

    private List<Class> throwableClassList;

    public SimpleExceptionHandlerDelegate() {
    }

    public SimpleExceptionHandlerDelegate(Class throwableClass,
            Thread.UncaughtExceptionHandler exceptionHandler) {
        this(Collections.singletonList(throwableClass), exceptionHandler);
    }

    public SimpleExceptionHandlerDelegate(List<Class> throwableClassList,
            Thread.UncaughtExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.throwableClassList = throwableClassList;
    }

    public void setThrowableClass(Class throwableClass) {
        setThrowableClassList(Collections.singletonList(throwableClass));
    }

    public void setThrowableClassList(List<Class> throwableClassList) {
        this.throwableClassList = throwableClassList;
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

