package org.valkyriercp.core;

import javax.swing.*;

public interface ButtonConfigurer {

	/**
	 * Configure the given button.
	 *
	 * @param button The button that needs to be configured.
	 * @return the configured button.
	 */
    public AbstractButton configure(AbstractButton button);
}