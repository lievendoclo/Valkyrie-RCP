package org.valkyriercp.taskpane;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.command.config.CommandFaceDescriptor;
import org.valkyriercp.command.config.DefaultCommandButtonConfigurer;
import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

public class JTaskPaneCommandButtonConfigurer extends DefaultCommandButtonConfigurer
{
    public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor)
    {

        Assert.notNull(button, "The button to configure cannot be null.");
        Assert.notNull(faceDescriptor, "The command face descriptor cannot be null.");

        if (StringUtils.hasText(faceDescriptor.getDescription()))
            button.setText(faceDescriptor.getDescription());
        else
            button.setText(faceDescriptor.getText());

        button.setToolTipText(faceDescriptor.getCaption());

        if (faceDescriptor.getLargeIcon() != null)
            faceDescriptor.configureIconInfo(button, true);
        else
            faceDescriptor.configureIcon(button);
        try
        {
            button.setUI((ButtonUI) Class.forName((String) UIManager.get("LinkButtonUI")).newInstance());
        }
        catch (Exception e)
        {

        }
    }
}
