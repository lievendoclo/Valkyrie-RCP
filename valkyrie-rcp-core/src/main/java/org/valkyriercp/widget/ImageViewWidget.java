package org.valkyriercp.widget;

import org.springframework.core.io.Resource;

import javax.swing.*;
import java.io.IOException;

/**
 * ImageViewingWidget generates a component to view an image.
 *
 * {@inheritDoc}
 *
 * @see #setImage(javax.swing.ImageIcon)
 * @see #setImage(org.springframework.core.io.Resource)
 */
public class ImageViewWidget extends AbstractWidget
{
    private JLabel imageHolder;
    private JComponent mainComponent;
    private boolean hasContent;

    public ImageViewWidget()
    {
        this.imageHolder = new JLabel();

        // below is a small lie to make sure we provide a blank control in case
        // people create us without ready content
        this.hasContent = true;

        this.mainComponent = imageHolder;
    }

    public ImageViewWidget(Resource resource)
    {
        this();
        setImage(resource);
    }

    public ImageViewWidget(ImageIcon image)
    {
        this();
        setImage(image);
    }

    /**
     * Sets the image content of the widget based on a resource
     *
     * @param resource
     *            points to a image resource
     */
    public void setImage(Resource resource)
    {
        ImageIcon image = null;
        if (resource != null && resource.exists())
        {
            try
            {
                image = new ImageIcon(resource.getURL());
            }
            catch (IOException e)
            {
                logger.warn("Error reading resource: " + resource);
                throw new RuntimeException("Error reading resource " + resource, e);
            }
        }
        setImage(image);
    }

    /**
     * Sets the image content based on an ImageIcon
     *
     * @param image
     *            The image icon to be shown
     */
    public void setImage(ImageIcon image)
    {
        this.imageHolder.setIcon(image);
        this.hasContent = (image != null);
    }

    public JComponent getComponent()
    {
        return this.hasContent ? this.mainComponent : new JPanel();
    }
}
