package org.valkyriercp.application.config.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;

/**
 * A bean post processor that will set the {@link ApplicationWindow} of any beans implementing the
 * {@link ApplicationWindowAware} interface.
 *
 * @author Keith Donald
 *
 * @see BeanPostProcessor
 *
 */
class ApplicationWindowSetter implements BeanPostProcessor {

    private final ApplicationWindow window;

    /**
     * Creates a new {@code ApplicationWindowSetter} that will set the given application window
     * on any beans processed if they implement the {@link ApplicationWindowAware} interface.
     *
     * @param window The application window to be set on the beans being processed. Must not be null.
     *
     * @throws IllegalArgumentException if {@code window} is null.
     */
    public ApplicationWindowSetter(ApplicationWindow window) {
        Assert.notNull(window, "window");
        this.window = window;
    }

    /**
     * Default implementation, performs no action on the given bean.
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * If the given bean is an implementation of {@link ApplicationWindowAware}, it will have its
     * application window set to the window provided to this instance at construction time.
     *
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationWindowAware) {
            ((ApplicationWindowAware)bean).setApplicationWindow(window);
        }
        return bean;
    }

}

