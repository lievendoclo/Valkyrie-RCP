package org.valkyriercp.command.support;

import org.valkyriercp.command.ActionCommandExecutor;

import java.util.Map;

/**
 * Sub-interface of command delegate that allows for parameterization of the
 * command invocation. Implement if your action needs to be parameterized with
 * "hints" allowing it to execute different based on some context.
 *
 * @author Keith Donald
 */
public interface ParameterizableActionCommandExecutor extends ActionCommandExecutor {

    /**
     * Invoke the specified command, passing in the set of arbitrary execution
     * parameters.
     *
     * @param parameters
     *            the parameter map, consisting of name-value pairs.
     */
    public void execute(Map parameters);
}
