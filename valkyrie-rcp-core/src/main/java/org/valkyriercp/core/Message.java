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
