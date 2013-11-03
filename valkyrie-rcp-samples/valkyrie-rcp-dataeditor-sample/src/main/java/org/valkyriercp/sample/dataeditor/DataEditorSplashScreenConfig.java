package org.valkyriercp.sample.dataeditor;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.application.splash.AbstractSplashScreenConfig;
import org.valkyriercp.application.splash.SplashScreen;
import org.valkyriercp.sample.dataeditor.ui.DataEditorSplash;

@Configuration
public class DataEditorSplashScreenConfig extends AbstractSplashScreenConfig {

    public SplashScreen splashScreen() {
        DataEditorSplash dataEditorSplash = new DataEditorSplash();
        dataEditorSplash.setImage(new ClassPathResource("/org/valkyriercp/sample/dataeditor/images/splash-screen.jpg"));
        return dataEditorSplash;
    }
}
