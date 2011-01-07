package org.valkyriercp.application;

import org.valkyriercp.factory.ControlFactory;

import java.util.List;

public interface ApplicationPage extends ControlFactory {
    String getId();
    ApplicationWindow getWindow();
    boolean close();
    boolean close(PageComponent pageComponent);
    public List<PageComponent> getPageComponents();
}
