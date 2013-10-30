package org.valkyriercp.command.support;

import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.config.CommandFaceDescriptor;

import javax.swing.*;

/**
 * A {@link CommandGroup} that can only contain ToggleCommands and for which only one togglecommand can be
 * chosen. Comparable to a radio button group.
 */
public class ExclusiveCommandGroup extends CommandGroup {

    private ExclusiveCommandGroupSelectionController controller = new ExclusiveCommandGroupSelectionController();

    public ExclusiveCommandGroup() {
        super();
    }

    public ExclusiveCommandGroup(String groupId) {
        super(groupId);
    }

    public ExclusiveCommandGroup(String groupId, CommandFaceDescriptor faceDescriptor) {
        super(groupId, faceDescriptor);
    }

    public ExclusiveCommandGroup(String groupId, CommandRegistry commandRegistry) {
        super(groupId, commandRegistry);
    }

    public ExclusiveCommandGroup(String id, String encodedLabel) {
        super(id, encodedLabel);
    }

    public ExclusiveCommandGroup(String id, String encodedLabel, Icon icon, String caption) {
        super(id, encodedLabel, icon, caption);
    }

    public void setAllowsEmptySelection(boolean allowsEmptySelection) {
        controller.setAllowsEmptySelection(allowsEmptySelection);
    }

    public boolean getAllowsEmptySelection() {
        return controller.getAllowsEmptySelection();
    }

    protected ExclusiveCommandGroupSelectionController getSelectionController() {
        return controller;
    }

    public boolean isAllowedMember(AbstractCommand prospectiveMember) {
        return prospectiveMember instanceof ToggleCommand;
    }
}
