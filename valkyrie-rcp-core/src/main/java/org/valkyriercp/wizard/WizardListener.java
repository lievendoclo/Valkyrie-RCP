package org.valkyriercp.wizard;

import java.util.EventListener;

/**
 * Interface for listening to wizard events.
 */
public interface WizardListener extends EventListener {

    /**
     * Invoked immediately after a wizard's performFinish method is invoked.
     *
     * @param wizard
     *            The wizard whose performFinished method was invoked.
     * @param result
     *            The result of the performFinished method.
     */
    public void onPerformFinish(Wizard wizard, boolean result);

    /**
     * Invoked immediately after a wizard's performCancel method is invoked.
     *
     * @param wizard
     *            The wizard whose performFinished method was invoked.
     * @param result
     *            The result of the performFinished method.
     */
    public void onPerformCancel(Wizard wizard, boolean result);
}
