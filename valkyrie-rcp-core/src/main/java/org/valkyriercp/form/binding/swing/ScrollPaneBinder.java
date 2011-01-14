package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.Map;

/**
 * A binder that binds a scroll pane and the scroll pane's view. If the
 * scroll pane does not have a view a default binding will be created and
 * set as the scroll pane's view.
 *
 * @author Oliver Hutchison
 */
public class ScrollPaneBinder extends AbstractBinder {

    private final BinderSelectionStrategy viewBinderSelectionStrategy;

    private final Class defaultViewType;

    /**
     * Constructs a new ScrollPaneBinder
     *
     * @param viewBinderSelectionStrategy the {@link BinderSelectionStrategy} which will be used
     * to select a Binder for the scrollpane's view component.
     * @param defaultViewType the type of the component that will be created and bound if the
     * scroll pane does not already have a view
     */
    public ScrollPaneBinder(BinderSelectionStrategy viewBinderSelectionStrategy, Class defaultViewType) {
        super(null);
        this.viewBinderSelectionStrategy = viewBinderSelectionStrategy;
        this.defaultViewType = defaultViewType;
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createScrollPane();
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JScrollPane, "Control must be an instance of JScrollPane.");
        JScrollPane scrollPane = (JScrollPane)control;
        Binding viewBinding = getViewBinding(scrollPane, formModel, formPropertyPath, context);
        return new ScrollPaneDecoratedBinding(viewBinding, scrollPane);
    }

    protected Binding getViewBinding(JScrollPane scrollPane, FormModel formModel, String formPropertyPath, Map context) {
        JComponent view = (JComponent)scrollPane.getViewport().getView();
        if (view == null) {
            Binding viewBinding = viewBinderSelectionStrategy.selectBinder(defaultViewType, formModel, formPropertyPath)
                    .bind(formModel, formPropertyPath, context);
            scrollPane.setViewportView(viewBinding.getControl());
            return viewBinding;
        }
        Binding existingBinding = (Binding)view.getClientProperty(BINDING_CLIENT_PROPERTY_KEY);
        if (existingBinding != null) {
            return existingBinding;
        }
        return viewBinderSelectionStrategy.selectBinder(view.getClass(), formModel, formPropertyPath).bind(
                view, formModel, formPropertyPath, context);
    }

    protected void validateContextKeys(Map context) {
        // do nothing as we pass the context on to
        // the scroll pane's view binder
    }
}
