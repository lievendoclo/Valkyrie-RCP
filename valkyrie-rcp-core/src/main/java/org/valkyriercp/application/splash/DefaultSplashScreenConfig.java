package org.valkyriercp.application.splash;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class DefaultSplashScreenConfig extends AbstractSplashScreenConfig {
    @Override
    public SplashScreen splashScreen() {
        SimpleSplashScreen simpleSplashScreen = new SimpleSplashScreen(new ClassPathResource("/org/valkyriercp/images/splash/default.jpg"));
        return simpleSplashScreen;
    }
}
