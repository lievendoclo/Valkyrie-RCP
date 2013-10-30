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
