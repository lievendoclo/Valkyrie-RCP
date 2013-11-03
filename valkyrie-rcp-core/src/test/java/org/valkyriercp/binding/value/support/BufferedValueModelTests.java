package org.valkyriercp.binding.value.support;

import org.junit.Before;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.test.TestBean;
import org.valkyriercp.test.TestPropertyChangeListener;

import static org.junit.Assert.*;

public final class BufferedValueModelTests extends AbstractValkyrieTest {

    private static final Object INITIAL_VALUE = "initial value";
    private static final Object RESET_VALUE   = "reset value";

    private ValueModel wrapped;
    private CommitTrigger commitTrigger;

    @Before
    public void doSetUp() throws Exception {
        wrapped = new ValueHolder(INITIAL_VALUE);
        commitTrigger = new CommitTrigger();
    }

    @Test
    public void testGetWrappedValueModel() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        assertSame(wrapped, buffer.getWrappedValueModel());
        assertSame(wrapped, buffer.getInnerMostWrappedValueModel());

        ValueModel nestedValueModel = new AbstractValueModelWrapper(wrapped) {};
        buffer = new BufferedValueModel(nestedValueModel);
        assertSame(nestedValueModel, buffer.getWrappedValueModel());
        assertSame(wrapped, buffer.getInnerMostWrappedValueModel());
    }

    @Test
    public void testReturnsWrappedValueIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        assertEquals(
            "Buffer value equals the wrapped value before any changes.",
            buffer.getValue(),
            wrapped.getValue());

        wrapped.setValue("change1");
        assertEquals(
            "Buffer value equals the wrapped value changes.",
            buffer.getValue(),
            wrapped.getValue());

        wrapped.setValue(null);
        assertEquals(
            "Buffer value equals the wrapped value changes.",
            buffer.getValue(),
            wrapped.getValue());

        wrapped.setValue("change2");
        assertEquals(
            "Buffer value equals the wrapped value changes.",
            buffer.getValue(),
            wrapped.getValue());
    }

    /**
     * Tests that the BufferedValueModel returns the buffered values
     * once a value has been assigned.
     */
    @Test
    public void testReturnsBufferedValueIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        Object newValue1 = wrapped.getValue();
        buffer.setValue(newValue1);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);

        Object newValue2 = "change1";
        buffer.setValue(newValue2);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue2);

        Object newValue3 = null;
        buffer.setValue(newValue3);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue3);

        Object newValue4 = "change2";
        buffer.setValue(newValue4);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue4);
    }

    @Test
    public void testDetectedWrappedValueChangeIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        Object newValue1 = "change1";
        buffer.setValue(newValue1);
        wrapped.setValue("change3");
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            "change3");
        wrapped.setValue(newValue1);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            newValue1);
        wrapped.setValue(null);
        assertSame(
            "Buffer value == new value once a value has been assigned.",
            buffer.getValue(),
            null);
    }

    /**
     * Tests that the BufferedValueModel returns the wrapped's values
     * after a commit.
     */
    @Test
    public void testReturnsWrappedValueAfterCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");  // shall buffer now
        commit();
        assertEquals(
            "Buffer value equals the wrapped value after a commit.",
            buffer.getValue(),
            wrapped.getValue());

        wrapped.setValue("change2");
        assertEquals(
            "Buffer value equals the wrapped value after a commit.",
            buffer.getValue(),
            wrapped.getValue());

        wrapped.setValue(buffer.getValue());
        assertEquals(
            "Buffer value equals the wrapped value after a commit.",
            buffer.getValue(),
            wrapped.getValue());
    }

    /**
     * Tests that the BufferedValueModel returns the wrapped's values
     * after a flush.
     */
    @Test
    public void testReturnsWrappedValueAfterFlush() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");  // shall buffer now
        revert();
        assertEquals(
            "Buffer value equals the wrapped value after a flush.",
            wrapped.getValue(),
            buffer.getValue());

        wrapped.setValue("change2");
        assertEquals(
            "Buffer value equals the wrapped value after a flush.",
            wrapped.getValue(),
            buffer.getValue());
    }


    // Testing Proper Value Commit and Flush **********************************

    /**
     * Tests the core of the buffering feature: buffer modifications
     * do not affect the wrapped before a commit.
     */
    @Test
    public void testWrappedValuesUnchangedBeforeCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object oldWrappedValue = wrapped.getValue();
        buffer.setValue("changedBuffer1");
        assertEquals(
            "Buffer changes do not change the wrapped value before a commit.",
            wrapped.getValue(),
            oldWrappedValue
        );
        buffer.setValue(null);
        assertEquals(
            "Buffer changes do not change the wrapped value before a commit.",
            wrapped.getValue(),
            oldWrappedValue
        );
        buffer.setValue(oldWrappedValue);
        assertEquals(
            "Buffer changes do not change the wrapped value before a commit.",
            wrapped.getValue(),
            oldWrappedValue
        );
        buffer.setValue("changedBuffer2");
        assertEquals(
            "Buffer changes do not change the wrapped value before a commit.",
            wrapped.getValue(),
            oldWrappedValue
        );
    }

    /**
     * Tests the core of a commit: buffer changes are written through on commit
     * and change the wrapped value.
     */
    @Test
    public void testCommitChangesWrappedValue() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object oldWrappedValue = wrapped.getValue();
        Object newValue1 = "change1";
        buffer.setValue(newValue1);
        assertEquals(
            "Wrapped value is unchanged before the first commit.",
            wrapped.getValue(),
            oldWrappedValue);
        commit();
        assertEquals(
            "Wrapped value is the new value after the first commit.",
            wrapped.getValue(),
            newValue1);

        // Set the buffer to the current wrapped value to check whether
        // the starts buffering, even if there's no value difference.
        Object newValue2 = wrapped.getValue();
        buffer.setValue(newValue2);
        commit();
        assertEquals(
            "Wrapped value is the new value after the second commit.",
            wrapped.getValue(),
            newValue2);
    }

    /**
     * Tests the core of a flush action: buffer changes are overridden
     * by wrapped changes after a flush.
     */
    @Test
    public void testFlushResetsTheBufferedValue() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        Object newValue1 = "new value1";
        buffer.setValue(newValue1);
        assertSame(
            "Buffer value reflects changes before the first flush.",
            buffer.getValue(),
            newValue1);
        revert();
        assertEquals(
            "Buffer value is the wrapped value after the first flush.",
            buffer.getValue(),
            wrapped.getValue());

        // Set the buffer to the current wrapped value to check whether
        // the starts buffering, even if there's no value difference.
        Object newValue2 = wrapped.getValue();
        buffer.setValue(newValue2);
        assertSame(
            "Buffer value reflects changes before the flush.",
            buffer.getValue(),
            newValue2);
        revert();
        assertEquals(
            "Buffer value is the wrapped value after the second flush.",
            buffer.getValue(),
            wrapped.getValue());
    }

    // Tests a Proper Buffering State *****************************************

    /**
     * Tests that a buffer isn't buffering as long as no value has been assigned.
     */
    @Test
    public void testIsNotBufferingIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        assertFalse(
            "Initially the buffer does not buffer.",
            buffer.isBuffering());

        Object newValue = "change1";
        wrapped.setValue(newValue);
        assertFalse(
            "Wrapped changes do not affect the buffering state.",
            buffer.isBuffering());

        wrapped.setValue(null);
        assertFalse(
            "Wrapped change to null does not affect the buffering state.",
            buffer.isBuffering());
    }

    /**
     * Tests that the buffer is buffering once a value has been assigned.
     */
    @Test
    public void testIsBufferingIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        assertTrue(
            "Setting a value (even the wrapped's value) turns on buffering.",
            buffer.isBuffering());

        buffer.setValue("change2");
        assertTrue(
            "Changing the value doesn't affect the buffering state.",
            buffer.isBuffering());

        buffer.setValue(wrapped.getValue());
        assertTrue(
            "Resetting the value to the wrapped's value should affect buffering.",
            !buffer.isBuffering());
    }

    /**
     * Tests that the buffer is not buffering after a commit.
     */
    @Test
    public void testIsNotBufferingAfterCommit() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        commit();
        assertFalse(
            "The buffer does not buffer after a commit.",
            buffer.isBuffering());

        Object newValue = "change1";
        wrapped.setValue(newValue);
        assertFalse(
        "The buffer does not buffer after a commit and wrapped change1.",
            buffer.isBuffering());

        wrapped.setValue(null);
        assertFalse(
        "The buffer does not buffer after a commit and wrapped change2.",
            buffer.isBuffering());
    }

    /**
     * Tests that the buffer is not buffering after a flush.
     */
    @Test
    public void testIsNotBufferingAfterFlush() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("change1");
        revert();
        assertFalse(
            "The buffer does not buffer after a flush.",
            buffer.isBuffering());

        Object newValue = "change1";
        wrapped.setValue(newValue);
        assertFalse(
        "The buffer does not buffer after a flush and wrapped change1.",
            buffer.isBuffering());

        wrapped.setValue(null);
        assertFalse(
        "The buffer does not buffer after a flush and wrapped change2.",
            buffer.isBuffering());
    }

    /**
     * Tests that changing the buffering state fires changes of
     * the <i>buffering</i> property.
     */
    @Test
    public void testFiresBufferingChanges() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();

        TestPropertyChangeListener pcl = new TestPropertyChangeListener(BufferedValueModel.BUFFERING_PROPERTY);
        buffer.addPropertyChangeListener(BufferedValueModel.BUFFERING_PROPERTY, pcl);

        assertEquals("Initial state.", 0, pcl.eventCount());
        buffer.getValue();
        assertEquals("Reading initial value.", 0, pcl.eventCount());
        buffer.setCommitTrigger(null);
        buffer.setCommitTrigger(commitTrigger);
        assertEquals("After commit trigger change.", 0, pcl.eventCount());

        buffer.setValue("now buffering");
        assertEquals("After setting the first value.", 1, pcl.eventCount());
        buffer.setValue("still buffering");
        assertEquals("After setting the second value.", 1, pcl.eventCount());
        buffer.getValue();
        assertEquals("Reading buffered value.", 1, pcl.eventCount());

        wrapped.setValue(buffer.getValue());
        assertEquals("Changing wrapped to same as buffer.", 2, pcl.eventCount());

        commit();
        assertEquals("After committing.", 2, pcl.eventCount());
        buffer.getValue();
        assertEquals("Reading unbuffered value.", 2, pcl.eventCount());

        buffer.setValue("buffering again");
        assertEquals("After second buffering switch.", 3, pcl.eventCount());
        revert();
        assertEquals("After flushing.", 4, pcl.eventCount());
        buffer.getValue();
        assertEquals("Reading unbuffered value.", 4, pcl.eventCount());

        buffer.setValue("before real commit");
        assertEquals("With new change to be committed", 5, pcl.eventCount());
        commit();
        assertEquals("With new change committed", 6, pcl.eventCount());
    }

    @Test
    public void testSetValueSendsProperValueChangeEvents() {
        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testSetValueSendsProperEvents(null, obj1,   true);
        testSetValueSendsProperEvents(obj1, null,   true);
        testSetValueSendsProperEvents(obj1, obj1,   false);
        testSetValueSendsProperEvents(obj1, obj2a,  true);
        testSetValueSendsProperEvents(obj2a, obj2b, false);
        testSetValueSendsProperEvents(null, null,   false);
    }

    @Test
    public void testValueChangeSendsProperValueChangeEvents() {
        Object obj1  = new Integer(1);
        Object obj2a = new Integer(2);
        Object obj2b = new Integer(2);
        testValueChangeSendsProperEvents(null, obj1,   true);
        testValueChangeSendsProperEvents(obj1, null,   true);
        testValueChangeSendsProperEvents(obj1, obj1,   false);
        testValueChangeSendsProperEvents(obj1, obj2a,  true);
        testValueChangeSendsProperEvents(obj2a, obj2b, false);
        testValueChangeSendsProperEvents(null, null,   false);

    }


    // Commit Trigger Tests *************************************************


    /**
     * Checks that #setCommitTrigger changes the commit trigger.
     */
    @Test
    public void testCommitTriggerChange() {
        CommitTrigger trigger1 = new CommitTrigger();
        CommitTrigger trigger2 = new CommitTrigger();

        BufferedValueModel buffer = new BufferedValueModel(wrapped, trigger1);
        assertSame(
            "Commit trigger has been changed.",
            buffer.getCommitTrigger(),
            trigger1);

        buffer.setCommitTrigger(trigger2);
        assertSame(
            "Commit trigger has been changed.",
            buffer.getCommitTrigger(),
            trigger2);

        buffer.setCommitTrigger(null);
        assertSame(
            "Commit trigger has been changed.",
            buffer.getCommitTrigger(),
            null);
    }

    /**
     * Checks and verifies that commit and flush events are driven
     * by the current commit trigger.
     */
    @Test
    public void testListensToCurrentCommitTrigger() {
        CommitTrigger trigger1 = new CommitTrigger();
        CommitTrigger trigger2 = new CommitTrigger();

        BufferedValueModel buffer = new BufferedValueModel(wrapped, trigger1);
        buffer.setValue("change1");
        Object wrappedValue = wrapped.getValue();
        Object bufferedValue = buffer.getValue();
        trigger2.commit();
        assertEquals(
            "Changing the unrelated trigger2 to commit has no effect on the wrapped.",
            wrapped.getValue(),
            wrappedValue);
        assertSame(
            "Changing the unrelated trigger2 to commit has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        trigger2.revert();
        assertEquals(
            "Changing the unrelated trigger2 to revert has no effect on the wrapped.",
            wrapped.getValue(),
            wrappedValue);
        assertSame(
            "Changing the unrelated trigger2 to revert has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        // Change the commit trigger to trigger2.
        buffer.setCommitTrigger(trigger2);
        assertSame(
            "Commit trigger has been changed.",
            buffer.getCommitTrigger(),
            trigger2);

        trigger1.commit();
        assertEquals(
            "Changing the unrelated trigger1 to commit has no effect on the wrapped.",
            wrapped.getValue(),
            wrappedValue);
        assertSame(
            "Changing the unrelated trigger1 to commit has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        trigger1.revert();
        assertEquals(
            "Changing the unrelated trigger1 to revert has no effect on the wrapped.",
            wrapped.getValue(),
            wrappedValue);
        assertSame(
            "Changing the unrelated trigger1 to revert has no effect on the buffer.",
            buffer.getValue(),
            bufferedValue);

        // Commit using trigger2.
        trigger2.commit();
        assertEquals(
            "Changing the current trigger2 to commit commits the buffered value.",
            buffer.getValue(),
            wrapped.getValue());

        buffer.setValue("change2");
        wrappedValue = wrapped.getValue();
        trigger2.revert();
        assertEquals(
            "Changing the current trigger2 to revert flushes the buffered value.",
            buffer.getValue(),
            wrapped.getValue());
        assertEquals(
            "Changing the current trigger2 to revert flushes the buffered value.",
            buffer.getValue(),
            wrappedValue);
    }


    // Tests Proper Update Notifications **************************************

    /**
     * Checks that wrapped changes fire value changes
     * if no value has been assigned.
     */
    @Test
    public void testPropagatesWrappedChangesIfNoValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        buffer.addValueChangeListener(pcl);

        wrapped.setValue("change1");
        assertEquals("Value change.", 1, pcl.eventCount());

        wrapped.setValue(null);
        assertEquals("Value change.", 2, pcl.eventCount());

        wrapped.setValue("change2");
        assertEquals("Value change.", 3, pcl.eventCount());

        wrapped.setValue(buffer.getValue());
        assertEquals("No value change.", 3, pcl.eventCount());
    }

    /**
     * Tests that wrapped changes are  propagated once a value has
     * been assigned, i.e. the buffer is buffering.
     */
    @Test
    public void testIgnoresWrappedChangesIfValueAssigned() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        buffer.addValueChangeListener(pcl);

        buffer.setValue("new buffer");
        wrapped.setValue("change1");
        assertEquals("Value change.", 2, pcl.eventCount());

        buffer.setValue("new buffer");
        wrapped.setValue(null);
        assertEquals("Value change.", 4, pcl.eventCount());

        buffer.setValue("new buffer");
        wrapped.setValue("change2");
        assertEquals("Value change.", 6, pcl.eventCount());

        buffer.setValue("new buffer");
        wrapped.setValue(buffer.getValue());    // won't fire event
        assertEquals("No value change.", 7, pcl.eventCount());
    }

    /**
     * Checks and verifies that a commit fires no value change.
     */
    @Test
    public void testCommitFiresNoChangeOnSameOldAndNewValues() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        buffer.setValue("value1");
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        buffer.addValueChangeListener(pcl);

        assertEquals("No initial change.", 0, pcl.eventCount());
        commit();
        assertEquals("First commit: no change.", 0, pcl.eventCount());

        buffer.setValue("value2");
        assertEquals("Setting a value: a change.", 1, pcl.eventCount());
        commit();
        assertEquals("Second commit: no change.", 1, pcl.eventCount());
    }

    @Test
    public void testCommitFiresChangeOnDifferentOldAndNewValues() {
        BufferedValueModel buffer = createDefaultBufferedValueModel(
                new ToUpperCaseStringHolder());
        buffer.setValue("initialValue");
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        buffer.addValueChangeListener(pcl);
        buffer.setValue("value1");
        assertEquals("One event fired",
                1,
                pcl.eventCount());
        assertEquals("First value set.",
                "value1",
                pcl.lastEvent().getNewValue());
        commit();
        assertEquals("Commit fires if the wrapped modifies the value.",
                2,
                pcl.eventCount());
        assertEquals("Old value is the buffered value.",
                "value1",
                pcl.lastEvent().getOldValue());
        assertEquals("New value is the modified value.",
                "VALUE1",
                pcl.lastEvent().getNewValue());
    }

    /**
     * Tests that a flush event fires a value change if and only if
     * the flushed value does not equal the buffered value.
     */
    @Test
    public void testFlushFiresTrueValueChanges() {
        BufferedValueModel buffer = createDefaultBufferedValueModel();
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);

        wrapped.setValue("new wrapped");
        buffer.setValue("new buffer");
        buffer.addValueChangeListener(pcl);
        revert();
        assertEquals("First flush changes value.", 1, pcl.eventCount());

        buffer.setValue(wrapped.getValue());
        assertEquals("Resetting value: no change.", 1, pcl.eventCount());
        revert();
        assertEquals("Second flush: no change.", 1, pcl.eventCount());

        buffer.setValue("new buffer2");
        assertEquals("Second value change.", 2, pcl.eventCount());
        wrapped.setValue("new wrapped2");
        assertEquals("Setting new wrapped value: no change.", 3, pcl.eventCount());
        buffer.setValue(wrapped.getValue());
        assertEquals("Third value change.", 3, pcl.eventCount());
        revert();
        assertEquals("Third flush: no change.", 3, pcl.eventCount());
    }

    // Misc Tests *************************************************************

    /**
     * Tests read actions on a read-only model.
     */
    @Test
    public void testReadOnly() {
        TestBean bean = new TestBean();
        ValueModel readOnlyModel = new BeanPropertyAccessStrategy(bean).getPropertyValueModel("readOnly");
        BufferedValueModel buffer = new BufferedValueModel(readOnlyModel, commitTrigger);

        assertSame(
            "Can read values from a read-only model.",
            buffer.getValue(),
            readOnlyModel.getValue());

        Object newValue1 = "new value";
        buffer.setValue(newValue1);
        assertSame(
            "Can read values from a read-only model when buffering.",
            buffer.getValue(),
            newValue1);

        revert();
        assertSame(
            "Can read values from a read-only model after a flush.",
            buffer.getValue(),
            bean.getReadOnly());

        buffer.setValue("new value2");
        try {
            commit();
            fail("Cannot commit to a read-only model.");
        } catch (Exception e) {
            // The expected behavior
        }
    }

    // Test Implementations ***************************************************

    private void testSetValueSendsProperEvents(Object oldValue, Object newValue, boolean eventExpected) {
        BufferedValueModel valueModel =
            new BufferedValueModel(new ValueHolder(oldValue), new CommitTrigger());
        testSendsProperEvents(valueModel, oldValue, newValue, eventExpected);
    }

    private void testValueChangeSendsProperEvents(Object oldValue, Object newValue, boolean eventExpected) {
        BufferedValueModel defaultModel = createDefaultBufferedValueModel();
        defaultModel.setValue(oldValue);
        testSendsProperEvents(defaultModel, oldValue, newValue, eventExpected);
    }

    private void testSendsProperEvents(BufferedValueModel valueModel, Object oldValue, Object newValue, boolean eventExpected) {
        TestPropertyChangeListener pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        valueModel.addValueChangeListener(pcl);
        int expectedEventCount = eventExpected ? 1 : 0;

        valueModel.setValue(newValue);
        assertEquals(
                "Expected event count after ( " +
                oldValue + " -> " + newValue + ").",
                expectedEventCount,
                pcl.eventCount());
        if (eventExpected) {
            assertEquals("Event's old value.", oldValue, pcl.lastEvent().getOldValue());
            assertEquals("Event's new value.", newValue, pcl.lastEvent().getNewValue());
        }
    }


    // Helper Code ************************************************************

    private void commit() {
        commitTrigger.commit();
    }

    private void revert() {
        commitTrigger.revert();
    }

    private BufferedValueModel createDefaultBufferedValueModel() {
        wrapped.setValue(RESET_VALUE);
        return new BufferedValueModel(wrapped, commitTrigger);
    }

    private BufferedValueModel createDefaultBufferedValueModel(ValueModel wrapped) {
        wrapped.setValue(RESET_VALUE);
        return new BufferedValueModel(wrapped, commitTrigger);
    }

    // A String typed ValueModel that modifies set values to uppercase.
    private static class ToUpperCaseStringHolder extends AbstractValueModel {

        private String text;

        public Object getValue() {
            return text;
        }

        public void setValue(Object newValue) {
            String newText = ((String) newValue).toUpperCase();
            Object oldText = text;
            text = newText;
            fireValueChange(oldText, newText);
        }

    }

    /**
     * This class is used to test alternate value change detection methods.
     */
    private static class StrictEquivalenceValueChangeDetector implements ValueChangeDetector {
        public boolean hasValueChanged(Object oldValue, Object newValue) {
            return oldValue != newValue;
        }
    }

}

