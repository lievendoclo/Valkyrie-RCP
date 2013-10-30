package org.valkyriercp.application;

import java.awt.*;

public interface Application {
    void start();

    String getName();

    Image getImage();

    void close();

    void close(boolean force, int exitCode);

    void openWindow(String pageDescriptorId);
}
