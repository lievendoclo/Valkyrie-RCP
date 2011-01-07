package org.valkyriercp.command.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.PropertyNotSetException;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.core.Secured;

import java.awt.*;

/**
 * A {@link org.springframework.beans.factory.FactoryBean} that produces a {@link CommandGroup}.
 *
 * <p>
 * Use of this bean simplifies the process of building up complex nested command
 * groups such as the main menu of an application window, a toolbar or popup
 * menus. The main property of interest when creating a bean definition for this
 * class is the {@code members} list. This list defines the members of the
 * command group that will be produced by the factory. The objects contained in
 * this list can be instances of the actual command or they can be strings that
 * represent the identifier of the command. Some strings have special meaning:
 *
 * <ul>
 * <li>{@value #GLUE_MEMBER_CODE}: Represents a 'glue' component between
 * command group members.</li>
 * <li>{@value #SEPARATOR_MEMBER_CODE}: Represents a separator between command
 * group members.</li>
 * </ul>
 * </p>
 *
 * @author Keith Donald
 */
@Configurable
public class CommandGroupFactoryBean implements BeanNameAware, FactoryBean, Secured {

	/**
	 * The string that represents a glue component, to be used between other
	 * members of the command group.
	 */
	public static final String GLUE_MEMBER_CODE = "glue";

	/**
	 * The string that represents a separator between commands in the command
	 * group.
	 */
	public static final String SEPARATOR_MEMBER_CODE = "separator";

	/**
	 * The string prefix that indicates a command group member that is a
	 * command.
	 */
	public static final String COMMAND_MEMBER_PREFIX = "command:";

	/**
	 * The string prefix that indicates a command group member that is another
	 * command group.
	 */
	public static final String GROUP_MEMBER_PREFIX = "group:";

	/** Class logger, available to subclasses. */
	protected Log logger = LogFactory.getLog(getClass());

	private String groupId;

    @Autowired
	private CommandRegistry commandRegistry;

    @Autowired
	private CommandConfigurer commandConfigurer;

	private Object[] members;

	private boolean exclusive;

	private boolean allowsEmptySelection;

	private CommandGroup commandGroup;

	private String securityControllerId;

	/**
	 * Creates a new uninitialized {@code CommandGroupFactoryBean}. If created
	 * by the Spring IoC container, the {@code groupId} assigned to this
	 * instance will be the bean name of the bean as declared in the bean
	 * definition file. If using this constructor, a non-null list of command
	 * group members must be provided by calling the
	 * {@link #setMembers(Object[])} method before this instance is used.
	 */
	public CommandGroupFactoryBean() {
		// do nothing
	}

	/**
	 * Creates a new {@code CommandGroupFactoryBean} with the given group ID and
	 * command group members.
	 *
	 * @param groupId The identifier that will be assigned to the command group
	 * produced by this factory. Note that if this instance is created by a
	 * Spring IoC container, the group ID provided here will be overwritten by
	 * the bean name of this instance's bean definition.
	 *
	 * @param members The collection of objects that specify the members of the
	 * command group. These objects are expected to be either instances of
	 * {@link AbstractCommand} or strings. See the class documentation for
	 * details on how these strings will be interpreted. Must not be null.
	 *
	 * @throws IllegalArgumentException if {@code members} is null.
	 */
	public CommandGroupFactoryBean(String groupId, Object... members) {
		this(groupId, null, null, members);
	}

	/**
	 * Creates a new {@code CommandGroupFactoryBean}.
	 *
	 * @param groupId The value to be used as the command identifier of the
	 * command group produced by this factory.
	 * @param commandRegistry The registry that will be used to retrieve the
	 * actual instances of the command group members as specified in
	 * {@code members}.
	 * @param members The collection of objects that specify the members of the
	 * command group. These objects are expected to be either instances of
	 * {@link AbstractCommand} or strings. See the class documentation for
	 * details on how these strings will be interpreted. Must not be null.
	 *
	 * @throws IllegalArgumentException if {@code members} is null.
	 */
	public CommandGroupFactoryBean(String groupId, CommandRegistry commandRegistry, Object... members) {
		this(groupId, commandRegistry, null, members);
	}

