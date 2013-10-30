package org.valkyriercp.factory;

import javax.swing.*;

public interface ButtonFactory {
    /**
	 * Returns a standard button.
	 */
	public AbstractButton createButton();

	/**
	 * Returns a checkBox.
	 */
	public AbstractButton createCheckBox();

	/**
	 * Returns a toggleButton.
	 */
	public AbstractButton createToggleButton();

	/**
	 * Returns a radioButton.
	 */
	public AbstractButton createRadioButton();
}
