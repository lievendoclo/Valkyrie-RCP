package org.valkyriercp.command.support;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;

/**
 * Testcase for ActionCommand
 *
 * @author Peter De Bruycker
 */
public class ActionCommandTests extends AbstractCommandTests {

	@Test
    public void testOnButtonAttached() {
		final boolean[] executed = { false };

		ActionCommand command = new ActionCommand() {
			protected void doExecuteCommand() {
				executed[0] = true;
			}
		};
		command.setActionCommand("theActionCommand");

		JButton button = new JButton("test");

		command.onButtonAttached(button);

		Assert.assertEquals("theActionCommand", button.getActionCommand());

		button.doClick();
		Assert.assertTrue(executed[0]);
	}

	@Test
    public void testOnButtonAttachedWithDisplayDialog() {
		ActionCommand command = new ActionCommand() {
			protected void doExecuteCommand() {
				// do nothing
			}
		};
		command.setDisplaysInputDialog(true);

		JButton button = new JButton();
		button.setText(null);

		command.onButtonAttached(button);

		Assert.assertEquals(null, button.getText());
	}

}

