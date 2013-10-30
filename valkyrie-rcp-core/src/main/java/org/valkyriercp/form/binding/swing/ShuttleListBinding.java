package org.valkyriercp.form.binding.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.BufferedCollectionValueModel;
import org.valkyriercp.binding.value.support.ListListModel;
import org.valkyriercp.component.ShuttleList;
import org.valkyriercp.form.binding.support.AbstractBinding;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.list.DynamicListModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Binding to manage a {@link ShuttleList} component.
 *
 * @author lstreepy
 */
@Configurable
public class ShuttleListBinding extends AbstractBinding {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private IconSource iconSource;

	private ShuttleList list;

	private ListModel model;

	private ValueModel selectedItemsHolder = null;

	private ValueModel selectableItemsHolder;

	private Comparator comparator;

	private Class selectedItemType;

	private Class concreteSelectedType;

	private String formId;

	/**
	 * Construct a binding.
	 *
	 * @param list ShuttleList to bind
	 * @param formModel The form model holding the bound property
	 * @param formPropertyPath Path to the property to bind
	 */
	public ShuttleListBinding(final ShuttleList list, final FormModel formModel, final String formPropertyPath) {
		super(formModel, formPropertyPath, null);
		this.list = list;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public void setModel(ListModel model) {
		this.model = model;
	}

	public void setRenderer(ListCellRenderer renderer) {
		list.setCellRenderer(renderer);
	}

	public ListCellRenderer getRenderer() {
		return list.getCellRenderer();
	}

	public void setSelectableItemsHolder(ValueModel selectableItemsHolder) {
		this.selectableItemsHolder = selectableItemsHolder;
	}

	public void setSelectedItemsHolder(ValueModel selectedItemsHolder) {
		this.selectedItemsHolder = selectedItemsHolder;
	}

	public void setSelectedItemType(final Class selectedItemType) {
		this.selectedItemType = selectedItemType;
	}

	protected Class getSelectedItemType() {
		if (this.selectedItemType == null && this.selectedItemsHolder != null
				&& this.selectedItemsHolder.getValue() != null) {
			setSelectedItemType(this.selectedItemsHolder.getValue().getClass());
		}

		return this.selectedItemType;
	}

	protected JComponent doBindControl() {
		list.setModel(createModel());
		if (selectedItemsHolder != null) {
			setSelectedValue(null);
			list.addListSelectionListener(new ListSelectedValueMediator());
		}
		if (comparator != null) {
			list.setComparator(comparator);
		}
		// Get the icon to use for the edit button
		list.setEditIcon(getEditIcon(), getEditIconText());

		list.setListLabels(getChosenLabel(), getSourceLabel());

		return list;
	}

	private String getSourceLabel() {
		return getMsgText("shuttleList.sourceList.label", null);
	}

	private String getChosenLabel() {
		return getMsgText("shuttleList.chosenList.label", null);
	}

	/**
	 * Look for Edit Text by searching the ApplicationServices for:
	 * formId.shuttleList.editText if nothing try shuttleList.editText
	 *
	 * @return string
	 */
	private String getEditIconText() {
		return getMsgText("shuttleList.editText", "Edit...");
	}

	private String getMsgText(String key, String defaultMsg) {
		String text = null;

		if (getFormId() != null) {
			if (getProperty() != null) {
				text = messageSource.getMessage(getFormId() + "." + getProperty() + "." + key, null, null, null);
			}

			if (text == null) {
				text = messageSource.getMessage(getFormId() + "." + key, null, null, null);
			}
		}

		if (text == null) {
			text = messageSource.getMessage(key, null, defaultMsg, null);
		}

		return text;
	}

	/**
	 * Using the application services, check for: formId.shuttleList.edit if not
	 * there shuttleList.edit
	 *
	 * @return an Icon
	 */
	private Icon getEditIcon() {
		Icon icon = null;

		if (getFormId() != null) {
			icon = iconSource.getIcon(getFormId() + ".shuttleList.edit");
		}

		if (icon == null) {
			icon = iconSource.getIcon("shuttleList.edit");
		}
		return icon;
	}

	/**
	 * Determine if the selected item type can be multi-valued (is a collection
	 * or an array.
	 *
	 * @return boolean <code>true</code> if the <code>selectedItemType</code>
	 * is a Collection or an Array.
	 */
	protected boolean isSelectedItemAnArray() {
		Class itemType = getSelectedItemType();
		return itemType != null && itemType.isArray();
	}

	protected boolean isSelectedItemACollection() {
		return getSelectedItemType() != null && Collection.class.isAssignableFrom(getSelectedItemType());
	}

	protected Class getConcreteSelectedType() {
		if (concreteSelectedType == null) {
			if (isSelectedItemACollection()) {
				concreteSelectedType = BufferedCollectionValueModel.getConcreteCollectionType(getSelectedItemType());
			}
			else if (isSelectedItemAnArray()) {
				concreteSelectedType = getSelectedItemType().getComponentType();
			}
		}
		return concreteSelectedType;
	}

	protected void setSelectedValue(final PropertyChangeListener silentValueChangeHandler) {
		final int[] indices = indicesOf(selectedItemsHolder.getValue());
		if (indices.length < 1) {
			list.clearSelection();
		}
		else {
			list.setSelectedIndices(indices);
			// The selection may now be different than what is reflected in
			// collection property if this is SINGLE_INTERVAL_SELECTION, so
			// modify if needed...
			updateSelectionHolderFromList(silentValueChangeHandler);
		}
	}

	/**
	 * Return an array of indices in the selectableItems for each element in the
	 * provided set. The set can be either a Collection or an Array.
	 *
	 * @param itemSet Either an array or a Collection of items
	 * @return array of indices of the elements in itemSet within the
	 * selectableItems
	 */
	protected int[] indicesOf(final Object itemSet) {
		int[] ret = null;

		if (itemSet instanceof Collection) {
			Collection collection = (Collection) itemSet;
			ret = new int[collection.size()];
			int i = 0;
			for (Iterator iter = collection.iterator(); iter.hasNext(); i++) {
				ret[i] = indexOf(iter.next());
			}
		}
		else if (itemSet == null) {
			ret = new int[0];
		}
		else if (itemSet.getClass().isArray()) {
			Object[] items = (Object[]) itemSet;
			ret = new int[items.length];
			for (int i = 0; i < items.length; i++) {
				ret[i] = indexOf(items[i]);
			}
		}
		else {
			throw new IllegalArgumentException("itemSet must be eithe an Array or a Collection");
		}

		return ret;
	}

	protected int indexOf(final Object o) {
		final ListModel listModel = list.getModel();
		final int size = listModel.getSize();
		for (int i = 0; i < size; i++) {
			if (equalByComparator(o, listModel.getElementAt(i))) {
				return i;
			}
		}

		return -1;
	}

	private ListModel createModel() {
		if (model != null)
			return model;

		ListListModel model;
		if (selectableItemsHolder != null) {
			model = new DynamicListModel(selectableItemsHolder);
		}
		else {
			model = new ListListModel();
		}
		model.setComparator(comparator);
		model.sort();
		return model;
	}

	protected void updateSelectionHolderFromList(final PropertyChangeListener silentValueChangeHandler) {
		final Object[] selected = list.getSelectedValues();

		if (isSelectedItemACollection()) {
			try {
				// In order to properly handle buffered forms, we will
				// create a new collection to hold the new selection.
				final Collection newSelection = (Collection) getConcreteSelectedType().newInstance();
				if (selected != null && selected.length > 0) {
					for (int i = 0; i < selected.length; i++) {
						newSelection.add(selected[i]);
					}
				}

				// Only modify the selectedItemsHolder if the selection is
				// actually changed.
				final Collection oldSelection = (Collection) selectedItemsHolder.getValue();
				if (oldSelection == null || oldSelection.size() != newSelection.size()
						|| !collectionsEqual(oldSelection, newSelection)) {
					if (silentValueChangeHandler != null) {
						selectedItemsHolder.setValueSilently(newSelection, silentValueChangeHandler);
					}
					else {
						selectedItemsHolder.setValue(newSelection);
					}
				}
			}
			catch (InstantiationException e1) {
				throw new RuntimeException("Unable to instantiate new concrete collection class for new selection.", e1);
			}
			catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
		}
		else if (isSelectedItemAnArray()) {

			final Object[] newSelection = (Object[]) Array.newInstance(getConcreteSelectedType(), selected.length);
			for (int i = 0; i < selected.length; i++) {
				newSelection[i] = selected[i];
			}

			// Only modify the selectedItemsHolder if the selection is actually
			// changed.
			final Object[] oldSelection = (Object[]) selectedItemsHolder.getValue();
			if (oldSelection == null || oldSelection.length != newSelection.length
					|| !arraysEqual(oldSelection, newSelection)) {
				if (silentValueChangeHandler != null) {
					selectedItemsHolder.setValueSilently(newSelection, silentValueChangeHandler);
				}
				else {
					selectedItemsHolder.setValue(newSelection);
				}
			}
		}
	}

	/**
	 * Compare two arrays for equality using the configured comparator.
	 *
	 * @param a1 First array to compare
	 * @param a2 Second array to compare
	 * @return boolean true if they are equal
	 */
	protected boolean arraysEqual(Object[] a1, Object[] a2) {
		if (a1 != null && a2 != null && a1.length == a2.length) {
			// Loop over each element and compare them using our comparator
			for (int i = 0; i < a1.length; i++) {
				if (!equalByComparator(a1[i], a2[i])) {
					return false;
				}
			}
			return true;
		}
		else if (a1 == null && a2 == null) {
			return true;
		}
		return false;
	}

	/**
	 * Compare two collections for equality using the configured comparator.
	 * Element order must be the same for the collections to compare equal.
	 *
	 * @param a1 First collection to compare
	 * @param a2 Second collection to compare
	 * @return boolean true if they are equal
	 */
	protected boolean collectionsEqual(Collection a1, Collection a2) {
		if (a1 != null && a2 != null && a1.size() == a2.size()) {
			// Loop over each element and compare them using our comparator
			Iterator iterA1 = a1.iterator();
			Iterator iterA2 = a2.iterator();
			while (iterA1.hasNext()) {
				if (!equalByComparator(iterA1.next(), iterA2.next())) {
					return false;
				}
			}
		}
		else if (a1 == null && a2 == null) {
			return true;
		}
		return false;
	}

	/**
	 * Using the configured comparator (or equals if not configured), determine
	 * if two objects are equal.
	 *
	 * @param o1 Object to compare
	 * @param o2 Object to compare
	 * @return boolean true if objects are equal
	 */
	private boolean equalByComparator(Object o1, Object o2) {
		return comparator == null ? o1.equals(o2) : comparator.compare(o1, o2) == 0;
	}

	protected void readOnlyChanged() {
		list.setEnabled(isEnabled() && !isReadOnly());
	}

	protected void enabledChanged() {
		list.setEnabled(isEnabled() && !isReadOnly());
	}

	/**
	 * @return Returns the formId.
	 */
	protected String getFormId() {
		return formId;
	}

	/**
	 * @param formId The formId to set.
	 */
	protected void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * Inner class to mediate the list selection events into calls to
	 * {@link #updateSelectionHolderFromList}.
	 */
	private class ListSelectedValueMediator implements ListSelectionListener {

		private final PropertyChangeListener valueChangeHandler;

		public ListSelectedValueMediator() {
			valueChangeHandler = new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					setSelectedValue(valueChangeHandler);
				}
			};
			selectedItemsHolder.addValueChangeListener(valueChangeHandler);
		}

		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				updateSelectionHolderFromList(valueChangeHandler);
			}
		}
	}
}

