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


