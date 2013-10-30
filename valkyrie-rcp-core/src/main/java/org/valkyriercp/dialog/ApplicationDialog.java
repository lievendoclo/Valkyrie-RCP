package org.valkyriercp.dialog;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.util.GuiStandardUtils;
import org.valkyriercp.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

/**
 * <p>
 * Abstract Base Class for a dialog with standard layout, buttons, and behavior.
 * </p>
 * <p>
 * Use of this class will apply a standard appearance to dialogs in the
 * application.
 * </p>
 * <p>
 * Subclasses implement the body of the dialog (wherein business objects are
 * manipulated), and the action taken by the <code>OK</code> button. Aside
 * from creating the dialog's contentj with {@link #createDialogContentPane()},
 * a proper disposing should be implemented in
 * {@link #disposeDialogContentPane()}.
 * </p>
 * <p>
 * Services of a <code>ApplicationDialog</code> include:
 * <ul>
 * <li>centering on the parent frame</li>
 * <li>reusing the parent's icon</li>
 * <li>standard layout and border spacing, based on Java Look and Feel
 * guidelines.</li>
 * <li>uniform naming style for dialog title</li>
 * <li><code>OK</code> and <code>Cancel</code> buttons at the bottom of the
 * dialog -<code>OK</code> is the default, and the <code>Escape</code> key
 * activates <code>Cancel</code> (the latter works only if the dialog receives
 * the escape keystroke, and not one of its components)</li>
 * <li>by default, modal</li>
 * <li>enabling & disabling of resizing</li>
 * <li>will be shown in taskbar if no parent window has been set, and no
 * applicationwindow is open</li>
 * </ul>
 * </p>
 * <em>Note: Default close behaviour is to dispose the graphical dialog when it closes, you can set the CloseAction to hide if needed.</em>
 *
 * @author Keith Donald
 * @author Jan Hoskens
 */
@Configurable
public abstract class ApplicationDialog implements TitleConfigurable, Guarded {

	private static final String DEFAULT_DIALOG_TITLE = "Application Dialog";

	protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";

	protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";

	protected static final String DEFAULT_FINISH_SUCCESS_MESSAGE_KEY = "defaultFinishSuccessMessage";

	protected static final String DEFAULT_FINISH_SUCCESS_TITLE_KEY = "defaultFinishSuccessTitle";

	protected static final String SUCCESS_FINISH_MESSAGE_KEY = "finishSuccessMessage";

	protected static final String SUCCESS_FINISH_TITLE_KEY = "finishSuccessTitle";

	protected final Log logger = LogFactory.getLog(getClass());

	private final DialogEventHandler dialogEventHandler = new DialogEventHandler();

	private String title;

	private JDialog dialog;

	private Component parentComponent;

	private Window parentWindow;

	private CloseAction closeAction = CloseAction.DISPOSE;

	private boolean defaultEnabled = true;

	private boolean modal = true;

	private boolean resizable = true;

	private Dimension preferredSize;

	private Point location;

	private Component locationRelativeTo;

	private ActionCommand finishCommand;

	private ActionCommand cancelCommand;

	private CommandGroup dialogCommandGroup;

	private boolean displayFinishSuccessMessage;

	private ActionCommand callingCommand;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private CommandManager commandManager;

	/**
	 * Create dialog with default closeAction {@link CloseAction#DISPOSE}. No
	 * parent or title set.
	 *
	 * @see #init()
	 */
	public ApplicationDialog() {
		init();
	}

	/**
	 * Create dialog with default closeAction {@link CloseAction#DISPOSE}.
	 *
	 * @param title text that will appear on dialog's titlebar.
	 * @param parent component serving as parent in it's hierarchy.
	 *
	 * @see #init()
	 */
	public ApplicationDialog(String title, Component parent) {
		setTitle(title);
		setParentComponent(parent);
		init();
	}

	/**
	 * Creates a new application dialog. The actual UI is not initialized until
	 * showDialog() is called.
	 *
	 * @param title text which appears in the title bar after the name of the
	 * application.
	 * @param parent frame to which this dialog is attached.
	 * @param closeAction sets the behaviour of the dialog upon close. Default
	 * closeAction is {@link CloseAction#DISPOSE}.
	 *
	 * @see #init()
	 */
	public ApplicationDialog(String title, Component parent, CloseAction closeAction) {
		setTitle(title);
		setParentComponent(parent);
		setCloseAction(closeAction);
		init();
	}

