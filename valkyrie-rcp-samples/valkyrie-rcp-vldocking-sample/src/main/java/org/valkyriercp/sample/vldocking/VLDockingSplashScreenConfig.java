package org.valkyriercp.sample.vldocking;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.splash.ProgressSplashScreen;
import org.valkyriercp.application.splash.SplashScreen;

@Configuration
public class VLDockingSplashScreenConfig extends DefaultSplashScreenConfig {
    @Override
    public SplashScreen splashScreen() {
        ProgressSplashScreen progressSplashScreen = new ProgressSplashScreen();
        progressSplashScreen.setImageResourcePath(new ClassPathResource("/org/valkyriercp/images/splash/default.jpg"));
        return progressSplashScreen;
    }
}
