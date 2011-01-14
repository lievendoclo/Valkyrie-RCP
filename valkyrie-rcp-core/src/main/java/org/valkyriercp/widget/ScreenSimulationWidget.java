package org.valkyriercp.widget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.valkyriercp.util.MessageConstants;
import org.valkyriercp.util.PopupMenuMouseListener;

import javax.swing.*;

/**
 * A widget that enables to mimic a screen. This is useful for application
 * still under development, but that want to show how a certain screen will look
 * like.
 *
 * The view consists of a image tab showing the screen look-a-like and/or a HTML tab
 * consisting of an explanation on what this screen will do.
 */
public class ScreenSimulationWidget extends AbstractWidget
{
    JTabbedPane mainComponent;

    private static Log log = LogFactory.getLog(ScreenSimulationWidget.class);

    public ScreenSimulationWidget(Resource explanationPath)
    {
        this(explanationPath, null);
    }

    public ScreenSimulationWidget(Resource explanationPath, Resource imagePath)
    {
        this(explanationPath, imagePath, null);
    }

    public ScreenSimulationWidget(Resource explanationPath, Resource imagePath, JPopupMenu popup)
    {
        this.mainComponent = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        JComponent imageArea = createImagePanel(imagePath);
        if (imageArea != null)
        {
            String screenLabel = applicationConfig.messageResolver().getMessage("simulation", "screen", MessageConstants.LABEL);
            this.mainComponent.addTab(screenLabel, imageArea);
        }
        else
            log.warn("Image not found at " + imagePath);

        JComponent explanationArea = createTextPanel(explanationPath);
        if (explanationArea != null)
        {

            String explanationLabel = applicationConfig.messageResolver().getMessage("simulation", "explanation", MessageConstants.LABEL);
            this.mainComponent.addTab(explanationLabel, explanationArea);
        }
        else
            log.warn("Explanation html not found at " + explanationPath);

        if (popup != null)
        {
            this.mainComponent.addMouseListener(new PopupMenuMouseListener(popup));
        }
    }

    private JComponent createTextPanel(Resource textResource)
    {
        HTMLViewWidget hw = new HTMLViewWidget(textResource);
        return hw.getComponent();
    }

    private JComponent createImagePanel(Resource imageResource)
    {
        ImageViewWidget hw = new ImageViewWidget(imageResource);
        return hw.getComponent();
    }

    public JComponent getComponent()
    {
        return mainComponent;
    }
}

