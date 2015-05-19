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
package org.valkyriercp.application.splash;

import org.springframework.util.Assert;
import org.valkyriercp.progress.ProgressBarProgressMonitor;

import javax.swing.*;
import java.awt.*;

/**
 * A lightweight splash-screen for displaying the progress of a GUI application startup process.
 *
 * <p>
 * The splash screen produced by this class will be an undecorated, centered frame containing an image above a progress
 * bar. It minimizes class loading so it is displayed immediately once the application is started.
 * </p>
 *
 *
 * @author Peter De Bruycker
 */
public class ProgressSplashScreen extends SimpleSplashScreen implements MonitoringSplashScreen {
    private JProgressBar progressBar;

    private boolean showProgressLabel;

    private org.valkyriercp.progress.ProgressMonitor progressMonitor;

    private boolean indeterminate = true;

    /**
     * Creates a new {@code ProgressSplashScreen} that uses an underlying {@link ProgressBarProgressMonitor}.
     */
    public ProgressSplashScreen() {
    }

    /**
     * Returns the flag that determines whether or not the progress bar will display updated textual info as it is
     * provided by the progress monitor.
     *
     * @return The showProgressLabel flag.
     */
    public boolean isShowProgressLabel() {
        return showProgressLabel;
    }

    /**
     * Sets the flag that determines whether or not the progress bar will display updated textual info as it is provided
     * by the progress monitor.
     *
     * @param showProgressLabel
     */
    public void setShowProgressLabel(boolean showProgressLabel) {
        this.showProgressLabel = showProgressLabel;
    }

    /**
     * Returns a component that displays an image above a progress bar.
     *
     * @return A splash screen containing an image and a progress bar, never null.
     */
    protected Component createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        Component component = super.createContentPane();
        if(component != null)
            content.add(component);

        JProgressBar progressBar = getProgressBar();
        progressBar.setIndeterminate(isIndeterminate());
        progressBar.setStringPainted(isShowProgressLabel());

        content.add(progressBar, BorderLayout.SOUTH);

        return content;
    }

    public org.valkyriercp.progress.ProgressMonitor getProgressMonitor() {
        if (progressMonitor == null) {
            progressMonitor = new ProgressBarProgressMonitor(getProgressBar());
        }
        return progressMonitor;
    }

    /**
     * Sets the progress monitor used by this splash screen.
     *
     * @param progressMonitor
     *            The progress monitor.
     */
    public void setProgressMonitor(org.valkyriercp.progress.ProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    /**
     * Returns the progress bar.
     *
     * @return not null
     */
    protected JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = createProgressBar();
            Assert.notNull(progressBar, "createProgressBar should not return null");
        }
        return progressBar;
    }

    protected JProgressBar createProgressBar() {
        return new JProgressBar();
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    /**
     * Determines whether the progress bar is in determinate or indeterminate mode. Default is true
     *
     * @param indeterminate
     *            <code>true</code> if the progress bar should change to indeterminate mode; <code>false</code> if
     *            it should revert to normal.
     * @see JProgressBar#setIndeterminate(boolean)
     */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
}

