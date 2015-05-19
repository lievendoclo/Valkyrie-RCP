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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.component.ShadowBorder;
import org.valkyriercp.core.Message;
import org.valkyriercp.factory.AbstractControlFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

/**
 * <p>
 * A <tt>StatusBar</tt> control is a component with a horizontal layout which hosts a number of status indication
 * controls. Typically it is situated below the content area of the window.
 * </p>
 * <p>
 * By default a <tt>StatusBar</tt> has two predefined status controls: a <tt>JLabel</tt> and a <tt>JProgressBar</tt>
 * and it provides API for easy access.
 * </p>
 *
 * @author Peter De Bruycker
 */
public class DefaultStatusBar extends AbstractControlFactory implements StatusBar {

    private static Log logger = LogFactory.getLog(DefaultStatusBar.class);

    private String message;

    private String errorMessage;

    private JLabel messageLabel;

    private JPanel statusBar;

    private StatusBarProgressMonitor progressMonitor;

    /**
     * Returns the status bar's progress monitor
     */
    public org.valkyriercp.progress.ProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    /**
     * Controls whether the ProgressIndication provides UI for canceling a long running operation.
     *
     * If the ProgressIndication is currently visible calling this method may have a direct effect on the layout because
     * it will make a cancel button visible.
     */
    public void setCancelEnabled(boolean enabled) {
        progressMonitor.setCancelEnabled(enabled);
    }

    /**
     * Sets the message text to be displayed on the status bar.
     * <p>
     * The icon of the message is ignored
     *
     * @param message
     *            the message to be set, if <code>null</code>, the status line is cleared.
     */
    public void setMessage(Message message) {
        setMessage(message == null ? null : message.getMessage());
    }

    /**
     * Sets the message text to be displayed on the status bar.
     *
     * @param message
     *            the message to be set, if <code>null</code>, the status line is cleared.
     */
    public void setMessage(String message) {
        this.message = message;
        if (errorMessage == null) {
            logger.debug("Setting status bar message to \"" + message + "\"");
            messageLabel.setText(this.message);
        }
    }

    /**
     * Sets the error message text to be displayed on the status bar.
     * <p>
     * Error messages are shown over the standard message, and in a red color.
     * <p>
     * The icon of the message is ignored
     *
     * @param errorMessage
     *            the error message to be set, if <code>null</code>, the error message is cleared, and the standard
     *            message is shown again
     */
    public void setErrorMessage(Message errorMessage) {
        setErrorMessage(errorMessage == null ? null : errorMessage.getMessage());
    }

    /**
     * Sets the error message text to be displayed on the status bar.
     * <p>
     * Error messages are shown over the standard message, and in a red color.
     *
     * @param errorMessage
     *            the error message to be set, if <code>null</code>, the error message is cleared, and the standard
     *            message is shown again
     */
    public void setErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            logger.debug("Resetting the status bar message color to normal");
            messageLabel.setForeground(SystemColor.controlText);

            this.errorMessage = null;
            setMessage(message);
        }
        else {
            logger.debug("Setting the status bar messsage color to red");
            messageLabel.setForeground(Color.RED);

            logger.debug("Setting status bar error message to \"" + errorMessage + "\"");
            this.errorMessage = errorMessage;
            messageLabel.setText(this.errorMessage);
        }
    }

    /**
     * Create the <code>JLabel</code> used to render the messages.
     * <p>
     * Can safely be overridden to customize the label
     *
     * @return the <code>JLabel</code>
     */
    protected JLabel createMessageLabel() {
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setName("message");
        Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, UIManager
                .getColor("controlHighlight"), UIManager.getColor("controlShadow"));
        Border emptyBorder = BorderFactory.createEmptyBorder(1, 3, 1, 3);
        messageLabel.setBorder(BorderFactory.createCompoundBorder(bevelBorder, emptyBorder));

        return messageLabel;
    }

    protected JComponent createControl() {
        statusBar = new JPanel(new BorderLayout());

        messageLabel = createMessageLabel();

        progressMonitor = createStatusBarProgressMonitor();

        statusBar.add(messageLabel);
        statusBar.add(progressMonitor.getControl(), BorderLayout.EAST);

        progressMonitor.getControl().setPreferredSize(new Dimension(200, 17));

        statusBar.setBorder(new ShadowBorder());

        return statusBar;
    }

    /**
     * Create the <code>StatusBarProgressMonitor</code>.
     * <p>
     * Can safely be overridden to customize the progress monitor and its components
     *
     * @return the <code>StatusBarProgressMonitor</code>
     */
    protected StatusBarProgressMonitor createStatusBarProgressMonitor() {
        return new StatusBarProgressMonitor();
    }

    /**
     * Shows or hides this status bar.
     *
     * @see Component#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        statusBar.setVisible(visible);
    }

    public void clear() {
        setErrorMessage((String) null);
        setMessage((String) null);
    }
}
