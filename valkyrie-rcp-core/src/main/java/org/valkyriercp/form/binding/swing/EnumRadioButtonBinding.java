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

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Radiobutton binding for Enum values.
 * 
 * Labels for the radio buttons can be configured in the messages:
 * 
 * <pre>
 * my.package.MyClass.MY_ENUM = My Enum Label
 * my.package.MyClass.null = None
 * </pre>
 * 
 * @author Lieven Doclo
 * 
 */
public class EnumRadioButtonBinding extends CustomBinding {

	private JPanel contentPanel;

	private List<Enum> selectableEnumsList;

	private Map<String, ContainerJRadioButton<Enum>> radioButtons;

	private Class propertyType;

	private ButtonGroup group;

	private boolean horizontalLayout = false;

	public EnumRadioButtonBinding(JPanel contentPanel, FormModel formModel,
			String formPropertyPath, Class<?> propertyType,
			List<Enum> selectableEnumsList, boolean horizontalLayout) {
		super(formModel, formPropertyPath, propertyType);
		this.contentPanel = contentPanel;
		this.propertyType = propertyType;
		radioButtons = new LinkedHashMap<String, ContainerJRadioButton<Enum>>();
		this.selectableEnumsList = selectableEnumsList;
		this.horizontalLayout = horizontalLayout;
		createRadioButtons();
	}

	@Override
	protected void valueModelChanged(Object newValue) {
		deselectAll();
		Enum enumValue = (Enum) newValue;
		ContainerJRadioButton<Enum> button;
		if (enumValue == null) {
			button = radioButtons.get(null);
		} else {
			button = radioButtons.get(enumValue.name());
		}
		if (button != null) {
			button.setSelected(true);
		}
	}

	private void deselectAll() {
		if (group != null)
			group.clearSelection();
	}

	@Override
	protected JComponent doBindControl() {
		Assert.notNull(selectableEnumsList, "selectableEnumsList should not be null");
		return createBindingControl();
	}

	private JPanel createBindingControl() {
		// group = new ButtonGroup();
		group = new ButtonGroup() {
			@Override
			public void setSelected(ButtonModel m, boolean b) {
				if (b && m != null && m != getSelection()) {
					super.setSelected(m, b);
				} else if (!b && m == getSelection()) {
					clearSelection();
					controlValueChanged(null);
				}
			}
		};
		FormLayout layout = new FormLayout(
				new ColumnSpec[] { FormFactory.DEFAULT_COLSPEC },
				getDefaultRowsWithGap(radioButtons.keySet().size()));
		int count = 1;
		// contentPanel.setLayout(layout);
		// contentPanel.setLayout(new BoxLayout(target, axis));
		contentPanel.setLayout(new BoxLayout(contentPanel,
				horizontalLayout ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));
		for (ContainerJRadioButton<Enum> button : radioButtons.values()) {
			group.add(button);
			contentPanel.add(button);
			// contentPanel.add(button, new CellConstraints(1, count));
			// contentPanel.add(button, new CellConstraints(count, 1));
			count += 2;
		}
		valueModelChanged(getValue());
		return contentPanel;
	}

	private static RowSpec[] getDefaultRowsWithGap(int rowCount) {
		List<RowSpec> rows = new ArrayList<RowSpec>();
		for (int i = 0; i < rowCount; i++) {
			rows.add(new RowSpec("fill:pref:nogrow"));
			rows.add(FormFactory.LINE_GAP_ROWSPEC);
		}
		rows.remove(rows.size() - 1);
		return rows.toArray(new RowSpec[] {});
	}

	@Override
	protected void enabledChanged() {
		for (ContainerJRadioButton<Enum> button : radioButtons.values()) {
			button.setEnabled(isEnabled());
		}

	}

	@Override
	protected void readOnlyChanged() {
		for (ContainerJRadioButton<Enum> button : radioButtons.values()) {
			button.setEnabled(!isReadOnly());
		}
	}

	private void createRadioButtons() {
		for (Enum enumValue : selectableEnumsList) {
			ContainerJRadioButton<Enum> button = new ContainerJRadioButton<Enum>();
			String text;
			if (enumValue == null) {
				text = ValkyrieRepository.getInstance().getApplicationConfig().messageSourceAccessor().getMessage(propertyType.getName()
                        + ".null", "null");
				button.setText(text == null ? propertyType.getName() + ".null"
						: text);
			} else {
				text = ValkyrieRepository.getInstance().getApplicationConfig().messageSourceAccessor().getMessage(propertyType.getName()
                        + "." + enumValue.name(), enumValue.name());
				button.setText(text == null ? propertyType.getName() + "."
						+ enumValue.name() : text);
			}
			button.setContainedObject(enumValue);
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ContainerJRadioButton<Enum> button = (ContainerJRadioButton<Enum>) e
							.getSource();
					if (button.isSelected()) {
						controlValueChanged(button.getContainedObject());
					}
				}

			});
			if (enumValue == null)
				radioButtons.put(null, button);
			else
				radioButtons.put(enumValue.name(), button);

		}
	}

	private class ContainerJRadioButton<T> extends JRadioButton {

		private T containedObject;

		public T getContainedObject() {
			return containedObject;
		}

		public void setContainedObject(T containedObject) {
			this.containedObject = containedObject;
		}

	}

}
