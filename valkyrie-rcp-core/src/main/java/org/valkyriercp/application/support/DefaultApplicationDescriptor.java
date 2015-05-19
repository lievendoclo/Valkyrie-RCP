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