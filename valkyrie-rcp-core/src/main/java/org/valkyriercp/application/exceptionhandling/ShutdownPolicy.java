package org.valkyriercp.application.exceptionhandling;

/**
 * A paramater value to determines if the user should or should not be asked or forced
 * to shutdown the application when an exception occurs.
 */
public enum ShutdownPolicy {

    NONE,
    ASK,
    OBLIGATE

}

