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
package org.valkyriercp.application.config.support;

import org.valkyriercp.application.ApplicationWindow;

/**
 * Interface to be implemented by application components that want to be made aware
 * of the {@link ApplicationWindow} that they have been created within.
 *
 * Note that simply implementing this interface is not enough to ensure that your components
 * are injected with the {@code ApplicationWindow} reference. This is dependent on the factory
 * or framework code that instantiates the components. For example, the
 * {@link DefaultApplicationLifecycleAdvisor} class is a framework component that can instantiate
 * command objects for use in toolbars, menus etc. If any of these commands implement
 * {@link ApplicationWindowAware}, the lifecycle advisor will inject the appropriate
 * {@link ApplicationWindow} into them.
 */
public interface ApplicationWindowAware {

    /**
     * Sets the reference to the application window that this object was created within.
     *
     * @param window The application window containing this component.
     */
    public void setApplicationWindow(ApplicationWindow window);

}
