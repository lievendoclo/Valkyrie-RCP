package org.valkyriercp.sample.showcase;

import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.support.ApplicationLauncher;

public class ShowcaseRunner {
    public static void main(String[] args) {
        new ApplicationLauncher(DefaultSplashScreenConfig.class, ShowcaseApplicationConfig.class);
    }
}
