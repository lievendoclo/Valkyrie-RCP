package org.valkyriercp.widget;

public interface WidgetProvider<T extends Widget> {
    T getWidget();
}
