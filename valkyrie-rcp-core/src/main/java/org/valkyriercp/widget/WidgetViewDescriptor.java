package org.valkyriercp.widget;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.application.support.DefaultViewDescriptor;

public final class WidgetViewDescriptor extends DefaultViewDescriptor
{
    /**
     * Widget to create the view.
     */
    private Widget widget;

    public WidgetViewDescriptor(String id, Widget widget)
    {
        setId(id);
        setViewClass(WidgetView.class);
        this.widget = widget;
    }

    /**
     * {@inheritDoc}
     */
    public PageComponent createPageComponent()
    {
        AbstractView sv = new WidgetView(this.widget);
        sv.setDescriptor(this);
        return sv;
    }
}
