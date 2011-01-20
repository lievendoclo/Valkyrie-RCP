package org.valkyriercp.taskpane;

import javax.swing.*;

public interface IconGenerator<T>
{
    public ImageIcon generateIcon(T forObject);
}

