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

import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponentPane;
import org.valkyriercp.application.ViewContext;
import org.valkyriercp.command.ActionCommandExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Mediator between the application and the view. The application uses this
 * class to get the view's local action handlers. The view uses this class to
 * get information about how the view is displayed in the application (for
 * example, on which window.)
 *
 * @author Keith Donald
 */
public class DefaultViewContext implements ViewContext {

    private PageComponentPane pane;

    private ApplicationPage page;

    private Map sharedCommandExecutors;

    public DefaultViewContext(ApplicationPage page, PageComponentPane pane) {
        Assert.notNull(page, "Views must be scoped relative to a page");
        this.page = page;
        this.pane = pane;
    }

    public ApplicationWindow getWindow() {
        return page.getWindow();
    }

    public ApplicationPage getPage() {
        return page;
    }

    public PageComponentPane getPane() {
        return pane;
    }

    public ActionCommandExecutor getLocalCommandExecutor(String commandId) {
        Assert.notNull(commandId, "The commandId is required");
        if (this.sharedCommandExecutors == null) {
            return null;
        }
        return (ActionCommandExecutor)this.sharedCommandExecutors.get(commandId);
    }

    public void register(String commandId, ActionCommandExecutor executor) {
        Assert.notNull(commandId, "The command id is required");
        if (this.sharedCommandExecutors == null) {
            this.sharedCommandExecutors = new HashMap();
        }
        if (executor == null) {
            this.sharedCommandExecutors.remove(commandId);
        } else {
            this.sharedCommandExecutors.put(commandId, executor);
        }
    }
}