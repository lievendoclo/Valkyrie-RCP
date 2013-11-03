package org.valkyriercp.application.splash;

import org.springframework.context.annotation.Bean;

public class AbstractSplashScreenConfig implements SplashScreenConfig {
    @Bean
    public SplashScreen splashScreen() {
        return new SimpleSplashScreen();
    }


}
