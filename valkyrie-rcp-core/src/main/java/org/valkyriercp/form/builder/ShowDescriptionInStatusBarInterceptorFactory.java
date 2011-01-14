package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;

/**
 * Shows the description of a form property in the status bar when the form
 * component gains focus. When the form component loses focus, the status bar is
 * emptied.
 *
 * @author Peter De Bruycker
 */
public class ShowDescriptionInStatusBarInterceptorFactory implements FormComponentInterceptorFactory {

	public FormComponentInterceptor getInterceptor(final FormModel formModel) {
		return new AbstractFormComponentStatusBarInterceptor() {

			protected String getStatusBarText(String propertyName) {
				return formModel.getFieldFace(propertyName).getCaption();
			}
		};
	}
}