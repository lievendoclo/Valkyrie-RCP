package org.valkyriercp.component;

import com.jidesoft.swing.JideTabbedPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.factory.ComponentFactoryDecorator;
import org.valkyriercp.factory.DefaultComponentFactory;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.util.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class JideOssComponentFactory extends ComponentFactoryDecorator {

    /**
     * The key of the jide tabbed pane tab trailing image in the image source.
     */
    private static final String JIDE_TABBED_PANE_TAB_TRAILING_IMAGE = "jideTabbedPane.tabTrailingImage";

    @Autowired
    private ImageSource imageSource;

    /**
     * Creates de Jide OSS component factory.
     */
    public JideOssComponentFactory() {

        super(new DefaultComponentFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JTabbedPane createTabbedPane() {

        final JideTabbedPane tabbedPane = new JideTabbedPane();

        tabbedPane.setShowTabButtons(Boolean.TRUE);
        tabbedPane.setHideOneTab(Boolean.FALSE);
        tabbedPane.setShowTabArea(Boolean.TRUE);
        tabbedPane.setShowTabContent(Boolean.TRUE);
        tabbedPane.setUseDefaultShowIconsOnTab(Boolean.FALSE);
        tabbedPane.setShowIconsOnTab(Boolean.TRUE);
        tabbedPane.setBoldActiveTab(Boolean.TRUE);
        tabbedPane.setScrollSelectedTabOnWheel(Boolean.TRUE);
        tabbedPane.setShowCloseButton(Boolean.FALSE);
        tabbedPane.setUseDefaultShowCloseButtonOnTab(Boolean.FALSE);
        tabbedPane.setShowCloseButtonOnTab(Boolean.TRUE);
        tabbedPane.setTabEditingAllowed(Boolean.FALSE);

        // Install tab trailing component
        final Image image = imageSource.getImage(JideOssComponentFactory.JIDE_TABBED_PANE_TAB_TRAILING_IMAGE);
        tabbedPane.setTabTrailingComponent(SwingUtils.generateComponent(image));

        return tabbedPane;
    }
}