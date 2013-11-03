package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;

/**
 * If a form property has a <code>caption</code> defined in the
 * messages.properties file it will be used as the tooltip for the form
 * component.
 *
 * @author Peter De Bruycker
 */
public class ToolTipInterceptorFactory implements FormComponentInterceptorFactory {
	private boolean processComponent = true;

	private boolean processLabel = true;

	public FormComponentInterceptor getInterceptor(FormModel formModel) {
		return new ToolTipInterceptor(formModel);
	}

	private class ToolTipInterceptor extends AbstractFormComponentInterceptor {
		private FormModel formModel;

		public ToolTipInterceptor(FormModel formModel) {
			this.formModel = formModel;
		}

		String getCaption(String propertyName) {
			return formModel.getFieldFace(propertyName).getCaption();
		}

		public void processComponent(final String propertyName, final JComponent component) {
			if (processComponent) {
				component.setToolTipText(getCaption(propertyName));
			}
		}

		public void processLabel(String propertyName, JComponent label) {
			if (processLabel) {
				label.setToolTipText(getCaption(propertyName));
			}
		}
	}

	public void setProcessComponent(boolean processComponent) {
		this.processComponent = processComponent;
	}

	public void setProcessLabel(boolean processLabel) {
		this.processLabel = processLabel;
	}
}

