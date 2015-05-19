/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.sample.vldocking;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.application.splash.DefaultSplashScreenConfig;
import org.valkyriercp.application.splash.ProgressSplashScreen;
import org.valkyriercp.application.splash.SplashScreen;

@Configuration
public class DockingFramesSplashScreenConfig extends DefaultSplashScreenConfig {
    @Override
    public SplashScreen splashScreen() {
        ProgressSplashScreen progressSplashScreen = new ProgressSplashScreen();
        progressSplashScreen.setImageResourcePath(new ClassPathResource("/org/valkyriercp/images/splash/default.jpg"));
        return progressSplashScreen;
    }
}
