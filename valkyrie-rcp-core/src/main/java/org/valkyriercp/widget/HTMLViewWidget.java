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

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HTMLViewingWidget generates a component to view HTML data
 *
 * {@inheritDoc}
 *
 * @see #setContent(org.springframework.core.io.Resource)
 * @see #setContent(String)
 */
public class HTMLViewWidget extends AbstractWidget
{
    /** Pane in which the HTML will be shown. */
    private JTextPane textPane;

    /** Complete component with scrollbars and html pane. */
    private JComponent mainComponent;

    private boolean hasContent;

    public HTMLViewWidget()
    {
        this(false);
    }

    public HTMLViewWidget(boolean readOnly)
    {
        this.textPane = new JTextPane();
        this.textPane.setEditorKit(new HTMLEditorKit());
        this.textPane.setEditable(!readOnly);

        JScrollPane scrollPane = new JScrollPane(this.textPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(250, 155));

        // below is a small lie to make sure we provide a blank control in case
        // people create us without ready content
        this.hasContent = true;

        this.mainComponent = scrollPane;
    }

    public HTMLViewWidget(Resource resource)
    {
        this();
        setContent(resource);
    }

    public HTMLViewWidget(Resource resource, boolean readOnly)
    {
        this(readOnly);
        setContent(resource);
    }

    public HTMLViewWidget(String htmlText)
    {
        this();
        setContent(htmlText);
    }

    public HTMLViewWidget(String htmlText, boolean readOnly)
    {
        this(readOnly);
        setContent(htmlText);
    }

    public void setContent(Resource resource)
    {

        String text = null;
        try
        {
            if (resource != null && resource.exists())
            {
                text = FileCopyUtils.copyToString(new BufferedReader(new InputStreamReader(resource
                        .getInputStream())));
            }
        }
        catch (IOException e)
        {
            logger.warn("Error reading resource: " + resource, e);
            throw new RuntimeException("Error reading resource " + resource, e);
        }
        finally
        {
            setContent(text);
        }
    }

    public void setContent(String htmlText)
    {
        this.textPane.setText(htmlText);
        this.hasContent = (htmlText != null && htmlText.length() > 0);
    }

    public JComponent getComponent()
    {
        return this.hasContent ? this.mainComponent : new JPanel();
    }

    @Override
    public String getId() {
        return "htmlViewWidget";
    }
}


