package org.valkyriercp.command.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.valkyriercp.widget.Widget;

import javax.swing.*;

/**
 * Base class for commands that use widgets. The widget can be injected or found in the context through its id.
 */
public abstract class AbstractWidgetCommand extends ApplicationWindowAwareCommand
        implements
        ApplicationContextAware
{

    private String widgetBeanId = null;

    private Widget widget;

    private ApplicationContext applicationContext;

    protected AbstractWidgetCommand(String commandId) {
        super(commandId);
    }

    public void setWidget(Widget widget)
    {
        this.widget = widget;
    }

    protected Widget getWidget()
    {
        if (this.widget == null && this.widgetBeanId != null)
            this.widget = applicationContext.getBean(widgetBeanId, Widget.class);
        return this.widget;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    protected final JComponent getWidgetComponent()
    {
        if (getWidget() == null)
            return new JPanel();
        return getWidget().getComponent();
    }

    public String getWidgetBeanId()
    {
        return widgetBeanId;
    }

    public void setWidgetBeanId(String widgetBeanId)
    {
        this.widgetBeanId = widgetBeanId;
    }
}


