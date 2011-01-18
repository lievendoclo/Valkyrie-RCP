package org.valkyriercp.application.support;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;

import javax.swing.*;

/**
 * <code>PageComponentPane</code> implementation that adds no extra decoration to the
 * contained <code>PageComponentPane</code>
 *
 * @author Peter De Bruycker
 */
public class SimplePageComponentPane implements PageComponentPane {

    private final PageComponent pageComponent;

    public SimplePageComponentPane( PageComponent component ) {
        pageComponent = component;
    }

    public PageComponent getPageComponent() {
        return pageComponent;
    }

    public JComponent getControl() {
        return pageComponent.getControl();
    }
}