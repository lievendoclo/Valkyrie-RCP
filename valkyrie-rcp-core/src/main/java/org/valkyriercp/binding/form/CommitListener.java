package org.valkyriercp.binding.form;

/**
 * Listener interface for objects interested in intercepting before and after a
 * form model is commited.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public interface CommitListener {

	/**
	 * Called just before a form model is about to commit.
	 */
	void preCommit(FormModel formModel);

	/**
	 * Called just after a form model is commited.
	 */
	void postCommit(FormModel formModel);
}
