package org.valkyriercp.command;

/**
 * This interface is to be implemented by objects that are able to execute an action command.
 */
public interface ActionCommandExecutor {

    /**
     * Performs the action.
     */
    public void execute();

}
