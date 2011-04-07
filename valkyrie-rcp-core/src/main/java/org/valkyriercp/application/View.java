package org.valkyriercp.application;

/**
 * A view is a panel-like component displayed within an area on the page associated with an application window. There
 * can be multiple views per page; a single view can only be displayed once on a single page.
 *
 * View instances encapsulate the creation of and access to the visual presentation of the underlying control. A view's
 * descriptor -- which is effectively a singleton -- can be asked to instantiate new instances of a single view for
 * display within an application with multiple windows. In other words, a single view instance is never shared between
 * windows.
 *
 * @author Keith Donald
 */
public interface View extends PageComponent {
    /**
     * Sets the input for this {@link View}. This method may be implemented so this {@link View} can update it's own
     * state (ui) when this method is called.
     * <p>
     * Normally this method is called from {@link ApplicationPage#showView(String, Object)}, but may also be called
     * directly.
     *
     * @param input
     *            the input, can be <code>null</code>
     */
    void setInput(Object input);

    ViewDescriptor getDescriptor();

    ViewDescriptor getDescriptor(String id);
}
