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
package org.valkyriercp.dialog;

import javax.swing.*;

/**
 * Forces calls to constructor to have greater clarity, by using a type-safe
 * enumeration instead of integers.
 */
public final class CloseAction {

    /**
     * Dispose the dialog on close; reclaiming any dialog resources in memory.
     */
    public static final CloseAction DISPOSE = new CloseAction(WindowConstants.DISPOSE_ON_CLOSE);

    /**
     * Hide the dialog on close; leaving it in memory for assumed re-display at
     * a later point.
     */
    public static final CloseAction HIDE = new CloseAction(WindowConstants.HIDE_ON_CLOSE);

    private final int action;

    private CloseAction(int action) {
        this.action = action;
    }

    public int getValue() {
        return this.action;
    }
}
