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
package org.valkyriercp.application.docking;

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.MultiViewPageDescriptor;

/**
 * @author Rogan Dawes
 */
public class VLDockingPageDescriptor extends MultiViewPageDescriptor {

    private VLDockingLayoutManager layoutManager;

    private Resource initialLayout;

    /**
     * @return the layoutManager
     */
    public VLDockingLayoutManager getLayoutManager() {
        return this.layoutManager;
    }

    /**
     * @param layoutManager the layoutManager to set
     */
    public void setLayoutManager(VLDockingLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * @return the initialLayout
     */
    public Resource getInitialLayout() {
        return this.initialLayout;
    }

    /**
     * @param initialLayout the initialLayout to set
     */
    public void setInitialLayout(Resource initialLayout) {
        this.initialLayout = initialLayout;
    }

}
