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
package org.valkyriercp.application.support;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import org.springframework.util.Assert;

import javax.swing.*;

/**
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Arg??ello (JAF)</a>
 */
public class SwingXStatusBar extends DefaultStatusBar {

    /**
     * The message label.
     */
    private JLabel messageLabel;

    /**
     * Gets the messageLabel.
     *
     * @return the messageLabel
     */
    public JLabel getMessageLabel() {

        return this.messageLabel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final JLabel createMessageLabel() {

        // Remember message label
        final JLabel jLabel = super.createMessageLabel();
        jLabel.setBorder(BorderFactory.createEmptyBorder());
        this.setMessageLabel(jLabel);

        return jLabel;
    }

    /**
     * {@inheritDoc}
     */
    protected JComponent createControl() {

        super.createControl();

        final JXStatusBar jxStatusBar = new JXStatusBar();
        jxStatusBar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);
        jxStatusBar.add(this.getMessageLabel(), JXStatusBar.Constraint.ResizeBehavior.FILL);
        // jxStatusBar.add(new JSeparator(JSeparator.VERTICAL));
        // jxStatusBar.add(new SubstanceSkinChooserComboBox().getControl());
//        jxStatusBar.add(test.check.statusbar.FontSizePanel.getPanel());
        jxStatusBar.add(((StatusBarProgressMonitor) this.getProgressMonitor()).getControl());

        return jxStatusBar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visible) {

        this.getControl().setVisible(visible);

    }

    /**
     * Sets the messageLabel.
     *
     * @param messageLabel
     *            the messageLabel to set
     */
    private void setMessageLabel(JLabel messageLabel) {

        Assert.notNull(messageLabel, "messageLabel");

        this.messageLabel = messageLabel;
    }

}