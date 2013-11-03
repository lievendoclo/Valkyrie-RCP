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
