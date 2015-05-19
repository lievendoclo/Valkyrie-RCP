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
