package org.valkyriercp.application.docking;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.util.ObjectUtils;

import java.util.Map;

/**
 * A bean post processor that automatically recognizes view descriptors and make them VLDocking aware dependending on
 * view characteristics.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public class VLDockingBeanPostProcessor implements BeanPostProcessor {
    /**
     * The mapping between page components types and view descriptor templates.
     * <p/>
     * The key is the page component type and the value the name of the prototype bean with the VLDocking view
     * descriptor template.
     * <p/>
     * Key is intencionally defined as <code>String</code> in order to make it easy defining new view types.
     */
    private Map<String, String> viewDescriptorsTemplates;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        // throws BeansException {

        return bean;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Replaces those view descriptors not implementing {@link VLDockingViewDescriptor}.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        // throws BeansException {

        if (bean instanceof VLDockingViewDescriptor) {
            return bean;
        } else if (bean instanceof ViewDescriptor) {
            final ViewDescriptor sourceViewDescriptor = (ViewDescriptor) bean;
            final ViewDescriptor targetViewDescriptor = this.getTemplate(sourceViewDescriptor);

            // Copy source state
            ObjectUtils.shallowCopy(sourceViewDescriptor, targetViewDescriptor);

            return targetViewDescriptor;
        }

        return bean;
    }

    /**
     * Gets the configured template for the given view descriptor.
     *
     * @param viewDescriptor the view descriptor.
     * @return the more suitable template.
     */
    private VLDockingViewDescriptor getTemplate(ViewDescriptor viewDescriptor) {

        Assert.notNull(viewDescriptor, "viewDescriptor");

        final VLDockingViewDescriptor vlDockingViewDescriptor;

        vlDockingViewDescriptor = new VLDockingViewDescriptor();

        return vlDockingViewDescriptor;
    }
}

