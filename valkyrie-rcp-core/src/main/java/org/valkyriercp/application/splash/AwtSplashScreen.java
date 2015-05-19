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
package org.valkyriercp.application.splash;

import org.springframework.util.Assert;

public class AwtSplashScreen implements SplashScreen {
    private java.awt.SplashScreen splashScreen;

    public void dispose() {
        splashScreen.close();
    }

    public void splash() {
        splashScreen = java.awt.SplashScreen.getSplashScreen();
        Assert.state(splashScreen != null, "No splash screen defined on startup");
    }

    /**
     * Returns the <code>java.awt.SplashScreen</code> implementation that has been set at startup. The splashscreen
     * can then be used to perform custom painting, etc...
     *
     * @return the splash screen
     */
    protected java.awt.SplashScreen getSplashScreen() {
        return splashScreen;
    }
}

