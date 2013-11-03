package org.valkyriercp.progress;

import org.springframework.util.Assert;
import org.valkyriercp.util.SwingUtils;

import javax.swing.*;

/**
 * <code>ProgressMonitor</code> implementation that delegates to a
 * <code>JProgressBar</code>.
 *
 * @author Peter De Bruycker
 */
public class ProgressBarProgressMonitor implements ProgressMonitor {

    private JProgressBar progressBar;
    private boolean canceled;

    public ProgressBarProgressMonitor(JProgressBar progressBar) {
        Assert.notNull(progressBar, "ProgressBar cannot be null.");
        this.progressBar = progressBar;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean b) {
        this.canceled = b;
    }

    public void done() {
        // not used
    }

    public void subTaskStarted(final String name) {
        SwingUtils.runInEventDispatcherThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setString(name);
            }
        });

    }

    public void taskStarted(final String name, final int totalWork) {
        SwingUtils.runInEventDispatcherThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
                progressBar.setMinimum(0);
                progressBar.setMaximum(totalWork);
                progressBar.setString(name);
            }
        });
    }

    public void worked(final int work) {
        SwingUtils.runInEventDispatcherThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(progressBar.getValue() + work);
            }
        });

    }
}
