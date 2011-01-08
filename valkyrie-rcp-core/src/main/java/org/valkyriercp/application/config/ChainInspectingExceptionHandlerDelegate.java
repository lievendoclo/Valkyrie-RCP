package org.valkyriercp.application.config;

import org.valkyriercp.application.exceptionhandling.AbstractExceptionHandlerDelegate;

import java.util.Iterator;
import java.util.List;

/**
 * Checks the exception chain to determine if it wants to handle it.
 * In most cases this class is overkill and SimpleExceptionHandlerDelegate with a purger will suffice.
 * Think about it: just purging out MySpecificLoginException suffices most of the time,
 * it doesn't really matter that it's wrapped in MyWrapperException.
 *
 * @see org.valkyriercp.application.exceptionhandling.SimpleExceptionHandlerDelegate
 * @see org.valkyriercp.application.exceptionhandling.DefaultExceptionPurger
 * @author Geoffrey De Smet
 * @since 0.3.0
 */
public class ChainInspectingExceptionHandlerDelegate extends AbstractExceptionHandlerDelegate {

    private List<ChainPart> chainPartList;

    public ChainInspectingExceptionHandlerDelegate() {
    }

    public ChainInspectingExceptionHandlerDelegate(List<ChainPart> chainPartList,
            Thread.UncaughtExceptionHandler exceptionHandler) {
        super(exceptionHandler);
        this.chainPartList = chainPartList;
    }

    public void setChainPartList(List<ChainPart> chainPartList) {
        this.chainPartList = chainPartList;
    }

    @Override
    public boolean hasAppropriateHandlerPurged(Throwable root) {
        Throwable e = root;
        boolean check = true;
        Iterator<ChainPart> it = chainPartList.iterator();
        while (check && it.hasNext()) {
            ChainPart chainPart = it.next();
            e = findChainPartThrowable(chainPart, e);
            if (e == null) {
                check = false;
            } else {
                // get cause of e (and null at end of chain)
                e = (e.getCause() == e) ? null : e.getCause();
                if (e == null) {
                    check = check && !it.hasNext();
                }
            }
        }
        return check;
    }

//  public boolean hasAppropriateHandlerPurgedAlternativeImplementation(Throwable root) {
//      Throwable e = root;
//      for (ChainPart chainPart : chainPartList) {
//          if (e == null) {
//              return false; // nothing left to check
//          }
//          e = findChainPartThrowable(chainPart, e);
//          if (e == null) {
//              return false; // not found
//          }
//          // get cause of e (and null at end of chain)
//          e = (e.getCause() == e) ? null : e.getCause();
//      }
//      return true;
//  }

    public Throwable findChainPartThrowable(ChainPart chainPart, Throwable firstThrowable) {
        Throwable e = firstThrowable;
        int relativeDepth = 0;
        int minimumRelativeDepth = chainPart.getMinimumRelativeDepth();
        int maximumRelativeDepth = chainPart.getMaximumRelativeDepth();
        while (!chainPart.getThrowableClass().isInstance(e)
                || (relativeDepth < minimumRelativeDepth)) {
            relativeDepth++;
            Throwable cause = e.getCause();
            if (cause == null || cause == e) {
                return null; // Chain is to short to find the chainPart
            } else {
                e = cause;
            }
            if ((maximumRelativeDepth >= 0)
                    && (relativeDepth > maximumRelativeDepth)) {
                return null; // We did not find the chainPart early enough
            }
        }
        return e;
    }


    @Override
    public void uncaughtExceptionPurged(Thread thread, Throwable throwable) {
        exceptionHandler.uncaughtException(thread, throwable);
    }

    public static class ChainPart {

        private Class throwableClass = Throwable.class;
        private int minimumRelativeDepth = 0;
        private int maximumRelativeDepth = -1;

        public ChainPart() {}

        public ChainPart(Class throwableClass) {
            this.throwableClass = throwableClass;
        }

        public ChainPart(Class throwableClass, int relativeDepth) {
            this(throwableClass, relativeDepth, relativeDepth);
        }

        public ChainPart(Class throwableClass, int minimumRelativeDepth, int maximumRelativeDepth) {
            this.throwableClass = throwableClass;
            this.minimumRelativeDepth = minimumRelativeDepth;
            this.maximumRelativeDepth = maximumRelativeDepth;
        }

        public Class getThrowableClass() {
            return throwableClass;
        }

        public void setThrowableClass(Class throwableClass) {
            this.throwableClass = throwableClass;
        }

        public void setRelativeDepth(int relativeDepth) {
            setMinimumRelativeDepth(relativeDepth);
            setMaximumRelativeDepth(relativeDepth);
        }

        public int getMinimumRelativeDepth() {
            return minimumRelativeDepth;
        }

        public void setMinimumRelativeDepth(int minimumRelativeDepth) {
            this.minimumRelativeDepth = minimumRelativeDepth;
        }

        public int getMaximumRelativeDepth() {
            return maximumRelativeDepth;
        }

        public void setMaximumRelativeDepth(int maximumRelativeDepth) {
            this.maximumRelativeDepth = maximumRelativeDepth;
        }

    }

}

