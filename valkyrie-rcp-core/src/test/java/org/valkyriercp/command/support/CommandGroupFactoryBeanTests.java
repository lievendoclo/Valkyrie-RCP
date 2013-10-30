package org.valkyriercp.command.support;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.application.PropertyNotSetException;
import org.valkyriercp.command.CommandConfigurer;

/**
 * Provides a suite of unit tests for the {@link CommandGroupFactoryBean} class.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class CommandGroupFactoryBeanTests extends AbstractValkyrieTest {

    private AbstractCommand noOpCommand = new AbstractCommand() {
        public void execute() {
            //do nothing
        }

        /**
         * {@inheritDoc}
         */
        public String getId() {
            return "noOpCommand";
        }

    };

    private ToggleCommand toggleCommand = new ToggleCommand() {

        /**
         * {@inheritDoc}
         */
        public String getId() {
            return "toggleCommand";
        }

    };

    /**
     * Creates a new uninitialized {@code CommandGroupFactoryBeanTests}.
     */
    public CommandGroupFactoryBeanTests() {
        super();
    }

    /**
     * Tests the constructor that takes the group id and members array.
     * @throws Exception
     */
    @Test
    public final void testConstructorTakingGroupIdAndMembersArray() throws Exception {

        String groupId = "groupId";
        Object[] members = null;

        members = new Object[] {noOpCommand};

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean(groupId, members);
        CommandGroup commandGroup = (CommandGroup) factoryBean.getObject();
        Assert.assertEquals(groupId, commandGroup.getId());
        Assert.assertEquals(1, commandGroup.size());

    }

    /**
     * Test method for {@link CommandGroupFactoryBean#setMembers(Object...)}.
     */
    @Test
    public final void testSetMembers() {

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean();

        try {
            factoryBean.setMembers(null);
            Assert.fail("Should have thrown an IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            //test passes
        }

        factoryBean.setMembers(new Object[] {});

    }

    /**
     * Confirms that an exception is thrown if the 'group:' prefix appears in the members list
     * with no following command name.
     */
    @Test
    public void testInvalidGroupPrefix() {

        Object[] members = new Object[] {CommandGroupFactoryBean.GROUP_MEMBER_PREFIX};

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean();
        factoryBean.setMembers(members);

        try {
            factoryBean.getCommandGroup();
            Assert.fail("Should have thrown an InvalidGroupMemberEncodingException");
        }
        catch (InvalidGroupMemberEncodingException e) {
            Assert.assertEquals(CommandGroupFactoryBean.GROUP_MEMBER_PREFIX, e.getEncodedString());
        }

    }

    /**
     * Confirms that an exception is thrown if the 'command:' prefix appears in the members list
     * with no following command name.
     */
    @Test
    public void testInvalidCommandPrefix() {

        Object[] members = new Object[] {CommandGroupFactoryBean.COMMAND_MEMBER_PREFIX};

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean();
        factoryBean.setMembers(members);

        try {
            factoryBean.getCommandGroup();
            Assert.fail("Should have thrown an InvalidGroupMemberEncodingException");
        }
        catch (InvalidGroupMemberEncodingException e) {
            Assert.assertEquals(CommandGroupFactoryBean.COMMAND_MEMBER_PREFIX, e.getEncodedString());
        }

    }

    /**
     * Test method for {@link CommandGroupFactoryBean#createCommandGroup()}.
     * @throws Exception
     */
    @Test
    public final void testCreateCommandGroup() throws Exception {

        String groupId = "bogusGroupId";
        String securityId = "bogusSecurityId";
        Object[] members = new Object[] {toggleCommand};

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean(groupId, members);
        factoryBean.setSecurityControllerId(securityId);
        CommandGroup commandGroup = (CommandGroup) factoryBean.getObject();
        Assert.assertEquals(securityId , commandGroup.getSecurityControllerId());
        Assert.assertFalse("Assert command group not exclusive", commandGroup instanceof ExclusiveCommandGroup);
        Assert.assertEquals(1, commandGroup.size());

        factoryBean = new CommandGroupFactoryBean(groupId, members);
        factoryBean.setExclusive(true);
        factoryBean.setAllowsEmptySelection(true);
        commandGroup = (CommandGroup) factoryBean.getObject();
        Assert.assertTrue("Assert command group is exclusive", commandGroup instanceof ExclusiveCommandGroup);
        Assert.assertTrue("Assert allows empty selection is true",
                          ((ExclusiveCommandGroup) commandGroup).getAllowsEmptySelection());


    }

    /**
     * Test method for {@link CommandGroupFactoryBean#getObjectType()}.
     */
    @Test
    public final void testGetObjectType() {
        Assert.assertEquals(CommandGroup.class, new CommandGroupFactoryBean().getObjectType());
    }

    /**
     * Confirms that the command group is assigned the security controller id of the factory bean.
     * @throws Exception
     */
    @Test
    public final void testSecurityControllerIdIsApplied() throws Exception {

        String groupId = "bogusGroupId";
        String securityId = "bogusSecurityId";
        Object[] members = new Object[] {noOpCommand};

        CommandGroupFactoryBean factoryBean = new CommandGroupFactoryBean(groupId, members);
        factoryBean.setSecurityControllerId(securityId);
        CommandGroup commandGroup = (CommandGroup) factoryBean.getObject();
        Assert.assertEquals(securityId , commandGroup.getSecurityControllerId());

    }

}

