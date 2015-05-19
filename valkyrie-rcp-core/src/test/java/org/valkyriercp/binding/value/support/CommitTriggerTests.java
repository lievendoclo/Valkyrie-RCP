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
package org.valkyriercp.binding.value.support;

import org.junit.Test;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.CommitTriggerListener;

import static junit.framework.Assert.assertEquals;

/**
 * Tests class {@link CommitTrigger}.
 *
 * @author Oliver Hutchison
 */
public class CommitTriggerTests {

    @Test
    public void testCommitTrigger() {
        CommitTrigger ct = new CommitTrigger();
        ct.commit();
        ct.revert();

        TestCommitTriggerListener l = new TestCommitTriggerListener();
        ct.addCommitTriggerListener(l);
        assertEquals(0, l.commits);
        assertEquals(0, l.reverts);

        ct.commit();
        assertEquals(1, l.commits);
        ct.commit();
        assertEquals(2, l.commits);
        assertEquals(0, l.reverts);
        ct.revert();
        assertEquals(2, l.commits);
        assertEquals(1, l.reverts);

        ct.removeCommitTriggerListener(l);

        ct.commit();
        assertEquals(2, l.commits);
        ct.revert();
        assertEquals(1, l.reverts);
    }

    private class TestCommitTriggerListener implements CommitTriggerListener {

        public int commits;

        int reverts;

        public void commit() {
            commits++;
        }

        public void revert() {
            reverts++;
        }
    }
}

