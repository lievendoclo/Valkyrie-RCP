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

