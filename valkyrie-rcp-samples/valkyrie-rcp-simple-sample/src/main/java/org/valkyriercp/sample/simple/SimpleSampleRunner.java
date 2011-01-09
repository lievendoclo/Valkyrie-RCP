package org.valkyriercp.sample.simple;

import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.support.ApplicationLauncher;

public class SimpleSampleRunner {
    public static void main(String[] args) {
        new ApplicationLauncher(DefaultSplashScreenConfig.class, "/org/valkyriercp/sample/simple/context.xml");
    }
}
