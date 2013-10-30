package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;

/**
 * Shows the caption of a form property in the status bar when the form
 * component gains focus. When the form component loses focus, the status bar is
 * emptied.
 *
 * @author Peter De Bruycker
 */
public class ShowCaptionInStatusBarInterceptorFactory implements FormComponentInterceptorFactory {

	public FormComponentInterceptor getInterceptor(final FormModel formModel) {
		return new AbstractFormComponentStatusBarInterceptor() {

			protected String getStatusBarText(String propertyName) {
				return formModel.getFieldFace(propertyName).getCaption();
			}
		};
	}
}
