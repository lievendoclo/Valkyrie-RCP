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

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;

public class WidgetView extends AbstractView
{

    private Widget widget;

    public WidgetView()
    {
        super("widgetView");
    }

    public WidgetView(Widget widget)
    {
        super(widget.getId());
        setWidget(widget);
    }

    public void setWidget(Widget widget)
    {
        this.widget = widget;
    }

    public Widget getWidget()
    {
        return this.widget;
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        widget.registerLocalCommandExecutors(context);
    }

    protected JComponent createControl()
    {
        JComponent widgetComponent = getWidget().getComponent();
        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(widgetComponent, BorderLayout.CENTER);
        Widget widget = getWidget();
        java.util.List<? extends AbstractCommand> widgetCommands = widget.getCommands();
        if (widgetCommands != null)
        {
            JComponent widgetButtonBar = ValkyrieRepository.getInstance().getApplicationConfig().commandManager().createCommandGroup(widgetCommands).createButtonBar(ColumnSpec.decode("fill:pref:nogrow"), RowSpec.decode("fill:default:nogrow"), null);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(widgetButtonBar);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            viewPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        return viewPanel;
    }

    public boolean canClose()
    {
        return getWidget().canClose();
    }

    public void componentFocusGained()
    {
        getWidget().onAboutToShow();
    }

    public void componentFocusLost()
    {
        getWidget().onAboutToHide();
    }
}


