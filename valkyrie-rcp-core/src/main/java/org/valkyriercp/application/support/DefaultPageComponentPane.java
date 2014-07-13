package org.valkyriercp.application.support;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.component.SimpleInternalFrame;
import org.valkyriercp.factory.AbstractControlFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A <code>DefaultPageComponentPane</code> puts the <code>PageComponent</code> inside
 * a <code>SimpleInternalFrame</code>.
 *
 * @author Peter De Bruycker
 *
 */
public class DefaultPageComponentPane extends AbstractControlFactory implements PageComponentPane {
    private PageComponent component;
    private SimpleInternalFrame control;

    public DefaultPageComponentPane( PageComponent component ) {
        this.component = component;
    }

    public PageComponent getPageComponent() {
        return component;
    }

    protected JComponent createControl() {
        control = new SimpleInternalFrame( component.getIcon(), component.getDisplayName(), createViewToolBar(), component
                .getControl() );
        control.setTitle( component.getDisplayName() );
        control.setFrameIcon( component.getIcon() );
        control.setToolTipText( component.getCaption() );
        return control;
    }

    protected JToolBar createViewToolBar() {
        // todo
        return null;
    }
}

