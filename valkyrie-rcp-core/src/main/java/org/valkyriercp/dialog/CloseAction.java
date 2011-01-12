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
