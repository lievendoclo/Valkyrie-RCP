package org.valkyriercp.application;

public interface ApplicationPageFactory {
    ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor);
}