	/**
	 * Creates a new {@code CommandGroupFactoryBean}.
	 *
	 * @param groupId The value to be used as the command identifier of the
	 * command group produced by this factory.
	 * @param commandRegistry The registry that will be used to retrieve the
	 * actual instances of the command group members as specified in
	 * {@code members}.
	 * @param members The collection of objects that specify the members of the
	 * command group. These objects are expected to be either instances of
	 * {@link AbstractCommand} or strings. See the class documentation for
	 * details on how these strings will be interpreted. Must not be null.
	 * @param commandConfigurer The object that will be used to configure the
	 * command objects contained in this factory's command group.
	 *
	 * @throws IllegalArgumentException if {@code members} is null.
	 */
	public CommandGroupFactoryBean(String groupId, CommandRegistry commandRegistry,
			CommandConfigurer commandConfigurer, Object... members) {

		Assert.notNull(members, "members");

		this.groupId = groupId;
		this.commandRegistry = commandRegistry;
		this.members = members;
		this.commandConfigurer = commandConfigurer;

	}

	/**
	 * Sets the registry that will be used to retrieve the actual instances of
	 * the command group members as specified in the encoded members collection.
	 *
	 * @param commandRegistry The registry containing commands for the command
	 * group produced by this factory. May be null.
	 */
	public void setCommandRegistry(CommandRegistry commandRegistry) {
		this.commandRegistry = commandRegistry;
	}

	/**
	 * @return commandRegistry containing commands for this command group.
	 */
	protected CommandRegistry getCommandRegistry() {
		return this.commandRegistry;
	}

	/**
	 * Sets the object that will be used to configure the command objects in the
	 * command groups produced by this factory.
	 *
	 * @param configurer The command configurer, may be null.
	 */
	public void setCommandConfigurer(CommandConfigurer configurer) {
		this.commandConfigurer = configurer;
	}

	/**
	 * Sets the collection of objects that specify the members of the command
	 * group produced by this factory. The objects in {@code members} are
	 * expected to be either instances of {@link AbstractCommand} or strings.
	 * See the class documentation for details on how these strings will be
	 * interpreted.
	 *
	 * @param members The (possibly) encoded representation of the command group
	 * members. Must not be null.
	 *
	 * @throws IllegalArgumentException if {@code members} is null.
	 */
	public final void setMembers(Object... members) {
		Assert.notNull(members, "members");
		this.members = members;
	}

	/**
	 * @return the possibly encoded representation of the command group members.
	 */
	protected Object[] getMembers() {
		return this.members;
	}

	/**
	 * Accepts notification from the IoC container of this instance's bean name
	 * as declared in the bean definition file. This value is used as the id of
	 * the command group produced by this factory.
	 */
	public void setBeanName(String beanName) {
		this.groupId = beanName;
	}

	/**
	 * @return beanName.
	 */
	protected String getBeanName() {
		return this.groupId;
	}

	/**
	 * Returns the value of the flag that indicates whether or not this factory
	 * produces an exclusive command group.
	 * @return The exclusive flag.
	 * @see ExclusiveCommandGroup
	 */
	public boolean isExclusive() {
		return this.exclusive;
	}

	/**
	 * Sets the flag that indicates whether or not this factory produces an
	 * exclusive command group.
	 *
	 * @param exclusive {@code true} to produce an exclusive command group,
	 * false otherwise.
	 * @see ExclusiveCommandGroup
	 */
	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	/**
	 * Sets the flag that indicates whether or not the command group produced by
	 * this factory allows no items in the group to be selected, default is
	 * false. This is only relevant for exclusive command groups.
	 *
	 * @param allowsEmptySelection Set {@code true} for the command group to
	 * allow none of its members to be selected.
	 */
	public void setAllowsEmptySelection(boolean allowsEmptySelection) {
		this.allowsEmptySelection = allowsEmptySelection;
	}

	/**
	 * @return <code>true</code> if the exclusive commandGroup can have no
	 * item selected.
	 */
	protected boolean isAllowsEmptySelection() {
		return this.allowsEmptySelection;
	}

	/**
	 * Returns the command group that this factory produces.
	 *
	 * @return The factory's command group, never null.
	 */
	public Object getObject() throws Exception {
		return getCommandGroup();
	}

	/**
	 * Returns the command group that this factory produces.
	 *
	 * @return The factory's command group, never null.
	 */
	public CommandGroup getCommandGroup() {
		if (commandGroup == null) {
			commandGroup = createCommandGroup();
		}
//        commandConfigurer.configure(commandGroup);
		return commandGroup;
	}

