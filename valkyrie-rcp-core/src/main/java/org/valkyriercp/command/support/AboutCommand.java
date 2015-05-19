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

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.AboutBox;

/**
 * @author Keith Donald
 */
public class AboutCommand extends ApplicationWindowAwareCommand {
    private static final String ID = "aboutCommand";

    private AboutBox aboutBox = new AboutBox();

    public AboutCommand() {
        super(ID);
    }

    /**
     * Set the path to the HTML file to display.  This is optional.  If
     * it is not specified, then the scrolling HTML panel will not be
     * displayed.
     *
     * @param path
     */
    public void setAboutTextPath(Resource path) {
        this.aboutBox.setAboutTextPath(path);
    }

    protected void doExecuteCommand() {
        aboutBox.display(getApplicationWindow().getControl());
    }

}
