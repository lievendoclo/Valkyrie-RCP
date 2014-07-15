package org.valkyriercp.application;

import org.valkyriercp.core.Saveable;

public interface Editor extends PageComponent, Saveable {
    /**
     * Sets the object for this editor to edit.
     *
     * @param input the input
     */
    void setEditorInput(Object input);

    /**
     * Returns the object this editor is using.
     *
     * @return theh input
     */
    Object getEditorInput();
}
