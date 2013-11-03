package org.valkyriercp.form.binding.swing.date;

import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.support.AbstractBinder;

import java.util.Date;
import java.util.Map;

/**
 * Abstract base class for <code>java.util.Date</code> binders.
 *
 * @author Peter De Bruycker
 */
public abstract class AbstractDateFieldBinder extends AbstractBinder implements Binder {

	public static final String DATE_FORMAT = "dateFormat";

	private String dateFormat;

	public AbstractDateFieldBinder(String[] supportedContextKeys) {
		super(Date.class, supportedContextKeys);
	}

	protected void applyContext(AbstractDateFieldBinding binding, Map context) {
		if (context.containsKey(DATE_FORMAT)) {
			binding.setDateFormat((String)context.get(DATE_FORMAT));
		}
		else if (dateFormat != null) {
			binding.setDateFormat(dateFormat);
		}
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}
}

