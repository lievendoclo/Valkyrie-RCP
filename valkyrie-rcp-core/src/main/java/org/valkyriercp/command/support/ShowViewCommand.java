package org.valkyriercp.command.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PropertyNotSetException;
import org.valkyriercp.application.ViewDescriptor;

/**
 * An action command for displaying a {@link org.valkyriercp.application.View}
 * based on a provided {@link ViewDescriptor}.
 */
public class ShowViewCommand extends ApplicationWindowAwareCommand implements
		InitializingBean {

	private ViewDescriptor viewDescriptor;

	/**
	 * Creates a new uninitialized {@code ShowViewCommand}. The
	 * {@code applicationWindow} and {@code viewDescriptor} properties must be
	 * set before using the new instance.
	 */
	public ShowViewCommand() {
		// do nothing
	}

	/**
	 * Creates a new @code ShowViewCommand}. The {@code applicationWindow} and
	 * {@code viewDescriptor} properties must be set before using the new
	 * instance.
	 */
	public ShowViewCommand(String id, ViewDescriptor descriptor) {
		setViewDescriptor(descriptor);
		setId(id);
		setEnabled(true);
        PropertyNotSetException.throwIfNull(this.viewDescriptor,
                "viewDescriptor", getClass());
	}

	/**
	 * Creates a new {@code ShowViewCommand} with the given view descriptor and
	 * associated application window. The new instance will have a command
	 * identifier equal to the id from the view descriptor, the command will be
	 * enabled by default.
	 * 
	 * @param viewDescriptor
	 *            The object describing the view that this command will be
	 *            responsible for showing.
	 * @param applicationWindow
	 *            The application window that the command belongs to.
	 * 
	 * @throw IllegalArgumentException if {@code viewDescriptor} or
	 *        {@code applicationWindow} are null.
	 */
	public ShowViewCommand(ViewDescriptor viewDescriptor,
			ApplicationWindow applicationWindow) {
		Assert.notNull(applicationWindow, "applicationWindow");
		setViewDescriptor(viewDescriptor);
		setApplicationWindow(applicationWindow);
		setEnabled(true);
        PropertyNotSetException.throwIfNull(this.viewDescriptor,
                "viewDescriptor", getClass());
	}

	/**
	 * Sets the descriptor for the view that is to be opened by this command
	 * object. This command object will be assigned the id, label, icon, and
	 * caption from the given view descriptor.
	 * 
	 * @param viewDescriptor
	 *            The view descriptor, cannot be null.
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code viewDescriptor} is null.
	 */
	public final void setViewDescriptor(ViewDescriptor viewDescriptor) {
		Assert.notNull(viewDescriptor, "viewDescriptor");
		setId(viewDescriptor.getId());
		setLabel(viewDescriptor.getShowViewCommandLabel());
		setIcon(viewDescriptor.getIcon());
		setCaption(viewDescriptor.getCaption());
		this.viewDescriptor = viewDescriptor;
	}

	/**
	 * Causes the view described by this instance's view descriptor to be shown.
	 */
	@Override
	protected void doExecuteCommand() {
		// FIXME getApplicationWindow can potentially return null. This should
		// probably be
		// made an invariant on the ApplicationWindowAwareCommand, that it never
		// returns null.
		// Same applies to ApplicationWindow.getPage(), can also return null
		getApplicationWindow().getPage().showView(this.viewDescriptor.getId());
	}

}
