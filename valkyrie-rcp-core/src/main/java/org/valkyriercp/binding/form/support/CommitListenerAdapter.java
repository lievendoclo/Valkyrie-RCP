package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.form.CommitListener;
import org.valkyriercp.binding.form.FormModel;

/**
 * Adapter for the CommitListener interface
 */
public abstract class CommitListenerAdapter implements CommitListener {
    public void preCommit(FormModel formModel) {
    }

    public void postCommit(FormModel formModel) {
    }

}
