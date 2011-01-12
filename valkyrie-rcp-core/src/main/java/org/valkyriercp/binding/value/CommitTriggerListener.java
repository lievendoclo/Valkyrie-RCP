package org.valkyriercp.binding.value;

/**
 * Interface to be implemented by classes that listen for the commit and revert
 * events fired by a <code>CommitTriger</code>.
 *
 * @author Oliver Hutchison
 * @see CommitTrigger
 */
public interface CommitTriggerListener {

	/**
	 * Called when a commit event is fired.
	 */
	void commit();

	/**
	 * Called when a revert event is fired.
	 */
	void revert();
}