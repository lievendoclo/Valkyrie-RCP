package org.valkyriercp.progress;

/**
 * <code>ProgressMonitor</code> implementation that does nothing.
 *
 * @author Peter De Bruycker
 */
public class NullProgressMonitor implements ProgressMonitor {

    private boolean canceled;

    public void done() {
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean b) {
        canceled = b;
    }

    public void subTaskStarted(String name) {
    }

    public void taskStarted(String name, int totalWork) {
    }

    public void worked(int work) {
    }
}