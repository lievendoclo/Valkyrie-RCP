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

    private PropertyChangeListener updater = new PropertyChangeListener() {
        public void propertyChange( PropertyChangeEvent evt ) {
            handleViewPropertyChange();
        }
    };

    public DefaultPageComponentPane( PageComponent component ) {
        this.component = component;
        this.component.addPropertyChangeListener( updater );
    }

    public PageComponent getPageComponent() {
        return component;
    }

    protected JComponent createControl() {
        return new SimpleInternalFrame( component.getIcon(), component.getDisplayName(), createViewToolBar(), component
                .getControl() );
    }

    protected JToolBar createViewToolBar() {
        // todo
        return null;
    }

    public void propertyChange( PropertyChangeEvent evt ) {
        handleViewPropertyChange();
    }

    protected void handleViewPropertyChange() {
        SimpleInternalFrame frame = (SimpleInternalFrame) getControl();
        frame.setTitle( component.getDisplayName() );
        frame.setFrameIcon( component.getIcon() );
        frame.setToolTipText( component.getCaption() );
    }
}

