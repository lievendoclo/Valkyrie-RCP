package org.valkyriercp.form.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.application.Application;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.util.HasInnerComponent;

import javax.swing.*;

/**
 * Abstract base for
 * {@link FormComponentInterceptorFactory}
 * with formModel handling.
 *
 * @author oliverh
 */
@Configurable
public abstract class AbstractFormComponentInterceptor implements FormComponentInterceptor {

	private final FormModel formModel;

    @Autowired
    private ApplicationConfig applicationConfig;

	protected AbstractFormComponentInterceptor() {
		formModel = null;
	}

	protected AbstractFormComponentInterceptor(FormModel formModel) {
		Assert.notNull(formModel);
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
        return applicationConfig;
    }
}

