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
package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Radio button binder for enum values.
 * 
 * Use this in your Application Context to configure the binder:
 * 
 * <pre>
 * &lt;bean name="enumRadioButtonBinder" class="org.springframework.richclient.form.binding.swing.EnumRadioButtonBinder" /&gt;
 * </pre>
 * 
 * Or when you need an additional null value that can be selected:
 * 
 * <pre>
 * &lt;bean name="enumRadioButtonBinder" class="org.springframework.richclient.form.binding.swing.EnumRadioButtonBinder" &gt;
 *     &lt;property name="nullable" value="true" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author Lieven Doclo
 */
public class EnumRadioButtonBinder extends AbstractBinder {

	public static final String NULLABLE_KEY = "nullable";

	public static final String HORIZONTAL_LAYOUT_KEY = "layout";

	private boolean nullable = false;

	/**
	 * Creates a new binder
	 */
	public EnumRadioButtonBinder() {
		super(Enum.class, new String[] { NULLABLE_KEY, HORIZONTAL_LAYOUT_KEY });
	}

	@Override
	protected JComponent createControl(Map context) {
		return new JPanel();
	}

	/**
	 * Sets whether this control can contain a <code>null</code> value
	 * 
	 * @param nullable
	 *            <code>true</code> if the binder needs to contain a
	 *            <code>null</code> value
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	@Override
	protected Binding doBind(JComponent control, FormModel formModel,
			String formPropertyPath, Map context) {
		boolean layout = context.get(HORIZONTAL_LAYOUT_KEY) == null ? false
				: ((Boolean) context.get(HORIZONTAL_LAYOUT_KEY)).booleanValue();

		EnumRadioButtonBinding binding = new EnumRadioButtonBinding(
				(JPanel) control, formModel, formPropertyPath, getPropertyType(
						formModel, formPropertyPath), getSelectableEnumsList(
						formModel, formPropertyPath, context), layout);
		return binding;

	}

	/**
	 * Adds the <code>null</code> value if this binder is nullable.
	 */
	private List<Enum> getSelectableEnumsList(FormModel formModel,
			String formPropertyPath, Map context) {
		List<Enum> out = new ArrayList<Enum>();
		Boolean nullable;
		if (context.containsKey(NULLABLE_KEY)) {
			nullable = (Boolean) context.get(NULLABLE_KEY);
		} else {
			nullable = this.nullable;
		}
		if (nullable) {
			out.add(null);
		}
		for (Enum e : ((Class<Enum>) getPropertyType(formModel,
				formPropertyPath)).getEnumConstants()) {
			out.add(e);
		}
		return out;
	}

}
