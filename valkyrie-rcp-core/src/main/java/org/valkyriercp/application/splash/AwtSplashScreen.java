package org.valkyriercp.application.splash;

import org.springframework.util.Assert;

public class AwtSplashScreen implements SplashScreen {
    private java.awt.SplashScreen splashScreen;

    public void dispose() {
        splashScreen.close();
    }

    public void splash() {
        splashScreen = java.awt.SplashScreen.getSplashScreen();
        Assert.state(splashScreen != null, "No splash screen defined on startup");
    }

    /**
     * Returns the <code>java.awt.SplashScreen</code> implementation that has been set at startup. The splashscreen
     * can then be used to perform custom painting, etc...
     *
     * @return the splash screen
     */
    protected java.awt.SplashScreen getSplashScreen() {
        return splashScreen;
    }
}

