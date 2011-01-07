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