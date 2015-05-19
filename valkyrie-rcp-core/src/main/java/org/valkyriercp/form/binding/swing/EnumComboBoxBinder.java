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

import com.google.common.base.Strings;
import org.springframework.util.Assert;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * <p>
 * Binder which shows Enumerations in a combobox.
 * </p>
 * <p>
 * Configuration of the combobox can be done by providing the label in your
 * messages.properties:
 * </p>
 * 
 * <pre>
 * &lt;fully qualified classname&gt;.&lt;enum value or null&gt;=Label for enum
 * </pre>
 * 
 * <p>
 * Icons can be configured in the same manner by providing the image in your
 * images.properties:
 * </p>
 * 
 * <pre>
 * &lt;fully qualified classname&gt;.&lt;enum value or null&gt;=Location of icon
 * </pre>
 * 
 * @author Lieven Doclo
 * 
 */
public class EnumComboBoxBinder extends ComboBoxBinder {

	public enum NullEnum {
		NULL {
			@Override
			public String toString() {
				return "";
			}
		}
	}

	/** Should there always be a selection or can it be null? */
	private boolean nullable = false;

	/**
	 * Returns <code>true</code> if the combobox can have a null selection.
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Set to <code>true</code> if the combobox must contain a null selection.
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * Default constructor.
	 */
	public EnumComboBoxBinder() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AbstractListBinding createListBinding(JComponent control,
			FormModel formModel, String formPropertyPath) {
		ComboBoxBinding binding = (ComboBoxBinding) super.createListBinding(
				(JComboBox) control, formModel, formPropertyPath);
		binding.setSelectableItems(createEnumSelectableItems(formModel,
				formPropertyPath));
		if (isNullable()) {
			setEmptySelectionValue(NullEnum.NULL);
		}
		Class propertyType = getPropertyType(formModel, formPropertyPath);
		binding.setRenderer(new EnumListRenderer(propertyType));
		binding.setEditor(new EnumComboBoxEditor(binding.getEditor()));
		return binding;
	}

	@SuppressWarnings("unchecked")
	private java.util.List<Enum> createEnumSelectableItems(FormModel formModel,
			String formPropertyPath) {
		Class propertyType = getPropertyType(formModel, formPropertyPath);
		Class<Enum> enumPropertyType = propertyType;
		java.util.List<Enum> out = new ArrayList<Enum>();

		for (Enum e : enumPropertyType.getEnumConstants()) {
			String messageKey = enumPropertyType.getName() + "." + e.name();
			String desc = getMessageResolver().getMessage(messageKey);
			if (!Strings.isNullOrEmpty(desc)) {
				out.add(e);
			} else {
				log.warn("No message found for: " + messageKey + ", ignoring!");
			}
		}

		// return enumPropertyType.getEnumConstants();
		return out;
	}

	private class EnumListRenderer extends JLabel implements ListCellRenderer {

		protected Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		private final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

		private Class enumType;

		public EnumListRenderer(Class enumType) {
			super();
			this.enumType = enumType;
			setOpaque(true);
			setBorder(getNoFocusBorder());
		}

		private Border getNoFocusBorder() {
			if (System.getSecurityManager() != null) {
				return SAFE_NO_FOCUS_BORDER;
			} else {
				return noFocusBorder;
			}
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Enum valueEnum = (Enum) value;

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			String label = "";
			Icon icon;
			if (value == null || value == NullEnum.NULL) {
				label = getMessageResolver()
						.getMessage(enumType.getName() + ".null");
				icon = getIconSource().getIcon(enumType.getName() + ".null");
			} else {
				label = getMessageResolver().getMessage(enumType.getName() + "."
                        + valueEnum.name());
				icon = getIconSource().getIcon(enumType.getName() + "."
                        + valueEnum.name());
			}
			setText(label);
			setIcon(icon);
			setEnabled(list.isEnabled());
			setFont(list.getFont());

			Border border = null;
			if (cellHasFocus) {
				if (isSelected) {
					border = UIManager
							.getBorder("List.focusSelectedCellHighlightBorder");
				}
				if (border == null) {
					border = UIManager
							.getBorder("List.focusCellHighlightBorder");
				}
			} else {
				border = getNoFocusBorder();
			}
			setBorder(border);

			return this;
		}

	}

	/**
	 * Specifieke editor om de waarden van een enum te gebruiken om de combobox
	 * te tonen
	 * 
	 * @author ldo
	 * 
	 */
	private class EnumComboBoxEditor implements ComboBoxEditor {

		private Object current;

		private ComboBoxEditor inner;

		public EnumComboBoxEditor(ComboBoxEditor editor) {
			Assert.notNull(editor, "Editor cannot be null");
			this.inner = editor;
		}

		public void selectAll() {
			inner.selectAll();
		}

		public Component getEditorComponent() {
			return inner.getEditorComponent();
		}

		public void addActionListener(ActionListener l) {
			inner.addActionListener(l);
		}

		public void removeActionListener(ActionListener l) {
			inner.removeActionListener(l);
		}

		public Object getItem() {
			if (current == NullEnum.NULL) {
				return null;
			}
			return current;
		}

		public void setItem(Object value) {
			current = value;
			if (value != null && value != NullEnum.NULL) {
				Enum valueEnum = (Enum) value;
				Class<? extends Enum> valueClass = valueEnum.getClass();
				inner.setItem(getMessageResolver().getMessage(valueClass.getName()
                        + "." + valueEnum.name()));
			} else {
				inner.setItem(NullEnum.NULL);
			}
		}
	}

    public MessageResolver getMessageResolver() {
        return ValkyrieRepository.getInstance().getApplicationConfig().messageResolver();
    }

    public IconSource getIconSource() {
        return ValkyrieRepository.getInstance().getApplicationConfig().iconSource();
    }
}
