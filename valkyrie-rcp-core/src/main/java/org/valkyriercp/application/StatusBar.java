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
package org.valkyriercp.application;

import org.valkyriercp.core.Message;
import org.valkyriercp.factory.ControlFactory;

public interface StatusBar extends ControlFactory {
	/**
	 * Returns the <code>ProgressMonitor</code> in use by this
	 * <code>StatusBar</code>
	 *
	 * @return the progress monitor
	 */
	org.valkyriercp.progress.ProgressMonitor getProgressMonitor();

	/**
	 * Sets the message of this status bar.
	 *
	 * @param message
	 *            the message, <code>null</code> clears the message
	 */
	void setMessage(String message);

	/**
	 * Sets the message of this status bar.
	 *
	 * @param message
	 *            the message, <code>null</code> clears the message
	 */
	void setMessage(Message message);

	/**
	 * Sets the error message of this status bar.
	 * <p>
	 * An error message is usualy painted in another color,
	 * <code>StatusBar</code> implementations are free to choose the way they
	 * display the error message
	 *
	 * @param message
	 *            the error message, <code>null</code> clears the error
	 *            message
	 */
	void setErrorMessage(String message);

	/**
	 * Sets the error message of this status bar.
	 * <p>
	 * An error message is usualy painted in another color,
	 * <code>StatusBar</code> implementations are free to choose the way they
	 * display the error message
	 *
	 * @param message
	 *            the error message, <code>null</code> clears the error
	 *            message
	 */
	void setErrorMessage(Message message);

	/**
	 * Sets the visible state of this status bar.
	 *
	 * @param visible
	 *            true or false
	 */
	void setVisible(boolean visible);

	/**
	 * Provides a hint whether the current running operation can be canceled.
	 *
	 * @param enabled
	 *            true or false
	 */
	void setCancelEnabled(boolean enabled);

	/**
	 * Clears the messages of this status bar.
	 */
	void clear();
}