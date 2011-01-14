package org.valkyriercp.form.builder;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Abstract base class for showing text in the status bar when the form
 * component gains focus.
 *
 * @author Peter De Bruycker
 */
public abstract class AbstractFormComponentStatusBarInterceptor extends AbstractFormComponentInterceptor {

	protected abstract String getStatusBarText(String propertyName);

	public void processComponent(final String propertyName, final JComponent component) {
		component.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (getApplicationConfig().windowManager().getActiveWindow() != null) {
					String caption = getStatusBarText(propertyName);
					if (caption != null) {
						getApplicationConfig().windowManager().getActiveWindow().getStatusBar().setMessage(caption);
					}
				}
			}

			public void focusLost(FocusEvent e) {
				if (getApplicationConfig().windowManager().getActiveWindow() != null) {
					getApplicationConfig().windowManager().getActiveWindow().getStatusBar().setMessage("");
				}
			}
		});
	}
}
