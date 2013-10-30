package org.valkyriercp.core;

import org.valkyriercp.core.support.LabelInfo;

/**
 * An object that can be labeled; where a label consists of text and mnemonic.
 *
 * @author Keith Donald
 */
public interface LabelConfigurable {

    /**
     * Sets the label information.
     * @param label The label information.
     */
    public void setLabelInfo(LabelInfo label);

}
