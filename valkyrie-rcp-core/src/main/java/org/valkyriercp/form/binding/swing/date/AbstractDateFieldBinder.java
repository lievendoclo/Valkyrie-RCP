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

