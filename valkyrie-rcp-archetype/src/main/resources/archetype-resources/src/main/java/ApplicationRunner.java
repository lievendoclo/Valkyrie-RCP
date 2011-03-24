package ${package};

import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.support.ApplicationLauncher;

public class ApplicationRunner {
    public static void main(String[] args) {
        new ApplicationLauncher(DefaultSplashScreenConfig.class, "/META-INF/valkyrie/context.xml");
    }
}