package org.valkyriercp.component;

import org.valkyriercp.form.HasValidationComponent;

import javax.swing.*;
import java.awt.*;

public class PanelWithValidationComponent extends JPanel implements HasValidationComponent
{
    public PanelWithValidationComponent()
    {
        super();
    }

    public PanelWithValidationComponent(LayoutManager layoutManager)
    {
        super(layoutManager);
    }

    public PanelWithValidationComponent(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
    }

    public PanelWithValidationComponent(LayoutManager layoutManager, boolean isDoubleBuffered)
    {
        super(layoutManager, isDoubleBuffered);
    }

    /**
     * Geef de component waarop de validatiekleur en het icoontje terecht komen.
     */
    public JComponent getValidationComponent()
    {
        return null;
    }

}
