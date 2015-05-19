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

import org.valkyriercp.application.Application;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * An action command that causes the application to exit.
 *
 * @author Keith Donald
 */
public class ExitCommand extends ApplicationWindowAwareCommand {

    /** The identifier of this command. */
    public static final String ID = "exitCommand";

    /**
     * Creates a new {@code ExitCommand} with an id of {@value #ID}.
     */
    public ExitCommand() {
        super(ID);
    }

    /**
     * Closes the single {@link Application} instance.
     *
     * @see Application#close()
     */
    protected void doExecuteCommand() {
        ValkyrieRepository.getInstance().getApplicationConfig().application().close();
    }

}
