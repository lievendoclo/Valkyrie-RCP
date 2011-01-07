package org.valkyriercp.application.support;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.ViewDescriptorRegistry;

import java.util.Map;

/**
 * A simple {@link ViewDescriptorRegistry} implementation that pulls singleton view definitions out
 * of a spring application context. This class is intended to be managed by a Spring IoC container.
 *
 *
 * @author Keith Donald
 * @author Kevin Stembridge
 */
@Component
public class BeanFactoryViewDescriptorRegistry implements ViewDescriptorRegistry {
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * {@inheritDoc}
     */
    public ViewDescriptor[] getViewDescriptors() {
        Map beans = applicationContext.getBeansOfType(ViewDescriptor.class, false, false);
        return (ViewDescriptor[])beans.values().toArray(new ViewDescriptor[beans.size()]);
    }

    /**
     * Returns the view descriptor with the given identifier, or null if no such bean definition
     * with the given name exists in the current application context.
     *
     * @param viewName The bean name of the view descriptor that is to be retrieved from the
     * underlying application context. Must not be null.
     *
     * @throws IllegalArgumentException if {@code viewName} is null.
     * @throws org.springframework.beans.factory.BeanNotOfRequiredTypeException if the bean retrieved from the underlying application
     * context is not of type {@link ViewDescriptor}.
     *
     */
    public ViewDescriptor getViewDescriptor(String viewName) {

        Assert.notNull(viewName, "viewName");

        try {
            return (ViewDescriptor) applicationContext.getBean(viewName, ViewDescriptor.class);
        }
        catch (NoSuchBeanDefinitionException e) {
            return null;
        }

    }

}

