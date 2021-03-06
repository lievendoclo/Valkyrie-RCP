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

import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;

import javax.swing.*;
import java.awt.*;

public class SingleViewPageDescriptor extends AbstractPageDescriptor {

    private ViewDescriptor viewDescriptor;

    public SingleViewPageDescriptor(ViewDescriptor viewDescriptor) {
        super();
        this.viewDescriptor = viewDescriptor;
    }

    public String getId() {
        return viewDescriptor.getId();
    }

    public String getDisplayName() {
        return viewDescriptor.getDisplayName();
    }

    public String getCaption() {
        return viewDescriptor.getCaption();
    }

    public String getDescription() {
        return viewDescriptor.getDescription();
    }

    public Icon getIcon() {
        return viewDescriptor.getIcon();
    }

    public Image getImage() {
        return viewDescriptor.getImage();
    }

    public void buildInitialLayout(PageLayoutBuilder layout) {
        layout.addView(viewDescriptor.getId());
    }

}
