package org.valkyriercp.rules.closure.support;

/**
 * Enumeration of possible process statuses.
 * 
 * @author Keith Donald
 */
public enum ProcessStatus {
	CREATED(0, "Created"), RUNNING(1, "Running"), STOPPED(2, "Stopped"), COMPLETED(
			3, "Completed"), RESET(4, "Reset"), ;

	int code;
	String label;

	ProcessStatus(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}
}