	/**
	 * Creates the command group for this factory and assigns it an identifier
	 * equal to the group id of the factory. The command group will also be
	 * assigned the security controller id, if any, that was provided via the
	 * {@link #setSecurityControllerId(String)} method and the values from the
	 * encoded members list will be used to retrieve the corresponding command
	 * objects from the command registry.
	 *
	 * @return The command group, never null.
	 */
	// NOTE: Find out (and add some comment about) what happens if a command
	// registry has not been provided.
	protected CommandGroup createCommandGroup() {
		CommandGroup group;
		if (isExclusive()) {
			ExclusiveCommandGroup g = new ExclusiveCommandGroup(getBeanName(), getCommandRegistry());
			g.setAllowsEmptySelection(isAllowsEmptySelection());
			group = g;
		}
		else {
			group = new CommandGroup(getBeanName(), getCommandRegistry());
		}

		// Apply our security controller id to the new group
		group.setSecurityControllerId(getSecurityControllerId());

		initCommandGroupMembers(group);
		return group;
	}

	/**
	 * Iterates over the collection of encoded members and adds them to the
	 * given command group.
	 *
	 * @param group The group that is to contain the commands from the encoded
	 * members list. Must not be null.
	 *
	 * @throws InvalidGroupMemberEncodingException if a member prefix is
	 * provided without a command id.
	 */
	protected void initCommandGroupMembers(CommandGroup group) {
		for (int i = 0; i < members.length; i++) {
			Object o = members[i];

			if (o instanceof AbstractCommand) {
				group.addInternal((AbstractCommand) o);
				configureIfNecessary((AbstractCommand) o);
			}
			else if (o instanceof Component) {
				group.addComponentInternal((Component) o);
			}
			else if (o instanceof String) {
				String str = (String) o;
				if (str.equalsIgnoreCase(SEPARATOR_MEMBER_CODE)) {
					group.addSeparatorInternal();
				}
				else if (str.equalsIgnoreCase(GLUE_MEMBER_CODE)) {
					group.addGlueInternal();
				}
				else if (str.startsWith(COMMAND_MEMBER_PREFIX)) {

					String commandId = str.substring(COMMAND_MEMBER_PREFIX.length());

					if (!StringUtils.hasText(commandId)) {
						throw new InvalidGroupMemberEncodingException(
								"The group member encoding does not specify a command id", str);
					}

					addCommandMember(str.substring(COMMAND_MEMBER_PREFIX.length()), group);
				}
				else if (str.startsWith(GROUP_MEMBER_PREFIX)) {

					String commandId = str.substring(GROUP_MEMBER_PREFIX.length());

					if (!StringUtils.hasText(commandId)) {
						throw new InvalidGroupMemberEncodingException(
								"The group member encoding does not specify a command id", str);
					}

					addCommandMember(commandId, group);
				}
				else {
					addCommandMember(str, group);
				}
			}
		}
	}

	/**
	 * Adds the command object with the given id to the given command group. If
	 * a command registry has not yet been provided to this factory, the command
	 * id will be passed as a 'lazy placeholder' to the group instead.
	 *
	 * @param commandId The id of the command to be added to the group. This is
	 * expected to be in decoded form, i.e. any command prefixes have been
	 * removed. Must not be null.
	 * @param group The group that the commands will be added to. Must not be
	 * null.
	 *
	 */
	private void addCommandMember(String commandId, CommandGroup group) {

		Assert.notNull(commandId, "commandId");
		Assert.notNull(group, "group");

		if (logger.isDebugEnabled()) {
			logger.debug("adding command group member with id [" + commandId + "] to group [" + group.getId() + "]");
		}

		AbstractCommand command = null;

		if (commandRegistry != null) {
			command = (AbstractCommand) commandRegistry.getCommand(commandId);
			if (command != null) {
				group.addInternal(command);
			}
		}

		if (command == null) {
			group.addLazyInternal(commandId);
		}

	}

	/**
	 * Configures the given command if it has not already been configured and
	 * this instance has been provided with a {@link CommandConfigurer}.
	 *
	 * @param command The command to be configured.
	 * @throws IllegalArgumentException if {@code command} is null.
	 */
	protected void configureIfNecessary(AbstractCommand command) {

		Assert.notNull(command, "command");

		if (commandConfigurer != null) {
			if (!command.isFaceConfigured()) {
				commandConfigurer.configure(command);
			}
		}

	}

	/**
	 * Returns the Class object for {@link CommandGroup}.
	 * @return CommandGroup.class
	 */
	public Class getObjectType() {
		return CommandGroup.class;
	}

	/**
	 * Always returns true. The command groups produced by this factory are
	 * always singletons.
	 * @return {@code true} always.
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSecurityControllerId(String controllerId) {
		this.securityControllerId = controllerId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSecurityControllerId() {
		return securityControllerId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAuthorized(boolean authorized) {
		// nothing to do on the factory. This method is only implemented because
		// it is declared on the SecurityControllable interface, which we need
		// to implement in order to be assigned a securityControllerId that we
		// can then pass on to the commandGroup produced by this factory
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAuthorized() {
		return false;
	}

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
