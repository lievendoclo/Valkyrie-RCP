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