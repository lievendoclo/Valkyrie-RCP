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
        JComponent innerComponent = getInnerComponent(component);
        innerComponent.addFocusListener(new FocusListener() {
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
