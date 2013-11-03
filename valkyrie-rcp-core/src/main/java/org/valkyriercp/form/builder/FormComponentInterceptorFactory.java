package org.valkyriercp.form.builder;

import org.valkyriercp.binding.form.FormModel;

/**
 * Factory for creating {@link FormComponentInterceptor}s for a given
 * {@link FormModel}.
 *
 * @author oliverh
 */
public interface FormComponentInterceptorFactory {

	/**
	 * Returns a {@link FormComponentInterceptor}s for the given
	 * {@link FormModel}.
	 */
	public FormComponentInterceptor getInterceptor(FormModel formModel);

}