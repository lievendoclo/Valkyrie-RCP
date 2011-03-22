package org.valkyriercp.application.support;

import com.jidesoft.swing.JideTabbedPane;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageLayoutBuilder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;

public class JideTabbedApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {
    private JideTabbedPane tabbedPane;
    private int tabPlacement = -1;
    private int tabLayoutPolicy = -1;
    private int tabShape = -1;
    private int colorTheme = -1;
    private boolean showCloseButton = false;

    private boolean addingComponent;

    protected JComponent createControl() {
        tabbedPane = new JideTabbedPane();
        if (tabPlacement != -1) {
            tabbedPane.setTabPlacement(tabPlacement);
        }
        if (tabLayoutPolicy != -1) {
            tabbedPane.setTabLayoutPolicy(tabLayoutPolicy);
        }
        if (tabShape != -1) {
            tabbedPane.setTabShape(tabShape);
        }
        if (colorTheme != -1) {
            tabbedPane.setColorTheme(colorTheme);
        }
        tabbedPane.setShowCloseButtonOnTab(showCloseButton);
        tabbedPane.setShowCloseButtonOnSelectedTab(showCloseButton);
        tabbedPane.setCloseAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close(getComponent(tabbedPane.getSelectedIndex()));
            }
        });
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // if we're adding a component, ignore change of active component
                if (!addingComponent && tabbedPane.getSelectedIndex() >= 0) {
                    setActiveComponent(getComponent(tabbedPane.getSelectedIndex()));
                }
            }
        });

        this.getPageDescriptor().buildInitialLayout(this);

        return tabbedPane;
    }

    protected void updatePageComponentProperties(PageComponent pageComponent) {
        int index = indexOf(pageComponent);

        tabbedPane.setIconAt(index, pageComponent.getIcon());
        tabbedPane.setTitleAt(index, pageComponent.getDisplayName());
        tabbedPane.setToolTipTextAt(index, pageComponent.getCaption());
    }

    public void addView(String viewDescriptorId) {
        showView(viewDescriptorId);
    }

    protected void doAddPageComponent(PageComponent pageComponent) {
        try {
            addingComponent = true;
            tabbedPane.addTab(pageComponent.getDisplayName(), pageComponent.getIcon(), pageComponent.getContext()
                    .getPane().getPageComponent().getControl(), pageComponent.getCaption());
        } finally {
            addingComponent = false;
        }
    }

    protected void doRemovePageComponent(PageComponent pageComponent) {
        if(getPageComponents().size() > 0)
             tabbedPane.removeTabAt(indexOf(pageComponent));
    }

    protected boolean giveFocusTo(PageComponent pageComponent) {
        int componentIndex = indexOf(pageComponent);
        if (componentIndex < 0) {
            return false;
        }

        tabbedPane.setSelectedIndex(componentIndex);
        return true;
    }

    private int indexOf(PageComponent component) {
        return getPageComponents().indexOf(component);
    }

    private PageComponent getComponent(int index) {
        return getPageComponents().get(index);
    }

    public void setTabPlacement(int tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public void setTabLayoutPolicy(int tabLayoutPolicy) {
        this.tabLayoutPolicy = tabLayoutPolicy;
    }

    public void setTabShape(int tabShape) {
        this.tabShape = tabShape;
    }

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }
}
