package org.valkyriercp.application;

import java.awt.*;

public interface ApplicationWindowConfigurer {
    public ApplicationWindow getWindow();

    public String getTitle();

    public Image getImage();

    public Dimension getInitialSize();

    public boolean getShowMenuBar();

    public boolean getShowToolBar();

    public boolean getShowStatusBar();

    public void setTitle(String title);

    public void setImage(Image image);

    public void setInitialSize(Dimension initialSize);

    public void setShowMenuBar(boolean showMenuBar);

    public void setShowToolBar(boolean showToolBar);

    public void setShowStatusBar(boolean statusBar);

}