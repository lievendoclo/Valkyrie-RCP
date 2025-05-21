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

import org.springframework.util.Assert;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.util.HasInnerComponent;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;

/**
 * Abstract base for
 * {@link FormComponentInterceptorFactory}
 * with formModel handling.
 *
 * @author oliverh
 */
public abstract class AbstractFormComponentInterceptor implements FormComponentInterceptor {

	private final FormModel formModel;

	protected AbstractFormComponentInterceptor() {
		formModel = null;
	}

	protected AbstractFormComponentInterceptor(FormModel formModel) {
		Assert.notNull(formModel, "formModel should not be null");
		this.formModel = formModel;
	}

	protected FormModel getFormModel() {
		return formModel;
	}

	public void processLabel(String propertyName, JComponent label) {
	}

	public void processComponent(String propertyName, JComponent component) {
	}

	/**
	 * Check for JScrollPane.
	 *
	 * @param component
	 * @return the component itself, or the inner component if it was a
	 * JScrollPane.
	 */
	protected JComponent getInnerComponent(JComponent component) {
		if (component instanceof JScrollPane) {
			return getInnerComponent((JComponent) ((JScrollPane) component).getViewport().getView());
		} if (component instanceof HasInnerComponent) {
            return getInnerComponent(((HasInnerComponent) component).getInnerComponent());
        }
		return component;
	}

    protected ApplicationConfig getApplicationConfig() {
        return ValkyrieRepository.getInstance().getApplicationConfig();
    }
}

