package org.valkyriercp.core.support;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.VisualizedElement;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LabeledObjectSupport extends AbstractPropertyChangePublisher implements DescribedElement, VisualizedElement {
    private String title;
    private String caption;
    private String description;
    private Image image;

    public void setCaption(String caption) {
        String oldValue = caption;
        this.caption = caption;
        firePropertyChange(CAPTION_PROPERTY, oldValue, caption);
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(DESCRIPTION_PROPERTY, oldValue, description);
    }

    public void setTitle(String title) {
        String oldValue = null;
        if (this.title != null) {
            oldValue = getDisplayName();
        }

        this.title = title;
        firePropertyChange(DISPLAY_NAME_PROPERTY, oldValue, getDisplayName());
    }

    public void setImage(Image image) {
        Image oldValue = image;
        this.image = image;
        firePropertyChange("image", oldValue, image);
    }

    public String getDisplayName() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public Icon getIcon() {
        if (image != null)
            return new ImageIcon(image);

        return null;
    }
}
