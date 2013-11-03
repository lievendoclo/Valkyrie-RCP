package org.valkyriercp.command.config;

import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;

public class DefaultCommandButtonConfigurer implements CommandButtonConfigurer {

    /**
     * {@inheritDoc}
     */
    public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
        Assert.notNull(button, "The button to configure cannot be null.");
        Assert.notNull(faceDescriptor, "The command face descriptor cannot be null.");
        faceDescriptor.configureLabel(button);
        faceDescriptor.configureIcon(button);
        faceDescriptor.configureColor(button);
        button.setToolTipText(faceDescriptor.getCaption());
    }

}