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
package org.valkyriercp.widget;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.application.support.DefaultViewDescriptor;

public final class WidgetViewDescriptor extends DefaultViewDescriptor {
    /**
     * Widget to create the view.
     */
    private WidgetProvider<Widget> widget;

    public WidgetViewDescriptor(String id, WidgetProvider<Widget> widget) {
        setId(id);
        setViewClass(WidgetView.class);
        this.widget = widget;
    }

    /**
     * {@inheritDoc}
     */
    public PageComponent createPageComponent() {
        AbstractView sv;
        sv = new WidgetView(getWidget());
        sv.setDescriptor(this);
        return sv;
    }

    public Widget getWidget() {
        return widget.getWidget();
    }
}
