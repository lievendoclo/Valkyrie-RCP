/*
 * Copyright 2002-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.valkyriercp.application.docking;

import bibliothek.gui.dock.DefaultDockable;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.ViewDescriptor;

import java.awt.*;

/**
 * @author Rogan Dawes
 */
public class ViewDescriptorDockable extends DefaultDockable {

    private ViewDescriptor viewDescriptor;

    private PageComponent pageComponent;

    public ViewDescriptorDockable(ViewDescriptor viewDescriptor) {
        this(viewDescriptor, null);
    }

    public ViewDescriptorDockable(ViewDescriptor viewDescriptor,
                                  PageComponent pageComponent) {
        this.viewDescriptor = viewDescriptor;
        this.pageComponent = pageComponent;
        setTitleText(viewDescriptor.getCaption());
        setTitleIcon(viewDescriptor.getIcon());

    }

    public PageComponent getPageComponent() {
        if (pageComponent == null)
            pageComponent = viewDescriptor.createPageComponent();
        return pageComponent;
    }

    public void setPageComponent(PageComponent pageComponent) {
        this.pageComponent = pageComponent;
    }

    /*
    * (non-Javadoc)
    *
    * @see com.vlsolutions.swing.docking.Dockable#getComponent()
    */
    public Component getComponent() {
        return getPageComponent().getControl();
    }
}
