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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Mathias Broekelmann
 *
 */
public abstract class AbstractCommandTests {

    private TestListener testListener;

    @Before
    public void setUp() throws Exception {
        testListener = new TestListener();
    }

    @Test
    public void testEnabledState() throws Exception {
        TestAbstractCommand command = new TestAbstractCommand();
        command.addEnabledListener(testListener);
        // test initial value
        command.secondaryEnabledState = false;
        Assert.assertFalse(command.isEnabled());
        command.setSecondaryEnabledState(false);
        Assert.assertEquals(0, testListener.eventCount);
        Assert.assertFalse(command.isEnabled());

        // test state change
        testListener.eventCount = 0;
        command.setSecondaryEnabledState(true);
        Assert.assertEquals(1, testListener.eventCount);
        Assert.assertTrue(command.isEnabled());

        // test initial value
        command = new TestAbstractCommand();
        command.addEnabledListener(testListener);
        testListener.eventCount = 0;
        command.secondaryEnabledState = true;
        Assert.assertTrue(command.isEnabled());
        command.setSecondaryEnabledState(false);
        Assert.assertEquals(1, testListener.eventCount);
        Assert.assertFalse(command.isEnabled());
    }

    @Test
    public void testVisibleState() throws Exception {
        TestAbstractCommand command = new TestAbstractCommand();
        command.addPropertyChangeListener("visible", testListener);
        // test initial value
        command.secondaryVisibleState = false;
        command.setSecondaryVisibleState(false);
        Assert.assertEquals(0, testListener.eventCount);
        Assert.assertFalse(command.isVisible());

        // test state change
        testListener.eventCount = 0;
        command.setSecondaryVisibleState(true);
        Assert.assertEquals(1, testListener.eventCount);
        Assert.assertTrue(command.isVisible());

        // test initial value
        command = new TestAbstractCommand();
        command.addPropertyChangeListener("visible", testListener);
        testListener.eventCount = 0;
        command.secondaryVisibleState = true;
        Assert.assertTrue(command.isVisible());
        command.setSecondaryVisibleState(false);
        Assert.assertEquals(1, testListener.eventCount);
        Assert.assertFalse(command.isVisible());
    }

    protected class TestListener implements PropertyChangeListener {

        int eventCount = 0;

        public void propertyChange(PropertyChangeEvent evt) {
            eventCount++;
        }

    }

    protected class TestAbstractCommand extends AbstractCommand {

        boolean secondaryEnabledState;

        boolean secondaryVisibleState;

        public boolean isEnabled() {
            return super.isEnabled() && secondaryEnabledState;
        }

        public boolean isVisible() {
            return super.isVisible() && secondaryVisibleState;
        }

        public void setSecondaryEnabledState(boolean secondaryEnabledState) {
            if (secondaryEnabledState != this.secondaryEnabledState) {
                this.secondaryEnabledState = secondaryEnabledState;
                updatedEnabledState();
            }
        }

        public void setSecondaryVisibleState(boolean secondaryVisibleState) {
            if (secondaryVisibleState != this.secondaryVisibleState) {
                this.secondaryVisibleState = secondaryVisibleState;
                updatedVisibleState();
            }
        }

        public void execute() {
        }
    }
}
