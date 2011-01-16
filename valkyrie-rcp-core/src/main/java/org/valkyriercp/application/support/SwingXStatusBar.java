package org.valkyriercp.application.support;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import org.springframework.util.Assert;

import javax.swing.*;

/**
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
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