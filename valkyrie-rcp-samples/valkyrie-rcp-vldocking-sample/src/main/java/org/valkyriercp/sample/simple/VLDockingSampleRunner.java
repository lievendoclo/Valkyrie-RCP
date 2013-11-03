package org.valkyriercp.sample.simple;

import org.valkyriercp.application.support.ApplicationLauncher;

public class VLDockingSampleRunner {
    public static void main(String[] args) {
        new ApplicationLauncher(VLDockingSplashScreenConfig.class, VLDockingSampleApplicationConfig.class);
    }
}
