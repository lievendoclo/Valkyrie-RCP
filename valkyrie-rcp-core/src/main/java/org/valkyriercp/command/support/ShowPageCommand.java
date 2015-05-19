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
package org.valkyriercp.command.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.PropertyNotSetException;

/**
 * An action command for displaying a {@link org.valkyriercp.application.ApplicationPage} based on a provided {@link PageDescriptor}.
 */
public class ShowPageCommand extends ApplicationWindowAwareCommand implements InitializingBean {

    private PageDescriptor pageDescriptor;

    /**
     * Creates a new uninitialized {@code ShowPageCommand}. The {@code applicationWindow} and
     * {@code pageDescriptor} properties must be set before using the new instance.
     */
    public ShowPageCommand() {
        //do nothing
    }

    /**
     * Creates a new {@code ShowPageCommand} with the given page descriptor and associated
     * application window. The new instance will have a command identifier equal to the id from
     * the page descriptor, the command will be enabled by default.
     *
     * @param pageDescriptor The object describing the page that this command will be
     * responsible for showing.
     * @param applicationWindow The application window that the command belongs to.
     *
     * @throw IllegalArgumentException if {@code pageDescriptor} or {@code applicationWindow} are null.
     */
    public ShowPageCommand(PageDescriptor pageDescriptor, ApplicationWindow applicationWindow) {
        Assert.notNull(applicationWindow, "applicationWindow");
        setPageDescriptor(pageDescriptor);
        setApplicationWindow(applicationWindow);
        setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    public void afterPropertiesSet() {
        PropertyNotSetException.throwIfNull(getApplicationWindow(), "applicationWindow", getClass());
        PropertyNotSetException.throwIfNull(this.pageDescriptor, "pageDescriptor", getClass());
    }

    /**
     * Sets the descriptor for the page that is to be opened by this command object. This
     * command object will be assigned the id, label, icon, and caption from the given page
     * descriptor.
     *
     * @param pageDescriptor The page descriptor, cannot be null.
     *
     * @throws IllegalArgumentException if {@code pageDescriptor} is null.
     */
    public final void setPageDescriptor(PageDescriptor pageDescriptor) {
        Assert.notNull(pageDescriptor, "pageDescriptor");
        setId(pageDescriptor.getId());
        setLabel(pageDescriptor.getShowPageCommandLabel());
        setIcon(pageDescriptor.getIcon());
        setCaption(pageDescriptor.getCaption());
        this.pageDescriptor = pageDescriptor;
    }

    /**
     * Causes the page described by this instance's page descriptor to be shown.
     */
    protected void doExecuteCommand() {
        //FIXME getApplicationWindow can potentially return null. This should probably be
        //made an invariant on the ApplicationWindowAwareCommand, that it never returns null.
        getApplicationWindow().showPage(this.pageDescriptor);
    }

}
