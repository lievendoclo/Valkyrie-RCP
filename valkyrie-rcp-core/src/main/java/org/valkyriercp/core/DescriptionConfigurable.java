package org.valkyriercp.core;

/**
 * Implemented by object that can be described for display in a GUI.
 *
 * @author Keith Donald
 */
public interface DescriptionConfigurable {

    /**
     * @param shortDescription
     */
    public void setCaption(String shortDescription);

    /**
     * @param longDescription
     */
    public void setDescription(String longDescription);
}