	/**
	 * Hook called in constructor. Add specific initialization code here.
	 */
	protected void init() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTitle(String title) {
		this.title = title;
		if (dialog != null) {
			dialog.setTitle(getTitle());
		}
	}

	/**
	 * Returns the title of this dialog. If no specific title has been set, the
	 * calling command's text will be used. If that doesn't yield a result, the
	 * default title is returned.
	 *
	 * @see #getCallingCommandText()
	 * @see #DEFAULT_DIALOG_TITLE
	 */
	protected String getTitle() {
		if (!StringUtils.hasText(this.title)) {
			if (StringUtils.hasText(getCallingCommandText()))
				return getCallingCommandText();

			return DEFAULT_DIALOG_TITLE;
		}
		return this.title;
	}

	/**
	 * The parent Component that will be used to extract the Frame/Dialog owner
	 * for the JDialog at creation. You may pass a Window/Frame that will be
	 * used directly as parent for the JDialog, or you can pass the component
	 * which has one of both in it's parent hierarchy. The latter option can be
	 * handy when you're locally implementing Components without a direct
	 * -connection to/notion of- a Window/Frame.
	 *
	 * @param parentComponent Component that is a Frame/Window or has one in its
	 * parent hierarchy.
	 */
	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}

	/**
	 * Returns the parent Component.
	 *
	 * @return
	 * @see #setParentComponent(Component)
	 */
	public Component getParentComponent() {
		return this.parentComponent;
	}

	/**
	 * Set the {@link CloseAction} of this dialog. Default action is
	 * {@link CloseAction#DISPOSE} which disposes the visual dialog upon
	 * closing. When using {@link CloseAction#HIDE} the visual components are
	 * cached and reused.
	 *
	 * @param action the {@link CloseAction} to use when closing the dialog.
	 */
	public void setCloseAction(CloseAction action) {
		this.closeAction = action;
	}

	/**
	 * When opening the dialog, the finish button can be enabled by default.
	 *
	 * @param enabled <code>true</code> when the finish button should be
	 * enabled by default, <code>false</code> otherwise.
	 */
	public void setDefaultEnabled(boolean enabled) {
		this.defaultEnabled = enabled;
	}

	/**
	 * Set the modal property of the dialog.
	 *
	 * @see JDialog#setModal(boolean)
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}

	/**
	 * Set the resizable property of the dialog.
	 *
	 * @see JDialog#setResizable(boolean)
	 */
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	/**
	 * Set a specific location for the JDialog to popup.
	 *
	 * @param location point on screen where to place the JDialog.
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Set a relative location for the JDialog to popup.
	 *
	 * @see Window#setLocationRelativeTo(Component)
	 */
	public void setLocationRelativeTo(Component locationRelativeTo) {
		this.locationRelativeTo = locationRelativeTo;
	}

	/**
	 * Set the preferrred size for the JDialog.
	 *
	 * @see JComponent#setPreferredSize(Dimension)
	 */
	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}

	/**
	 * Enable/disable the finish command of the dialog.
	 */
	public void setEnabled(boolean enabled) {
		setFinishEnabled(enabled);
	}

	/**
	 * Message to show upon succesful completion.
	 */
	public void setDisplayFinishSuccessMessage(boolean displayFinishSuccessMessage) {
		this.displayFinishSuccessMessage = displayFinishSuccessMessage;
	}

	/**
	 * Set the command that opened this dialog.
	 *
	 * @see #getFinishSuccessMessage()
	 * @see #getFinishSuccessTitle()
	 */
	public void setCallingCommand(ActionCommand callingCommand) {
		this.callingCommand = callingCommand;
	}

	/**
	 * Enable/disable the finish command.
	 */
	protected void setFinishEnabled(boolean enabled) {
		if (isControlCreated()) {
			finishCommand.setEnabled(enabled);
		}
	}

	/**
	 * Returns whether this Dialog is enabled.
	 */
	public boolean isEnabled() {
		if (isControlCreated())
			return finishCommand.isEnabled();

		return false;
	}

	/**
	 * Returns <code>true</code> if the JDialog is showing.
	 *
	 * @see JDialog#isShowing()
	 */
	public boolean isShowing() {
		if (!isControlCreated()) {
			return false;
		}
		return dialog.isShowing();
	}

	/**
	 * Returns <code>true</code> if the JDialog is constructed.
	 */
	public boolean isControlCreated() {
		return dialog != null;
	}

	/**
	 * Return the JDialog, create it if needed (lazy).
	 */
	public JDialog getDialog() {
		if (!isControlCreated()) {
			createDialog();
		}
		return dialog;
	}

	/**
	 * Return the contentPane of the dialog.
	 *
	 * @see JDialog#getContentPane()
	 */
	protected Container getDialogContentPane() {
		Assert.state(isControlCreated(), "The wrapped JDialog control has not yet been created.");
		return dialog.getContentPane();
	}

	/**
	 * <p>
	 * Show the dialog. The dialog will be created if it doesn't exist yet.
	 * Before setting the dialog visible, a hook method onAboutToShow is called
	 * and the location will be set.
	 * </p>
	 * <p>
	 * When showing the dialog several times, it will always be opened on the
	 * location that has been set, or relative to the parent. (former location
	 * will not persist)
	 * </p>
	 */
	public void showDialog() {
		if (!isControlCreated()) {
			createDialog();
		}
		if (!isShowing()) {
			onAboutToShow();
			if (getLocation() != null) {
				dialog.setLocation(getLocation());
                dialog.setPreferredSize(getPreferredSize());
			}
			else {
				WindowUtils.centerOnParent(dialog, getLocationRelativeTo());
			}

			dialog.setVisible(true);
		}
	}

	/**
	 * Subclasses should call if layout of the dialog components changes.
	 */
	protected void componentsChanged() {
		if (isControlCreated()) {
			dialog.pack();
		}
	}

	/**
	 * Builds/initializes the dialog and all of its components.
	 * <p>
	 * Follows the Java Look and Feel guidelines for spacing elements.
	 */
	protected final void createDialog() {
		constructDialog();
		addDialogComponents();
		attachListeners();
		registerDefaultCommand();
		onInitialized();

		dialog.pack();
	}

	/**
	 * Construct the visual dialog frame on which the content needs to be added.
	 */
	private void constructDialog() {
		if (getParentWindow() instanceof Frame) {
			dialog = new JDialog((Frame) getParentWindow(), getTitle(), modal);
		}
		else {
			dialog = new JDialog((Dialog) getParentWindow(), getTitle(), modal);
		}

		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.setResizable(resizable);

		initStandardCommands();
		addCancelByEscapeKey();
	}

	/**
	 * <p>
	 * --jh-- This method is copied from JOptionPane. I'm still trying to figure
	 * out why they chose to have a static method with package visibility for
	 * this one instead of just making it public.
	 * </p>
	 *
	 * Returns the specified component's toplevel <code>Frame</code> or
	 * <code>Dialog</code>.
	 *
	 * @param parentComponent the <code>Component</code> to check for a
	 * <code>Frame</code> or <code>Dialog</code>
	 * @return the <code>Frame</code> or <code>Dialog</code> that contains
	 * the component, or the default frame if the component is <code>null</code>,
	 * or does not have a valid <code>Frame</code> or <code>Dialog</code>
	 * parent
	 * @exception HeadlessException if
	 * <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 */
	public static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return JOptionPane.getRootFrame();
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
			return (Window) parentComponent;
		return getWindowForComponent(parentComponent.getParent());
	}

	/**
	 * Initialize the standard commands needed on a Dialog: Ok/Cancel.
	 */
	private void initStandardCommands() {
		finishCommand = new ActionCommand(getFinishCommandId()) {
			public void doExecuteCommand() {
				boolean result = onFinish();
				if (result) {
					if (getDisplayFinishSuccessMessage()) {
						showFinishSuccessMessageDialog();
					}
					executeCloseAction();
				}
			}
		};
		finishCommand.setSecurityControllerId(getFinishSecurityControllerId());
		finishCommand.setEnabled(defaultEnabled);

		cancelCommand = new ActionCommand(getCancelCommandId()) {

			public void doExecuteCommand() {
				onCancel();
			}
		};
	}

	/**
	 * Subclasses may override to return a custom message key, default is
	 * "okCommand", corresponding to the "&OK" label.
	 *
	 * @return The message key to use for the finish ("ok") button
	 */
	protected String getFinishCommandId() {
		return DEFAULT_FINISH_COMMAND_ID;
	}

	/**
	 * Subclasses may override to return a security controller id to be attached
	 * to the finish command. The default is null, no controller.
	 *
	 * @return security controller id, or null if none
	 */
	protected String getFinishSecurityControllerId() {
		return null;
	}

	/**
	 * Request invocation of the action taken when the user hits the
	 * <code>OK</code> (finish) button.
	 *
	 * @return true if action completed successfully; false otherwise.
	 */
	protected abstract boolean onFinish();

	/**
	 * Return the message that needs to be set on a succesful finish.
	 */
	protected boolean getDisplayFinishSuccessMessage() {
		return displayFinishSuccessMessage;
	}

	/**
	 * Opens a dialog which contains the sussesful finish message.
	 *
	 * @see #getFinishSuccessTitle()
	 * @see #getFinishSuccessMessage()
	 */
	protected void showFinishSuccessMessageDialog() {
		MessageDialog messageDialog = new MessageDialog(getFinishSuccessTitle(), getDialog(), getFinishSuccessMessage());
		messageDialog.showDialog();
	}

	/**
	 * Returns the message to use upon succesful finish.
	 */
	protected String getFinishSuccessMessage() {
		ActionCommand callingCommand = getCallingCommand();
		if (callingCommand != null) {
			String[] successMessageKeys = new String[] { callingCommand.getId() + "." + SUCCESS_FINISH_MESSAGE_KEY,
					DEFAULT_FINISH_SUCCESS_MESSAGE_KEY };
			return applicationConfig.messageResolver().getMessage(successMessageKeys, getFinishSuccessMessageArguments());
		}
		return applicationConfig.messageResolver().getMessage(DEFAULT_FINISH_SUCCESS_MESSAGE_KEY);
	}

	/**
	 * Returns the command that opened this dialog.
	 */
	protected ActionCommand getCallingCommand() {
		return callingCommand;
	}

	/**
	 * Returns the arguments to use in the succesful finish message.
	 */
	protected Object[] getFinishSuccessMessageArguments() {
		return new Object[0];
	}

	/**
	 * Returns the title to use upon succesful finish.
	 */
	protected String getFinishSuccessTitle() {
		ActionCommand callingCommand = getCallingCommand();
		if (callingCommand != null) {
			String[] successTitleKeys = new String[] { callingCommand.getId() + "." + SUCCESS_FINISH_TITLE_KEY,
					DEFAULT_FINISH_SUCCESS_TITLE_KEY };
			return applicationConfig.messageResolver().getMessage(successTitleKeys, getFinishSuccessTitleArguments());
		}
		return applicationConfig.messageResolver().getMessage(DEFAULT_FINISH_SUCCESS_TITLE_KEY);
	}

	/**
	 * Returns the arguments to use in the finish succesful title.
	 */
	protected Object[] getFinishSuccessTitleArguments() {
		if (StringUtils.hasText(getCallingCommandText()))
			return new Object[] { getCallingCommandText() };

		return new Object[0];
	}

	/**
	 * Return the text of the command that opened this dialog.
	 */
	private String getCallingCommandText() {
		return getCallingCommand() != null ? getCallingCommand().getText() : null;
	}

	/**
	 * Returns the id for the cancel command.
	 */
	protected String getCancelCommandId() {
		return DEFAULT_CANCEL_COMMAND_ID;
	}

	/**
	 * Returns the finish command.
	 */
	protected ActionCommand getFinishCommand() {
		return finishCommand;
	}

	/**
	 * Returns the cancel command.
	 */
	protected ActionCommand getCancelCommand() {
		return cancelCommand;
	}

	/**
	 * Force the escape key to call the same action as pressing the Cancel
	 * button. This does not always work. See class comment.
	 */
	private void addCancelByEscapeKey() {
		int noModifiers = 0;
		KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, noModifiers, false);
		addActionKeyBinding(escapeKey, cancelCommand.getId());
	}

	/**
	 * Add an action key binding to this dialog.
	 *
	 * @param key the {@link KeyStroke} that triggers the command/action.
	 * @param actionKey id of command that will be triggered by the {@link KeyStroke}.
	 *
	 * @see #addActionKeyBinding(KeyStroke, String, Action)
	 */
	protected void addActionKeyBinding(KeyStroke key, String actionKey) {
		if (actionKey == finishCommand.getId()) {
			addActionKeyBinding(key, actionKey, finishCommand.getActionAdapter());
		}
		else if (actionKey == cancelCommand.getId()) {
			addActionKeyBinding(key, actionKey, cancelCommand.getActionAdapter());
		}
		else {
			throw new IllegalArgumentException("Unknown action key " + actionKey);
		}
	}

	/**
	 * Add an action key binding to this dialog.
	 *
	 * @param key the {@link KeyStroke} that triggers the command/action.
	 * @param actionKey id of the action.
	 * @param action {@link Action} that will be triggered by the {@link KeyStroke}.
	 *
	 * @see #getActionMap()
	 * @see #getInputMap()
	 * @see ActionMap#put(Object, Action)
	 * @see InputMap#put(KeyStroke, Object)
	 */
	protected void addActionKeyBinding(KeyStroke key, String actionKey, Action action) {
		getInputMap().put(key, actionKey);
		getActionMap().put(actionKey, action);
	}

	/**
	 * Return the {@link ActionMap} of the dialog.
	 *
	 * @see JLayeredPane#getActionMap()
	 */
	protected ActionMap getActionMap() {
		return getDialog().getLayeredPane().getActionMap();
	}

	/**
	 * Return the {@link InputMap} of the dialog.
	 *
	 * @see JLayeredPane#getInputMap(int)
	 */
	protected InputMap getInputMap() {
		return getDialog().getLayeredPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Subclasses may override to customize how this dialog is built.
	 */
	protected void addDialogComponents() {
		JComponent dialogContentPane = createDialogContentPane();
		GuiStandardUtils.attachDialogBorder(dialogContentPane);
		if (getPreferredSize() != null) {
			dialogContentPane.setPreferredSize(getPreferredSize());
		}
		getDialogContentPane().add(dialogContentPane);
		getDialogContentPane().add(createButtonBar(), BorderLayout.SOUTH);
	}

	/**
	 * Return the location of the dialog.
	 */
	protected Point getLocation() {
		return location;
	}

	/**
	 * Return the relative location of the dialog.
	 */
	protected Component getLocationRelativeTo() {
		return locationRelativeTo;
	}

	/**
	 * Return the preferred size for the dialog.
	 */
	protected Dimension getPreferredSize() {
		return preferredSize;
	}

	/**
	 * Return the GUI which allows the user to manipulate the business objects
	 * related to this dialog. This GUI will be placed above the <code>OK</code>
	 * and <code>Cancel</code> buttons, in a standard manner.
	 *
	 * <p>
	 * Any components/objects created at this point need to be disposed in
	 * {@link #disposeDialogContentPane()}.
	 * </p>
	 *
	 * @see #disposeDialogContentPane()
	 */
	protected abstract JComponent createDialogContentPane();

	/**
	 * Attach the handler that invokes the lifecycle methods on the
	 * <code>ApplicationDialog</code>.
	 *
	 * @see DialogEventHandler
	 */
	protected final void attachListeners() {
		dialog.addWindowFocusListener(dialogEventHandler);
		dialog.addWindowListener(dialogEventHandler);
	}

	/**
	 * Return a standardized row of command buttons, right-justified and all of
	 * the same size, with OK as the default button, and no mnemonics used, as
	 * per the Java Look and Feel guidelines.
	 */
	protected JComponent createButtonBar() {
		this.dialogCommandGroup = commandManager.createCommandGroup(null, getCommandGroupMembers());
		JComponent buttonBar = this.dialogCommandGroup.createButtonBar();
		GuiStandardUtils.attachDialogBorder(buttonBar);
		return buttonBar;
	}

	/**
	 * Template getter method to return the commands to populate the dialog
	 * button bar.
	 *
	 * @return The array of commands (may also be a separator or glue
	 * identifier)
	 */
	protected java.util.List<? extends AbstractCommand> getCommandGroupMembers() {
		return Lists.<AbstractCommand>newArrayList(getFinishCommand(), getCancelCommand());
	}

	/**
	 * Register the finish button as the default dialog button.
	 */
	protected void registerDefaultCommand() {
		if (isControlCreated()) {
			finishCommand.setDefaultButtonIn(getDialog());
		}
	}

	/**
	 * Register the cancel button as the default dialog button.
	 */
	protected final void registerCancelCommandAsDefault() {
		if (isControlCreated()) {
			cancelCommand.setDefaultButtonIn(getDialog());
		}
	}

    protected ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    /**
	 * Register the provided button as the default dialog button. The button
	 * must be present on the dialog.
	 *
	 * @param command The button to become the default.
	 */
	protected final void registerDefaultCommand(ActionCommand command) {
		if (isControlCreated()) {
			command.setDefaultButtonIn(getDialog());
		}
	}

	/**
	 * Template lifecycle method invoked after the dialog control is
	 * initialized.
	 */
	protected void onInitialized() {
	}

	/**
	 * Template lifecycle method invoked right before the dialog is to become
	 * visible.
	 */
	protected void onAboutToShow() {
	}

	/**
	 * Template lifecycle method invoked when the dialog gains focus.
	 */
	protected void onWindowGainedFocus() {
	}

	/**
	 * Template lifecycle method invoked when the dialog is activated.
	 */
	protected void onWindowActivated() {
	}

	/**
	 * Template lifecycle method invoked when the dialog loses focus.
	 */
	protected void onWindowLostFocus() {
	}

	/**
	 * Template lifecycle method invoked when the dialog's window is closing.
	 */
	protected void onWindowClosing() {
	}

	/**
	 * Handle a dialog cancellation request.
	 */
	protected void onCancel() {
		executeCloseAction();
	}

	/**
	 * Select the appropriate close logic.
	 */
	private void executeCloseAction() {
		if (closeAction == CloseAction.HIDE) {
			hide();
		}
		else {
			dispose();
		}
	}

	/**
	 * Close and dispose of the visual dialog. This forces the dialog to be
	 * re-built on the next show. Any subclasses that are creating visual
	 * components and holding references to them should dispose them when the
	 * surrounding dialog is disposed by implementing
	 * {@link #disposeDialogContentPane()}. Any other objects that are created
	 * in {@link #createDialogContentPane()} can be handled here as well.
	 *
	 * @see #disposeDialogContentPane()
	 */
	protected final void dispose() {
		if (dialog != null) {
			onWindowClosing();
			disposeDialogContentPane();
			dialog.dispose();
			dialog = null;
		}
	}

	/**
	 * Cleanup any components/objects that are created during
	 * {@link #createDialogContentPane()}. This method is called if the
	 * {@link CloseAction} is set to {@link CloseAction#DISPOSE} and the dialog
	 * is being closed. This ensures that when disposing the surrounding dialog,
	 * the content pane can be disposed as well.
	 *
	 * @see #createDialogContentPane()
	 * @see #dispose()
	 */
	protected void disposeDialogContentPane() {
	}

	/**
	 * Hide the dialog. This differs from dispose in that the dialog control
	 * stays cached in memory.
	 */
	protected final void hide() {
		if (dialog != null) {
			onWindowClosing();
			this.dialog.setVisible(false);
		}
	}

	/**
	 * Returns the parent window based on the internal parent Component. Will
	 * search for a Window in the parent hierarchy if needed (when parent
	 * Component isn't a Window).
	 *
	 * @return the parent window
	 */
	public Window getParentWindow() {
		if (parentWindow == null) {
			if ((parentComponent == null) && (applicationConfig.windowManager().getActiveWindow() != null)) {
				parentWindow = applicationConfig.windowManager().getActiveWindow().getControl();
			}
			else {
				parentWindow = getWindowForComponent(parentComponent);
			}
		}
		return parentWindow;
	}

	/**
	 * Handler that will be registered as listener on the dialog.
	 */
	private class DialogEventHandler extends WindowAdapter implements WindowFocusListener {
		public void windowActivated(WindowEvent e) {
			onWindowActivated();
		}

		public void windowClosing(WindowEvent e) {
			getCancelCommand().execute();
		}

		public void windowGainedFocus(WindowEvent e) {
			onWindowGainedFocus();
		}

		public void windowLostFocus(WindowEvent e) {
			onWindowLostFocus();
		}
	}
}
