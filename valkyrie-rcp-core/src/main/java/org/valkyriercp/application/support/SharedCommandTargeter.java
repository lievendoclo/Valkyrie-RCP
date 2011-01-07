package org.valkyriercp.application.support;

import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.command.support.TargetableActionCommand;

import java.util.Iterator;

/**
 * Retargets window-scoped, shared commands when the active View associated with
 * an ApplicationPage changes.
 *
 * @author Keith Donald
 */
public class SharedCommandTargeter extends PageComponentListenerAdapter {
    private ApplicationWindow window;

    public SharedCommandTargeter(ApplicationWindow window) {
        Assert.notNull(window, "The application window containing targetable shared commands is required");
        this.window = window;
    }

    public void componentFocusGained(PageComponent component) {
        super.componentFocusGained(component);
        PageComponentContext context = component.getContext();
        for (Iterator i = window.getSharedCommands(); i.hasNext();) {
            TargetableActionCommand globalCommand = (TargetableActionCommand)i.next();
            globalCommand.setCommandExecutor(context.getLocalCommandExecutor(globalCommand.getId()));
        }
    }

}