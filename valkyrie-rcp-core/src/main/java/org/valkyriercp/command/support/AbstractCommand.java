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
package org.valkyriercp.command.support;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.SecuredActionCommandExecutor;
import org.valkyriercp.command.config.*;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.factory.MenuFactory;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>
 * Base class for commands. Extend this class by implementing the
 * {@link #execute()} method.
 * </p>
 *
 * <p>
 * Most (if not all) commands result in a UI component. Several methods are
 * provided here to deliver abstractButtons or menuItems. Configuring this
 * visual aspect of the command is done by a number of
 * {@link CommandFaceDescriptor}s. One of these will be registered as the
 * default while others can be used to create a different look by providing a
 * faceDescriptorId.
 * </p>
 *
 * @see CommandFaceDescriptor
 *
 * @author Keith Donald
 * @author Jan Hoskens
 *
 */
public abstract class AbstractCommand extends AbstractPropertyChangePublisher implements
        BeanNameAware, SecuredActionCommandExecutor {

	/** Property used to notify changes in the <em>enabled</em> state. */
	public static final String ENABLED_PROPERTY_NAME = "enabled";

	/** Property used to notify changes in the <em>visible</em> state. */
	public static final String VISIBLE_PROPERTY_NAME = "visible";

	private static final String DEFAULT_FACE_DESCRIPTOR_ID = "default";

	private String id;

	private String defaultFaceDescriptorId = DEFAULT_FACE_DESCRIPTOR_ID;

	private boolean enabled = true;

	private boolean visible = true;

	private boolean authorized = true;

	private String securityControllerId = null;

    private String[] authorities;

	private Map faceButtonManagers;

	private CommandServices commandServices;

	private CommandFaceDescriptorRegistry faceDescriptorRegistry;

	private Boolean oldEnabledState;

	private Boolean oldVisibleState;


	/**
	 * Default constructor. Id can be set by context.
	 *
	 * @see BeanNameAware
	 */
	protected AbstractCommand() {
		this(null);
	}

	/**
	 * Constructor providing an id for configuration.
	 *
	 * @param id
	 */
	protected AbstractCommand(String id) {
		super();
		setId(id);
		// keep track of enable state for buttons
		addEnabledListener(new ButtonEnablingListener());
		// keep track of visible state for buttons
		addPropertyChangeListener(VISIBLE_PROPERTY_NAME, new ButtonVisibleListener());
        afterPropertiesSet();
	}

	/**
	 * Constructor providing id and encodedLabel. A default FaceDescriptor will
	 * be created by passing the encodedLabel.
	 *
	 * @param id
	 * @param encodedLabel label to use when creating the default
	 * {@link CommandFaceDescriptor}.
	 */
	protected AbstractCommand(String id, String encodedLabel) {
		this(id, new CommandFaceDescriptor(encodedLabel));
	}

	/**
	 * Constructor providing id and a number of parameters to create a default
	 * {@link CommandFaceDescriptor}.
	 *
	 * @param id
	 * @param encodedLabel label for the default {@link CommandFaceDescriptor}.
	 * @param icon icon for the default {@link CommandFaceDescriptor}.
	 * @param caption caption for the default {@link CommandFaceDescriptor}.
	 */
	protected AbstractCommand(String id, String encodedLabel, Icon icon, String caption) {
		this(id, new CommandFaceDescriptor(encodedLabel, icon, caption));
	}

	/**
	 * Constructor providing an id and the default FaceDescriptor.
	 *
	 * @param id
	 * @param faceDescriptor the default FaceDescriptor to use.
	 */
	protected AbstractCommand(String id, CommandFaceDescriptor faceDescriptor) {
		this(id);
		if (faceDescriptor != null) {
			setFaceDescriptor(faceDescriptor);
		}
        afterPropertiesSet();
	}

	/**
	 * Constructor providing an id and a number of FaceDescriptors. No default
	 * faceDescriptor is set.
	 *
	 * @param id
	 * @param faceDescriptors a map which contains &lt;faceDescriptorId,
	 * faceDescriptor&gt; pairs.
	 */
	protected AbstractCommand(String id, Map faceDescriptors) {
		this(id);
		setFaceDescriptors(faceDescriptors);
        afterPropertiesSet();
	}

	/**
	 * @return id of this Command.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set the id. In most cases, this is provided by the constructor or through
	 * the beanId provided in the applicationContext.
	 *
	 * @param id
	 */
	protected void setId(String id) {
		if (!StringUtils.hasText(id)) {
			id = null;
		}
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBeanName(String name) {
		if (getId() == null) {
			setId(name);
		}
	}

	/**
	 * Set the default faceDescriptor to use for this command.
	 *
	 * @param faceDescriptor the {@link CommandFaceDescriptor} to use as
	 * default.
	 */
	public void setFaceDescriptor(CommandFaceDescriptor faceDescriptor) {
		setFaceDescriptor(getDefaultFaceDescriptorId(), faceDescriptor);
	}

	/**
	 * Add an additional {@link CommandFaceDescriptor}.
	 *
	 * @param faceDescriptorId key to identify and use this faceDescriptor.
	 * @param faceDescriptor additional {@link CommandFaceDescriptor}.
	 */
	public void setFaceDescriptor(String faceDescriptorId, CommandFaceDescriptor faceDescriptor) {
		getButtonManager(faceDescriptorId).setFaceDescriptor(faceDescriptor);
	}

	/**
	 * Add a number of {@link CommandFaceDescriptor}s to this Command.
	 *
	 * @param faceDescriptors a {@link Map} which contains &lt;faceDescriptorId,
	 * CommandFaceDescriptor&gt; pairs.
	 */
	public void setFaceDescriptors(Map faceDescriptors) {
		Assert.notNull(faceDescriptors);
		Iterator it = faceDescriptors.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String faceDescriptorId = (String) entry.getKey();
			CommandFaceDescriptor faceDescriptor = (CommandFaceDescriptor) entry.getValue();
			setFaceDescriptor(faceDescriptorId, faceDescriptor);
		}
	}

	/**
	 * Change the default FaceDescriptor.
	 *
	 * @param defaultFaceDescriptorId the id of the faceDescriptor to be used as
	 * default.
	 */
	public void setDefaultFaceDescriptorId(String defaultFaceDescriptorId) {
		this.defaultFaceDescriptorId = defaultFaceDescriptorId;
	}

	/**
	 * Set the {@link CommandFaceDescriptorRegistry} to use when
	 * registering/looking up {@link CommandFaceDescriptor}s.
	 *
	 * @param faceDescriptorRegistry registry to use for the
	 * {@link CommandFaceDescriptor}s.
	 */
	public void setFaceDescriptorRegistry(CommandFaceDescriptorRegistry faceDescriptorRegistry) {
		this.faceDescriptorRegistry = faceDescriptorRegistry;
	}

	/**
	 * Set the {@link CommandServices}.
	 */
	public void setCommandServices(CommandServices services) {
		this.commandServices = services;
	}

	/**
	 * Set the provided label on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setButtonLabelInfo(String)
	 */
	public void setLabel(String encodedLabel) {
		getOrCreateFaceDescriptor().setButtonLabelInfo(encodedLabel);
	}

	/**
	 * Set the provided label on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setLabelInfo(CommandButtonLabelInfo)
	 */
	public void setLabel(CommandButtonLabelInfo label) {
		getOrCreateFaceDescriptor().setLabelInfo(label);
	}

	/**
	 * Set the provided description on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setCaption(String)
	 */
	public void setCaption(String shortDescription) {
		getOrCreateFaceDescriptor().setCaption(shortDescription);
	}

	/**
	 * Set the provided icon on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setIcon(Icon)
	 */
	public void setIcon(Icon icon) {
		getOrCreateFaceDescriptor().setIcon(icon);
	}

	/**
	 * Set the provided iconInfo on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setIconInfo(CommandButtonIconInfo)
	 */
	public void setIconInfo(CommandButtonIconInfo iconInfo) {
		getOrCreateFaceDescriptor().setIconInfo(iconInfo);
	}

	/**
	 * Set the provided foreground colour on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setForeground(java.awt.Color)
	 */
	public void setForeground(Color foreground) {
		getOrCreateFaceDescriptor().setForeground(foreground);
	}

	/**
	 * Set the provided background colour on the default {@link CommandFaceDescriptor}.
	 *
	 * @see CommandFaceDescriptor#setBackground(Color)
	 */
	public void setBackground(Color background) {
		getOrCreateFaceDescriptor().setBackground(background);
	}

	/**
	 * Performs initialisation and validation of this instance after its
	 * dependencies have been set. If subclasses override this method, they
	 * should begin by calling {@code super.afterPropertiesSet()}.
	 */
	public void afterPropertiesSet() {
		if (getId() == null) {
			logger.info("Command " + this + " has no set id; note: anonymous commands cannot be used in registries.");
		}
		if (this instanceof ActionCommand && !isFaceConfigured()) {
			logger.info("The face descriptor property is not yet set for action command '" + getId()
					+ "'; configuring");
		}
	}

	/**
	 * Returns the defaultFaceDescriptor. Creates one if needed.
	 */
	private CommandFaceDescriptor getOrCreateFaceDescriptor() {
		if (!isFaceConfigured()) {
			if (logger.isInfoEnabled()) {
				logger.info("Lazily instantiating default face descriptor on behalf of caller to prevent npe; "
						+ "command is being configured manually, right?");
			}
			if(ValkyrieRepository.isCurrentlyRunningInContext()) {
				ValkyrieRepository.getInstance().getApplicationConfig().commandConfigurer().configure(this);
			} else {
				setFaceDescriptor(new CommandFaceDescriptor());
			}
		}
		return getFaceDescriptor();
	}

	/**
	 * Returns the default faceDescriptorId.
	 */
	public String getDefaultFaceDescriptorId() {
		if (!StringUtils.hasText(defaultFaceDescriptorId)) {
			return DEFAULT_FACE_DESCRIPTOR_ID;
		}
		return defaultFaceDescriptorId;
	}

	/**
	 * Returns the default faceDescriptor.
	 */
	protected CommandFaceDescriptor getFaceDescriptor() {
		return getDefaultButtonManager().getFaceDescriptor();
	}

	/**
	 * Returns <code>true</code> if this command has a default faceDescriptor.
	 */
	public boolean isFaceConfigured() {
		return getDefaultButtonManager().isFaceConfigured();
	}

	/**
	 * Returns the icon from the default faceDescriptor or <code>null</code>
	 * if no faceDescriptor is available.
	 */
	public Icon getIcon() {
		if (isFaceConfigured()) {
			return getFaceDescriptor().getIcon();
		}
		return null;
	}

	/**
	 * Returns the text from the default faceDescriptor or the default text of
	 * the {@link CommandButtonLabelInfo#BLANK_BUTTON_LABEL#getText()}.
	 */
	public String getText() {
		if (isFaceConfigured()) {
			return getFaceDescriptor().getText();
		}
		return CommandButtonLabelInfo.BLANK_BUTTON_LABEL.getText();
	}

	/**
	 * Returns the mnemonic from the default faceDescriptor or the default
	 * mnemonic of the
	 * {@link CommandButtonLabelInfo#BLANK_BUTTON_LABEL#getMnemonic()}.
	 */
	public int getMnemonic() {
		if (isFaceConfigured()) {
			return getFaceDescriptor().getMnemonic();
		}
		return CommandButtonLabelInfo.BLANK_BUTTON_LABEL.getMnemonic();
	}

	/**
	 * Returns the mnemonicIndex from the default faceDescriptor or the default
	 * mnemonicIndex of the
	 * {@link CommandButtonLabelInfo#BLANK_BUTTON_LABEL#getMnemonicIndex()}.
	 */
	public int getMnemonicIndex() {
		if (isFaceConfigured()) {
			return getFaceDescriptor().getMnemonicIndex();
		}
		return CommandButtonLabelInfo.BLANK_BUTTON_LABEL.getMnemonicIndex();
	}

	/**
	 * Returns the accelerator from the default faceDescriptor or the default
	 * accelerator of the
	 * {@link CommandButtonLabelInfo#BLANK_BUTTON_LABEL#getAccelerator()}.
	 */
	public KeyStroke getAccelerator() {
		if (isFaceConfigured()) {
			return getFaceDescriptor().getAccelerator();
		}
		return CommandButtonLabelInfo.BLANK_BUTTON_LABEL.getAccelerator();
	}

	/**
	 * Returns the {@link CommandFaceDescriptorRegistry} of this
	 * {@link AbstractCommand} which holds all face descriptors.
	 */
	public CommandFaceDescriptorRegistry getFaceDescriptorRegistry() {
		return faceDescriptorRegistry;
	}

	/**
	 * Returns the {@link CommandServices} for this {@link AbstractCommand}.
	 */
	protected CommandServices getCommandServices() {
		if (commandServices == null) {
			commandServices = ValkyrieRepository.getInstance().getApplicationConfig().commandServices();
		}
		return this.commandServices;
	}

	/**
	 * Set the Id of the security controller that should manage this object.
	 * @param controllerId Id (bean name) of the security controller
	 */
	public void setSecurityControllerId(String controllerId) {
		this.securityControllerId = controllerId;
	}

	/**
	 * Get the id (bean name) of the security controller that should manage this
	 * object.
	 * @return controller id
	 */
	public String getSecurityControllerId() {
		return securityControllerId;
	}

	/**
	 * Set the authorized state. Setting authorized to false will override any
	 * call to {@link #setEnabled(boolean)}. As long as this object is
	 * unauthorized, it can not be enabled.
	 * @param authorized Pass <code>true</code> if the object is to be
	 * authorized
	 */
	public void setAuthorized(boolean authorized) {
		boolean wasAuthorized = isAuthorized();
		if (hasChanged(wasAuthorized, authorized)) {
			this.authorized = authorized;
			firePropertyChange(AUTHORIZED_PROPERTY, wasAuthorized, authorized);
			updatedEnabledState();
		}
	}

	/**
	 * Returns <code>true</code> if the command is authorized.
	 */
	public boolean isAuthorized() {
		return authorized;
	}

	/**
	 * Returns <code>true</code> if the command is enabled and
	 * {@link #isAuthorized()}.
	 *
	 * @see #isAuthorized()
	 */
	public boolean isEnabled() {
		return enabled && isAuthorized();
	}

	/**
	 * This method is called when any predicate for enabled state has changed.
	 * This implementation fires the enabled changed event if the return value
	 * of {@link #isEnabled()} has changed.
	 * <p>
	 * Subclasses which have an additional predicate to enabled state must call
	 * this method if the state of the predicate changes.
	 */
	protected void updatedEnabledState() {
		boolean isEnabled = isEnabled();
		if (oldEnabledState == null || hasChanged(oldEnabledState.booleanValue(), isEnabled)) {
			firePropertyChange(ENABLED_PROPERTY_NAME, oldEnabledState == null ? !isEnabled : oldEnabledState
					.booleanValue(), isEnabled);
		}
		oldEnabledState = Boolean.valueOf(isEnabled);
	}

	/**
	 * Set the enabled state of this command. Note that if we are currently not
	 * authorized, then the new value will just be recorded and no change in the
	 * current enabled state will be made.
	 * @param enabled state
	 */
	public void setEnabled(boolean enabled) {
		if (hasChanged(this.enabled, enabled)) {
			this.enabled = enabled;
			updatedEnabledState();
		}
	}

	/**
	 * Listener to keep track of enabled state. When enable on command changes,
	 * each button has to be checked and set.
	 */
	private class ButtonEnablingListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			// We need to keep the buttons in sync with the command, so go
			// through the buttons and set Enabled state.
			// alternative is to add a listener to the enabled value and change
			// buttons in that listener
			// NOT redundant
			boolean enabled = evt.getNewValue() == Boolean.TRUE;
			Iterator it = buttonIterator();
			while (it.hasNext()) {
				AbstractButton button = (AbstractButton) it.next();
				button.setEnabled(enabled);
			}
		}
	}

	/**
	 * Listener to keep track of visible state. When visible on command changes,
	 * each button has to be checked and set.
	 */
	private class ButtonVisibleListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			// We need to keep the buttons in sync with the command, so go
			// through the buttons and set visible state.
			// alternative is to add a listener to the visible value and change
			// buttons in that listener
			// NOT redundant
			boolean enabled = evt.getNewValue() == Boolean.TRUE;
			Iterator it = buttonIterator();
			while (it.hasNext()) {
				AbstractButton button = (AbstractButton) it.next();
				button.setVisible(enabled);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addEnabledListener(PropertyChangeListener listener) {
		addPropertyChangeListener(ENABLED_PROPERTY_NAME, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeEnabledListener(PropertyChangeListener listener) {
		removePropertyChangeListener(ENABLED_PROPERTY_NAME, listener);
	}

	/**
	 * <p>
	 * Returns an iterator over all buttons in the default
	 * {@link CommandFaceButtonManager}.
	 * </p>
	 * <p>
	 * To traverse all buttons of all {@link CommandFaceButtonManager}s see
	 * {@link #buttonIterator()}.
	 * </p>
	 */
	protected final Iterator defaultButtonIterator() {
		return getDefaultButtonManager().iterator();
	}

	/**
	 * Returns an iterator over <em>all</em> buttons by traversing
	 * <em>each</em> {@link CommandFaceButtonManager}.
	 */
	protected final Iterator buttonIterator() {

		if (this.faceButtonManagers == null)
			return Collections.EMPTY_SET.iterator();

		return new NestedButtonIterator(this.faceButtonManagers.values().iterator());
	}

	/**
	 * Iterator to traverse all buttons in every
	 * {@link CommandFaceButtonManager} of this {@link AbstractCommand}.
	 */
	private static final class NestedButtonIterator implements Iterator {
		private final Iterator managerIterator;

		private Iterator currentButtonIterator;

		private AbstractButton nextButton;

		NestedButtonIterator(Iterator it) {
			this.managerIterator = it;
			preFetchNextButton();
		}

		public boolean hasNext() {
			return nextButton != null;
		}

		public Object next() {
			if (nextButton == null) {
				throw new NoSuchElementException();
			}
			AbstractButton lastButton = nextButton;
			preFetchNextButton();
			return lastButton;
		}

		public void remove() {
			throw new UnsupportedOperationException("Can't use a button-iterator on AbstractCommand to remove buttons.");
		}

		private void preFetchNextButton() {
			while (this.currentButtonIterator == null || !this.currentButtonIterator.hasNext()) {
				if (this.managerIterator.hasNext()) {
					CommandFaceButtonManager cfbm = (CommandFaceButtonManager) this.managerIterator.next();
					this.currentButtonIterator = cfbm.iterator();
				}
				else {
					this.currentButtonIterator = null;
					this.nextButton = null;
					return;
				}
			}

			if (this.currentButtonIterator.hasNext())
				nextButton = (AbstractButton) this.currentButtonIterator.next();
			else
				nextButton = null;
		}
	}

	/**
	 * Returns <code>true</code> if this command doesn't have an Id.
	 */
	public boolean isAnonymous() {
		return id == null;
	}

	/**
	 * Returns <code>true</code> if the command is visible.
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Set this command visible and update all associated buttons.
	 */
	public void setVisible(boolean value) {
		if (visible != value) {
			this.visible = value;
			updatedVisibleState();
		}
	}

	/**
	 * <p>
	 * This method is called when any predicate for visible state has changed.
	 * This implementation fires the visible changed event if the return value
	 * of {@link #isVisible()} has changed.
	 * </p>
	 * <p>
	 * Subclasses which have an additional predicate to visible state must call
	 * this method if the state of the predicate changes.
	 * </p>
	 */
	protected void updatedVisibleState() {
		boolean isVisible = isVisible();
		if (oldVisibleState == null || hasChanged(oldVisibleState.booleanValue(), isVisible)) {
			firePropertyChange(VISIBLE_PROPERTY_NAME, oldVisibleState == null ? !isVisible : oldVisibleState
					.booleanValue(), isVisible);
		}
		oldVisibleState = Boolean.valueOf(isVisible);
	}

	/**
	 * Create a button using the defaults for faceDescriptorId, buttonFactory
	 * and buttonConfigurer.
	 *
	 * @see #createButton(String, ButtonFactory, CommandButtonConfigurer)
	 */
	public final AbstractButton createButton() {
		return createButton(getDefaultFaceDescriptorId(), getButtonFactory(), getDefaultButtonConfigurer());
	}

	/**
	 * Create a button using the defaults for buttonFactory and
	 * buttonConfigurer.
	 *
	 * @see #createButton(String, ButtonFactory, CommandButtonConfigurer)
	 */
	public final AbstractButton createButton(String faceDescriptorId) {
		return createButton(faceDescriptorId, getButtonFactory(), getDefaultButtonConfigurer());
	}

	/**
	 * Create a button using the defaults for faceDescriptorId and
	 * buttonConfigurer.
	 *
	 * @see #createButton(String, ButtonFactory, CommandButtonConfigurer)
	 */
	public final AbstractButton createButton(ButtonFactory buttonFactory) {
		return createButton(getDefaultFaceDescriptorId(), buttonFactory, getDefaultButtonConfigurer());
	}

	/**
	 * Create a button using the default buttonConfigurer.
	 *
	 * @see #createButton(String, ButtonFactory, CommandButtonConfigurer)
	 */
	public final AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory) {
		return createButton(faceDescriptorId, buttonFactory, getDefaultButtonConfigurer());
	}

	/**
	 * Create a button using the default buttonFactory.
	 *
	 * @see #createButton(String, ButtonFactory, CommandButtonConfigurer)
	 */
	public final AbstractButton createButton(ButtonFactory buttonFactory, CommandButtonConfigurer buttonConfigurer) {
		return createButton(getDefaultFaceDescriptorId(), buttonFactory, buttonConfigurer);
	}

	/**
	 * Creates a button using the provided id, factory and configurer.
	 *
	 * @param faceDescriptorId id of the faceDescriptor used to configure the
	 * button.
	 * @param buttonFactory factory that delivers the button.
	 * @param buttonConfigurer configurer mapping the faceDescriptor on the
	 * button.
	 * @return a button attached to this command.
	 */
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {
		AbstractButton button = buttonFactory.createButton();
		attach(button, faceDescriptorId, buttonConfigurer);
		return button;
	}

	/**
	 * Create a menuItem using the defaults for faceDescriptorId, menuFactory
	 * and menuItemButtonConfigurer.
	 *
	 * @see #createMenuItem(String, MenuFactory, CommandButtonConfigurer)
	 */
	public final JMenuItem createMenuItem() {
		return createMenuItem(getDefaultFaceDescriptorId(), getMenuFactory(), getMenuItemButtonConfigurer());
	}

	/**
	 * Create a menuItem using the defaults for menuFactory and
	 * menuItemButtonConfigurer.
	 *
	 * @see #createMenuItem(String, MenuFactory, CommandButtonConfigurer)
	 */
	public final JMenuItem createMenuItem(String faceDescriptorId) {
		return createMenuItem(faceDescriptorId, getMenuFactory(), getMenuItemButtonConfigurer());
	}

	/**
	 * Create a menuItem using the defaults for faceDescriptorId and
	 * menuItemButtonConfigurer.
	 *
	 * @see #createMenuItem(String, MenuFactory, CommandButtonConfigurer)
	 */
	public final JMenuItem createMenuItem(MenuFactory menuFactory) {
		return createMenuItem(getDefaultFaceDescriptorId(), menuFactory, getMenuItemButtonConfigurer());
	}

	/**
	 * Create a menuItem using the default and menuItemButtonConfigurer.
	 *
	 * @see #createMenuItem(String, MenuFactory, CommandButtonConfigurer)
	 */
	public final JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory) {
		return createMenuItem(faceDescriptorId, menuFactory, getMenuItemButtonConfigurer());
	}

	/**
	 * Create a menuItem using the default faceDescriptorId.
	 *
	 * @see #createMenuItem(String, MenuFactory, CommandButtonConfigurer)
	 */
	public final JMenuItem createMenuItem(MenuFactory menuFactory, CommandButtonConfigurer buttonConfigurer) {
		return createMenuItem(getDefaultFaceDescriptorId(), menuFactory, buttonConfigurer);
	}

	/**
	 * Create a menuItem using the provided id, factory and configurer.
	 *
	 * @param faceDescriptorId id of the faceDescriptor used to configure the
	 * button.
	 * @param menuFactory factory that delivers the menuItem.
	 * @param buttonConfigurer configurer mapping the faceDescriptor on the
	 * button.
	 * @return a menuItem attached to this command.
	 */
	public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
			CommandButtonConfigurer buttonConfigurer) {
		JMenuItem menuItem = menuFactory.createMenuItem();
		attach(menuItem, faceDescriptorId, buttonConfigurer);
		return menuItem;
	}

	/**
	 * Attach and configure the button to the default faceDescriptor using the
	 * default configurer.
	 *
	 * @see #attach(AbstractButton, String, CommandButtonConfigurer)
	 */
	public void attach(AbstractButton button) {
		attach(button, getDefaultFaceDescriptorId(), getCommandServices().getDefaultButtonConfigurer());
	}

	/**
	 * Attach and configure the button to the default faceDescriptor using the
	 * given configurer.
	 *
	 * @see #attach(AbstractButton, String, CommandButtonConfigurer)
	 */
	public void attach(AbstractButton button, CommandButtonConfigurer configurer) {
		attach(button, getDefaultFaceDescriptorId(), configurer);
	}

	/**
	 * Attach and configure the button to the faceDescriptorId using the configurer.
	 *
	 * @param button the button to attach and configure.
	 * @param faceDescriptorId the id of the faceDescriptor.
	 * @param configurer that maps the faceDescriptor on the button.
	 */
	public void attach(AbstractButton button, String faceDescriptorId, CommandButtonConfigurer configurer) {
		getButtonManager(faceDescriptorId).attachAndConfigure(button, configurer);
		onButtonAttached(button);
	}

	/**
	 * Additional code to execute when attaching a button.
	 *
	 * @param button the button that has been attached.
	 */
	protected void onButtonAttached(AbstractButton button) {
		if (logger.isDebugEnabled()) {
			logger.debug("Configuring newly attached button for command '" + getId() + "' enabled=" + isEnabled()
					+ ", visible=" + isVisible());
		}
		button.setEnabled(isEnabled());
		button.setVisible(isVisible());
	}

	/**
	 * Detach the button from the {@link CommandFaceButtonManager}.
	 *
	 * @param button the button to detach.
	 */
	public void detach(AbstractButton button) {
		if (getDefaultButtonManager().isAttachedTo(button)) {
			getDefaultButtonManager().detach(button);
			onButtonDetached();
		}
	}

	/**
	 * Returns <code>true</code> if the provided button is attached to the
	 * default {@link CommandFaceButtonManager}.
	 *
	 * @param b the button to check.
	 * @return <code>true</code> if b is attached to the default
	 * {@link CommandFaceButtonManager}.
	 */
	public boolean isAttached(AbstractButton b) {
		return getDefaultButtonManager().isAttachedTo(b);
	}

	/**
	 * Implement this to add custom code executed when detaching a button.
	 */
	protected void onButtonDetached() {
		// default no implementation, subclasses may override
	}

	/**
	 * Returns the {@link CommandFaceButtonManager} for the default
	 * {@link CommandFaceDescriptor}.
	 */
	private CommandFaceButtonManager getDefaultButtonManager() {
		return getButtonManager(getDefaultFaceDescriptorId());
	}

	/**
	 * Returns the {@link CommandFaceButtonManager} for the given
	 * faceDescriptorId.
	 *
	 * @param faceDescriptorId id of the {@link CommandFaceDescriptor}.
	 * @return the {@link CommandFaceButtonManager} managing buttons configured
	 * with the {@link CommandFaceDescriptor}.
	 */
	private CommandFaceButtonManager getButtonManager(String faceDescriptorId) {
		if (this.faceButtonManagers == null) {
			this.faceButtonManagers = new AbstractCachingMapDecorator() {
				protected Object create(Object key) {
					return new CommandFaceButtonManager(AbstractCommand.this, (String) key);
				}
			};
		}
		CommandFaceButtonManager m = (CommandFaceButtonManager) this.faceButtonManagers.get(faceDescriptorId);
		return m;
	}

	/**
	 * @see CommandServices#getDefaultButtonConfigurer()
	 */
	protected CommandButtonConfigurer getDefaultButtonConfigurer() {
		return getCommandServices().getDefaultButtonConfigurer();
	}

	/**
	 * @see CommandServices#getToolBarButtonConfigurer()
	 */
	protected CommandButtonConfigurer getToolBarButtonConfigurer() {
		return getCommandServices().getToolBarButtonConfigurer();
	}

	/**
	 * @see CommandServices#getToolBarButtonFactory()
	 */
	protected ButtonFactory getToolBarButtonFactory() {
		return getCommandServices().getToolBarButtonFactory();
	}

	/**
	 * @see CommandServices#getMenuItemButtonConfigurer()
	 */
	protected CommandButtonConfigurer getMenuItemButtonConfigurer() {
		return getCommandServices().getMenuItemButtonConfigurer();
	}

	/**
	 * @see CommandServices#getComponentFactory()
	 */
	protected ComponentFactory getComponentFactory() {
		return getCommandServices().getComponentFactory();
	}

	/**
	 * @see CommandServices#getButtonFactory()
	 */
	protected ButtonFactory getButtonFactory() {
		return getCommandServices().getButtonFactory();
	}

	/**
	 * @see CommandServices#getMenuFactory()
	 */
	protected MenuFactory getMenuFactory() {
		return getCommandServices().getMenuFactory();
	}

	/**
	 * Search for a button representing this command in the provided container
	 * and let it request the focus.
	 *
	 * @param container the container which holds the command button.
	 * @return <code>true</code> if the focus request is likely to succeed.
	 *
	 * @see #getButtonIn(Container)
	 * @see JComponent#requestFocusInWindow()
	 */
	public boolean requestFocusIn(Container container) {
		AbstractButton button = getButtonIn(container);
		if (button != null) {
			return button.requestFocusInWindow();
		}
		return false;
	}

	/**
	 * Search for the first button of this command that is a child component of
	 * the given container.
	 *
	 * @param container the container to be searched.
	 * @return the {@link AbstractButton} representing this command that is
	 * embedded in the container or <code>null</code> if none was found.
	 */
	public AbstractButton getButtonIn(Container container) {
		Iterator it = buttonIterator();
		while (it.hasNext()) {
			AbstractButton button = (AbstractButton) it.next();
			if (SwingUtilities.isDescendingFrom(button, container)) {
				return button;
			}
		}
		return null;
	}

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String... authorities) {
        this.authorities = authorities;
    }

    /**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringCreator(this).append("id", getId()).append("enabled", enabled).append("visible", visible)
				.append("defaultFaceDescriptorId", defaultFaceDescriptorId).toString();
	}

}