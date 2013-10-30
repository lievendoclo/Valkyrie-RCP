package org.valkyriercp.application.support;

import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

public class JideTabbedApplicationPageFactory implements ApplicationPageFactory {
    private int tabPlacement = -1;
    private int tabLayoutPolicy = -1;
    private int tabShape = -1;
    private int colorTheme = -1;
    private boolean showCloseButton = false;

    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        JideTabbedApplicationPage page = new JideTabbedApplicationPage();
        page.setApplicationWindow(window);
        page.setDescriptor(descriptor);
        if (tabPlacement != -1) {
            page.setTabPlacement(tabPlacement);
        }
        if (tabLayoutPolicy != -1) {
            page.setTabLayoutPolicy(tabLayoutPolicy);
        }
        if (tabShape != -1) {
            page.setTabShape(tabShape);
        }
        if (colorTheme != -1) {
            page.setColorTheme(colorTheme);
        }
        page.setShowCloseButton(showCloseButton);
        return page;
    }

    public void setTabPlacement(int tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public int getTabPlacement() {
        return tabPlacement;
    }

    public int getTabLayoutPolicy() {
        return tabLayoutPolicy;
    }

    public void setTabLayoutPolicy(int tabLayoutPolicy) {
        this.tabLayoutPolicy = tabLayoutPolicy;
    }

    public int getTabShape() {
        return tabShape;
    }

    public void setTabShape(int tabShape) {
        this.tabShape = tabShape;
    }

    public int getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }
}
