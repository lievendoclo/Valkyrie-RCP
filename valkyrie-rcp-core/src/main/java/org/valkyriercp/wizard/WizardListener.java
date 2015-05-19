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
