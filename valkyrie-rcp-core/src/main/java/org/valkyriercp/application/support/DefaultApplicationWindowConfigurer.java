package org.valkyriercp.application.support;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowConfigurer;

import java.awt.*;

public class DefaultApplicationWindowConfigurer implements ApplicationWindowConfigurer {

    private String title = "New Application Window";

    private Image image;

    private boolean showMenuBar = true;

    private boolean showToolBar = true;

    private boolean showStatusBar = true;

    private Dimension initialSize = new Dimension(800, 600);

    private ApplicationWindow window;

    public DefaultApplicationWindowConfigurer(ApplicationWindow window) {
        Assert.notNull(window, "Application window is required");
        this.window = window;
    }

    public ApplicationWindow getWindow() {
        return window;
    }

    public String getTitle() {
        return title;
    }

    public Image getImage() {
        return image;
    }

    public Dimension getInitialSize() {
        return initialSize;
    }

    public boolean getShowMenuBar() {
        return showMenuBar;
    }

    public boolean getShowToolBar() {
        return showToolBar;
    }

    public boolean getShowStatusBar() {
        return showStatusBar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setInitialSize(Dimension initialSize) {
        if (initialSize != null) {
            this.initialSize = initialSize;
        }
    }

    public void setShowMenuBar(boolean showMenuBar) {
        this.showMenuBar = showMenuBar;
    }

    public void setShowToolBar(boolean showToolBar) {
        this.showToolBar = showToolBar;
    }

    public void setShowStatusBar(boolean showStatusBar) {
        this.showStatusBar = showStatusBar;
    }

    public String toString() {
        return new ToStringCreator(this).append("title", title).append("image", image).append("showMenuBar",
                showMenuBar).append("showToolBar", showToolBar).append("showStatusBar", showStatusBar).append(
                "initialSize", initialSize).append("window", window).toString();
    }

}