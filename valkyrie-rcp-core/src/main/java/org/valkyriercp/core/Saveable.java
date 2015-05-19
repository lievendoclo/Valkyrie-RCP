package org.valkyriercp.core;

import org.valkyriercp.progress.ProgressMonitor;

public interface Saveable extends Dirtyable {
	void save();

	void save(ProgressMonitor saveProgressTracker);

	void saveAs();

	boolean isSaveAsSupported();

	boolean isSaveOnCloseRecommended();
}