package org.valkyriercp.application;

import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.PropertyChangePublisher;
import org.valkyriercp.core.VisualizedElement;
import org.valkyriercp.factory.ControlFactory;

/**
 * A page component is displayed within an area on the page
 * associated with an application window. There can be multiple components
 * per page; a single page component can only be displayed once on a
 * single page.
 *
 * Components instances encapsulate the creation of and access to the visual
 * presentation of the underlying control. A component's descriptor --
 * which is effectively a singleton -- can be asked to instantiate new
 * instances of a single page component for display within an application
 * with multiple windows. In other words, a single page component instance is
 * never shared between windows.
 *
 * @author Keith Donald
 */
public interface PageComponent extends PropertyChangePublisher, DescribedElement, VisualizedElement, ControlFactory {
    public PageComponentContext getContext();

    public void componentOpened();

    public void componentFocusGained();

    public void componentFocusLost();

    public void componentClosed();

    public void dispose();

    public void setContext(PageComponentContext context);

    public void setDescriptor(PageComponentDescriptor pageComponentDescriptor);

    public String getId();

    boolean canClose();

    public void close();
}
