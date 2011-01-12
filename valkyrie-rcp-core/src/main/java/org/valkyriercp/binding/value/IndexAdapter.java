package org.valkyriercp.binding.value;

/**
 * Adds an index aspect to the backing object mostly used by collection type
 * backing objects. The index can be set to point to a specific item which can
 * be manipulated using get/setValue().
 */
public interface IndexAdapter extends ValueModel {

	/**
	 * Returns the index of the item which is currently being manipulated.
	 */
	public int getIndex();

	/**
	 * Set the index of the item to manipulate.
	 */
	public void setIndex(int index);

	/**
	 * Code to be executed when the index has changed.
	 */
	public void fireIndexedObjectChanged();
}
