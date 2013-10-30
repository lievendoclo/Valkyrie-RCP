package org.valkyriercp.application.splash;

import org.valkyriercp.progress.ProgressMonitor;

/**
 * A <code>MonitoringSplashScreen</code> can be used to provide feedback of the application startup phase
 *
 * @author Mathias Broekelmann
 * @since 0.3
 */
public interface MonitoringSplashScreen extends SplashScreen {

    /**
     * Returns this <code>SplashScreen</code>'s <code>ProgressMonitor</code>. Implementors wishing to show the
     * progress to the user should return a <code>ProgressMonitor</code> that updates the user interface.
     *
     * @return the <code>ProgressMonitor</code>, not null.
     */
    ProgressMonitor getProgressMonitor();
}
