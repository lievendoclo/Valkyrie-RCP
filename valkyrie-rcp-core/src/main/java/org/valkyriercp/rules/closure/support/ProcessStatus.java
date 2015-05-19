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
