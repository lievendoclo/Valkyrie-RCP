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
