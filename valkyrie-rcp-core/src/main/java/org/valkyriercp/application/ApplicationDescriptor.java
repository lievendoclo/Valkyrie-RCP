package org.valkyriercp.application;

import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.VisualizedElement;

public interface ApplicationDescriptor extends DescribedElement, VisualizedElement {

    /**
     *  Returns a string used to identify the build number of the application.
     */
    String getBuildId();

    /**
     * Returns a string used to identify the version of the application.
     */
    String getVersion();
}
