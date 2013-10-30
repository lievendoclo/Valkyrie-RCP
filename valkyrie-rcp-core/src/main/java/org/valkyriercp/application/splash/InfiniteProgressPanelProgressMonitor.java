package org.valkyriercp.application.splash;

import org.valkyriercp.progress.ProgressMonitor;

/**
 * <code>ProgressMonitor</code> adapter implementation for the <code>InfiniteProgressPanel</code>.
 *
 * @author Peter De Bruycker
 */
public class InfiniteProgressPanelProgressMonitor implements ProgressMonitor {
	private boolean cancelled;

	private InfiniteProgressPanel progressPanel;

	public InfiniteProgressPanelProgressMonitor(InfiniteProgressPanel progressPanel) {
		this.progressPanel = progressPanel;
	}

	public void worked(int work) {
		// not used
	}

	public void taskStarted(String name, int totalWork) {
		progressPanel.setText(name);
		progressPanel.start();
	}

	public void subTaskStarted(String name) {
		progressPanel.setText(name);
	}

	public void setCanceled(boolean b) {
		if (b) {
			progressPanel.interrupt();
		}
		cancelled = b;
	}

	public boolean isCanceled() {
		return cancelled;
	}

	public void done() {
		progressPanel.stop();
	}
}