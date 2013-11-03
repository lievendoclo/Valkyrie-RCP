package org.valkyriercp.form.binding.swing.date;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import java.util.Date;

/**
 * Abstract base class for <code>java.util.Date</code> bindings
 *
 * @author Peter De Bruycker
 */
public abstract class AbstractDateFieldBinding extends CustomBinding {

	private String dateFormat;

	public AbstractDateFieldBinding(FormModel formModel, String formPropertyPath) {
		super(formModel, formPropertyPath, Date.class);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
