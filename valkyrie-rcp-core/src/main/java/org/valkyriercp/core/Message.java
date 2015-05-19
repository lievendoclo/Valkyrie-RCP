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
package org.valkyriercp.core;

import javax.swing.*;

public interface Message {
        /**
         * Timestamp in long format of the message creation.
         */
        long getTimestamp();

        /**
         * The textual representation of the message. This is not necessarily how
         * the message will appear on a GUI component.
         *
         * @return textual message, never <code>null</code>, but possibly an
         * empty string.
         */
        String getMessage();

        /**
         * Return the {@link Severity} of this message, possibly <code>null</code>.
         */
        Severity getSeverity();

        /**
         * Decorate the given component with this message.
         *
         * @param component visual component to decorate.
         */
        void renderMessage(JComponent component);

}
