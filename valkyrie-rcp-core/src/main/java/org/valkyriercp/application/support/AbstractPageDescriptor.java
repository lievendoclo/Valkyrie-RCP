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

import org.springframework.beans.factory.BeanNameAware;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.ShowPageCommand;
import org.valkyriercp.core.support.LabeledObjectSupport;

public abstract class AbstractPageDescriptor extends LabeledObjectSupport implements PageDescriptor, BeanNameAware {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBeanName(String name) {
        setId(name);
    }

    public CommandButtonLabelInfo getShowPageCommandLabel() {
        return getLabel();
    }

    public ActionCommand createShowPageCommand(ApplicationWindow window) {
        return new ShowPageCommand(this, window);
    }
}
