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

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.support.AbstractApplicationPage;

import javax.swing.*;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.SplitDockStation;

/**
 * @author Rogan Dawes
 */
public class DockingFramesApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {

    private SplitDockStation desktop;

    private DockFrontend dockFrontend;

    private boolean resolving = false;

    public DockingFramesApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {
        super(window, pageDescriptor);
        desktop = new SplitDockStation();
        dockFrontend = new DockFrontend(window.getControl());
        dockFrontend.addRoot("root", desktop);
    }

    protected PageComponent getPageComponent(Dockable dockable) {
        if (dockable instanceof ViewDescriptorDockable)
            return ((ViewDescriptorDockable) dockable).getPageComponent();
        return null;
    }

    protected Dockable getDockable(PageComponent pageComponent) {
        for (int i = 0; i < desktop.getDockableCount(); i++) {
            Dockable dockable = desktop.getDockable(i);
            PageComponent pc = getPageComponent(dockable);
            if (pc == pageComponent)
                return dockable;
        }
        return null;
    }

    protected boolean giveFocusTo(PageComponent pageComponent) {
        Dockable dockable = getDockable(pageComponent);
        if (dockable == null) {
            return false;
        }
        // Don't request focus here, the DockingDesktop already shifts focus. If requesting focus at this point, 
        //  the DockingDesktop catches this event and fires another focus event. This might cause loops when 
        // maximizing/minimizing/restoring because at that point a remove of the component is done which shifts
        // focus and after setting the correct docking state, a focus request is done. 
        // see RCP-558
        return true;
    }

    public void addView(String viewDescriptorId) {
        showView(viewDescriptorId);
    }

    protected void doAddPageComponent(PageComponent pageComponent) {
        if (resolving)
            return;
        pageComponent.getControl();
        Dockable dockable = getDockable(pageComponent);
        if (dockable != null)
            return;
        dockable = createDockable(pageComponent);
        desktop.drop(dockable);
    }

    protected Dockable createDockable(PageComponent pageComponent) {
        return createDockable(getViewDescriptor(pageComponent.getId()), pageComponent);
    }

    protected Dockable createDockable(ViewDescriptor descriptor, PageComponent pageComponent) {
        return new ViewDescriptorDockable(descriptor, pageComponent);
    }

    protected void doRemovePageComponent(PageComponent pageComponent) {
        Dockable dockable = getDockable(pageComponent);
        if (dockable != null) {
            dockFrontend.remove(dockable);
        }
    }

    protected JComponent createControl() {
        return (JComponent) desktop.getComponent();
    }

    protected void updatePageComponentProperties(PageComponent pageComponent) {
        Dockable dockable = getDockable(pageComponent);
        if(dockable instanceof ViewDescriptorDockable) {
            ViewDescriptorDockable vdd = (ViewDescriptorDockable) dockable;
            vdd.setTitleText(pageComponent.getCaption());
            vdd.setTitleIcon(pageComponent.getIcon());
        }
    }
}
