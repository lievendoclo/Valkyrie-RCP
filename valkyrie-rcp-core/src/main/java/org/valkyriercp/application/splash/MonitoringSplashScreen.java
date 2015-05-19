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

import org.valkyriercp.progress.ProgressMonitor;

/**
 * A <code>MonitoringSplashScreen</code> can be used to provide feedback of the application startup phase
 *
 * @author Mathias Broekelmann
 * @since 0.3
 */
public interface MonitoringSplashScreen extends SplashScreen {

    /**
     * Returns this <code>SplashScreen</code>'s <code>ProgressMonitor</code>. Implementors wishing to show the
     * progress to the user should return a <code>ProgressMonitor</code> that updates the user interface.
     *
     * @return the <code>ProgressMonitor</code>, not null.
     */
    ProgressMonitor getProgressMonitor();
}
