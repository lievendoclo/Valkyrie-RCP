package org.valkyriercp.application.config.support;

import org.valkyriercp.component.JideOssComponentFactory;
import org.valkyriercp.component.JideOverlayService;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.binding.jide.JideBindingFactoryProvider;

public abstract class AbstractJideOssApplicationConfig extends AbstractApplicationConfig {
    @Override
    public ComponentFactory componentFactory() {
        return new JideOssComponentFactory();
    }

    @Override
    public BindingFactoryProvider bindingFactoryProvider() {
        return new JideBindingFactoryProvider();
    }

    @Override
    public OverlayService overlayService() {
        return new JideOverlayService();
    }
}
