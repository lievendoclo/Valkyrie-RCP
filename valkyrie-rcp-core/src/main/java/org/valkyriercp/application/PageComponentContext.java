package org.valkyriercp.application;

import org.valkyriercp.command.ActionCommandExecutor;

public interface PageComponentContext {
    public ApplicationWindow getWindow();

    public ApplicationPage getPage();

    public PageComponentPane getPane();

    public ActionCommandExecutor getLocalCommandExecutor(String commandId);

    /**
     * Register a local handler for a global command.
     * @param commandId the global command id
     * @param localExecutor the local handler
     */
    public void register(String commandId, ActionCommandExecutor localExecutor);

}