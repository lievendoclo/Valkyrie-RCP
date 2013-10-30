package org.valkyriercp.application.support;

import org.jdesktop.swingx.JXTitledPanel;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.factory.AbstractControlFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JXPageComponentPane extends AbstractControlFactory implements PageComponentPane {
    private PageComponent component;

    private PropertyChangeListener updater = new PropertyChangeListener() {
        public void propertyChange( PropertyChangeEvent evt ) {
            handleViewPropertyChange();
        }
    };

    public JXPageComponentPane( PageComponent component ) {
        this.component = component;
        this.component.addPropertyChangeListener( updater );
    }

    public PageComponent getPageComponent() {
        return component;
    }

    protected JComponent createControl() {
        JXTitledPanel panel = new JXTitledPanel(component.getDisplayName(), component.getControl());
        return panel;
    }

    protected JToolBar createViewToolBar() {
        // todo
        return null;
    }

    public void propertyChange( PropertyChangeEvent evt ) {
        handleViewPropertyChange();
    }

    protected void handleViewPropertyChange() {
        JXTitledPanel frame = (JXTitledPanel) getControl();
        frame.setTitle( component.getDisplayName() );
        frame.setToolTipText( component.getCaption() );
    }
}
