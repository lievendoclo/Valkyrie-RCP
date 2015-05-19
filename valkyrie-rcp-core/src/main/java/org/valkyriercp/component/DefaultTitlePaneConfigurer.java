/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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