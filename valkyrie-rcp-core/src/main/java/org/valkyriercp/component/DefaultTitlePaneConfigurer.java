package org.valkyriercp.component;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.*;
import java.awt.*;

public class DefaultTitlePaneConfigurer implements TitlePaneConfigurer {

    @Autowired
    @Qualifier("titlePaneBackgroundColor")
    private Color titlePaneBackgroundColor;

    @Autowired
    @Qualifier("titlePanePinstripeColor")
    private Color titlePanePinstripeColor;

    @Override
    public void configure(JXPanel panel) {
        MattePainter matte = new MattePainter(titlePaneBackgroundColor);
        PinstripePainter pinstripe = new PinstripePainter();
        pinstripe.setPaint(titlePanePinstripeColor);
        pinstripe.setSpacing(5.);
        GlossPainter gloss = new GlossPainter();
        CompoundPainter<JXPanel> painter = new CompoundPainter<JXPanel>(matte,
                pinstripe, gloss);
        panel.setBackgroundPainter(painter);
        panel.setBackground(getBackgroundColor());
    }

    @Override
    public Color getBackgroundColor() {
        Color c = UIManager.getLookAndFeel().getDefaults()
                .getColor("primaryControlHighlight");
        if (c == null) {
            c = UIManager.getColor("controlLtHighlight");
        }
        return c;
    }

}