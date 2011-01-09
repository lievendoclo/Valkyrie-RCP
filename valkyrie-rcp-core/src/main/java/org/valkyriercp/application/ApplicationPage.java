package org.valkyriercp.application;

import org.valkyriercp.factory.ControlFactory;

import java.util.List;

public interface ApplicationPage extends ControlFactory {
    String getId();
    ApplicationWindow getWindow();
    boolean close();
    boolean close(PageComponent pageComponent);
    public List<PageComponent> getPageComponents();
    void addPageComponentListener(PageComponentListener listener);
    void removePageComponentListener(PageComponentListener listener);
    /**
     * Shows the {@link View} with the given id.
     * <p>
     * If the {@link View} is already opened, the view will be reused.
     * <p>
     * NOTE: this is NOT the same as calling <code>this.showView(id, null)</code>.
     *
     * @param id
     *            the view id, cannot be empty
     *
     * @return the {@link View} that is shown
     */
    View showView(String id);

    /**
     * Shows the {@link View} with the given id, and passes the input to the {@link View}, by calling
     * {@link View#setInput(Object)}.
     * <p>
     * If the {@link View} is already opened, the view will be reused.
     *
     * @param id
     *            the view id, cannot be empty
     * @param input
     *            the input, can be <code>null</code>
     *
     * @return the {@link View} that is shown
     */
    View showView(String id, Object input);

    /**
     * Returns the {@link View} with the given id. Returns <code>null</code> if no {@link View} with the given id is
     * shown.
     * <p>
     * This method is "generified" to avoid extra casts when calling this method:
     *
     * <pre>
     * ApplicationPage page = ...; // get a reference to the ApplicationPage
     * InitialView initialView = page.getView(&quot;initialView&quot;);
     * </pre>
     *
     * @param id
     *            the id, cannot be <code>null</code>
     * @return the {@link View}, or <code>null</code>
     */
    <T extends View> T getView(String id);
}
