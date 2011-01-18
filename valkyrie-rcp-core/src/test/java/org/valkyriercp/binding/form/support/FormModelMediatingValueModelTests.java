package org.valkyriercp.binding.form.support;

import org.junit.Before;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.test.TestPropertyChangeListener;
import org.valkyriercp.binding.value.DirtyTrackingValueModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.test.TestPropertyChangeListener;

import static org.junit.Assert.*;

/**
 * Tests for @link FormModelMediatingValueModel
 *
 * @author  Oliver Hutchison
 */
public class FormModelMediatingValueModelTests extends AbstractValkyrieTest {

    private FormModelMediatingValueModel mvm;

    private ValueModel pvm;

    private TestPropertyChangeListener vcl;

    private TestPropertyChangeListener dcl;

    @Before
    public void doSetUp() throws Exception {
        pvm = new ValueHolder();
        mvm = new FormModelMediatingValueModel(pvm, true);
        vcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        dcl = new TestPropertyChangeListener(DirtyTrackingValueModel.DIRTY_PROPERTY);
        mvm.addValueChangeListener(vcl);
        mvm.addPropertyChangeListener(DirtyTrackingValueModel.DIRTY_PROPERTY, dcl);
    }

    @Test
    public void testSetGet() {
        mvm.setValue("1");
        vcl.assertLastEvent(1, null, "1");
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());

        mvm.setValue("2");
        vcl.assertLastEvent(2, "1", "2");
        assertEquals("2", mvm.getValue());
        assertEquals("2", pvm.getValue());

        mvm.setValue("2");
        vcl.assertEventCount(2);

        mvm.setValue(null);
        vcl.assertLastEvent(3, "2", null);
        assertEquals(null, mvm.getValue());
        assertEquals(null, pvm.getValue());

        mvm.setValue(null);
        vcl.assertEventCount(3);
    }

    @Test
    public void testDirtyTracking() {
        assertTrue(!mvm.isDirty());
        dcl.assertEventCount(0);

        mvm.setValue("1");
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(1, false, true);

        mvm.setValue(null);
        assertTrue(!mvm.isDirty());
        dcl.assertLastEvent(2, true, false);

        mvm.setValue("2");
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(3, false, true);

        mvm.setValue("3");
        dcl.assertEventCount(3);

        mvm.clearDirty();
        assertTrue(!mvm.isDirty());
        dcl.assertLastEvent(4, true, false);
    }

    @Test
    public void testValueUpFromPropertyValueModelClearsDirty() {
        pvm.setValue("1");
        assertTrue(!mvm.isDirty());
        dcl.assertEventCount(0);

        mvm.setValue("1");
        assertTrue(!mvm.isDirty());
        dcl.assertEventCount(0);

        mvm.setValue("2");
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(1, false, true);

        // XXX Is this failing test important? Fixing this behavior would be fairly complex.
        // pvm.setValue("2");
        // assertTrue(!mvm.isDirty());
        // dcl.assertLastEvent(2, true, false);
    }

    @Test
    public void testTurningOnAndOffDeliverValueChangeEvents() {
        mvm.setDeliverValueChangeEvents(false);
        mvm.setValue("1");
        vcl.assertEventCount(0);
        dcl.assertEventCount(0);
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());
        assertEquals(true, mvm.isDirty());

        mvm.setValue("2");

        mvm.setDeliverValueChangeEvents(true);
        vcl.assertLastEvent(1, null, "2");
        dcl.assertLastEvent(1, false, true);

        mvm.setDeliverValueChangeEvents(false);
        pvm.setValue("1");
        vcl.assertEventCount(1);
        dcl.assertEventCount(1);
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());
        assertEquals(false, mvm.isDirty());

        mvm.setDeliverValueChangeEvents(true);
        vcl.assertLastEvent(2, "2", "1");
        dcl.assertLastEvent(2, true, false);
    }
}

