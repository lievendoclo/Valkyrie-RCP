package org.valkyriercp.application.exceptionhandling;

/**
 * Purges a throwable, ussually by looking into it's chain.
 * Usefull for unwrapping WrapEverythingException etc.
 *
 * @see DefaultExceptionPurger
 */
public interface ExceptionPurger {

    /**
     * Purges the throwable to unwrap it to find the most suitable throwable to evaluate or handle.
     *
     * @param e the root exception or error
     * @return e or a chained Throwable which is part of e's chain
     */
    Throwable purge(Throwable e);

}