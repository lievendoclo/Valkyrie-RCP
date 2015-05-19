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

    @Override
    public String getId() {
        return "imageViewWidget";
    }
}
