package org.valkyriercp.application.support;

import org.valkyriercp.application.ApplicationDescriptor;
import org.valkyriercp.core.support.LabeledObjectSupport;

/**
 * Metadata about a application.
 *
 * @author Keith Donald
 */
public class DefaultApplicationDescriptor extends LabeledObjectSupport implements ApplicationDescriptor {
    /** The version of the application */
    private String version;

    /** The build identifier associated with this build of the application */
    private String buildId;

    public DefaultApplicationDescriptor() {
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}