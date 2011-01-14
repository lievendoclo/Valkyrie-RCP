package org.valkyriercp.form.binding.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.DecoratedControlBinding;

import javax.swing.*;

/**
 * A convenience class that decorates the component produced from a source
 * Binding with a JScrollPane.  Useful for placing JList (and JTextArea)
 * bindings, among others, in a scroll pane when needed.
 *
 * @author Andy DePue
 */
public class ScrollPaneDecoratedBinding extends DecoratedControlBinding {

    public ScrollPaneDecoratedBinding(ComponentFactory componentFactory, final Binding source) {
        this(source, componentFactory.createScrollPane(source.getControl()));
    }

    public ScrollPaneDecoratedBinding(ComponentFactory componentFactory, final Binding source, final int vsbPolicy, final int hsbPolicy) {
        this(source, componentFactory.createScrollPane(source.getControl(), vsbPolicy,
                hsbPolicy));
    }

    public ScrollPaneDecoratedBinding(final Binding source, JComponent scrollPane) {
        super(source, scrollPane);
    }
}
