package org.valkyriercp.application.splash;

/**
 * A <code>SplashScreen</code> is shown to the user during application startup.
 *
 * @author Peter De Bruycker
 */
public interface SplashScreen {
    /**
     * Shows this <code>SplashScreen</code>.
     */
    void splash();

    /**
     * Disposes this <code>SplashScreen</code>, freeing any system resources
     * that it may be using.
     */
    void dispose();
}
