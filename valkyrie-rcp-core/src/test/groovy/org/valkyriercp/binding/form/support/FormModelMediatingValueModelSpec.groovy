/*
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
package org.valkyriercp.binding.form.support
import org.valkyriercp.AbstractValkyrieSpec
import org.valkyriercp.binding.value.DirtyTrackingValueModel
import org.valkyriercp.binding.value.ValueModel
import org.valkyriercp.binding.value.support.ValueHolder
import org.valkyriercp.test.TestPropertyChangeListener

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class FormModelMediatingValueModelSpec extends AbstractValkyrieSpec {
    def FormModelMediatingValueModel mvm;
    def ValueModel pvm;
    def TestPropertyChangeListener vcl;
    def TestPropertyChangeListener dcl;

    def setup() {
        pvm = new ValueHolder();
        mvm = new FormModelMediatingValueModel(pvm, true);
        vcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        dcl = new TestPropertyChangeListener(DirtyTrackingValueModel.DIRTY_PROPERTY);
        mvm.addValueChangeListener(vcl);
        mvm.addPropertyChangeListener(DirtyTrackingValueModel.DIRTY_PROPERTY, dcl);
    }

    def testSetGet() {
        when:
        mvm.setValue("1");
        then:
        vcl.assertLastEvent(1, null, "1");
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());

        when:
        mvm.setValue("2");
        then:
        vcl.assertLastEvent(2, "1", "2");
        assertEquals("2", mvm.getValue());
        assertEquals("2", pvm.getValue());

        when:
        mvm.setValue("2");
        then:
        vcl.assertEventCount(2);

        when:
        mvm.setValue(null);
        then:
        vcl.assertLastEvent(3, "2", null);
        assertEquals(null, mvm.getValue());
        assertEquals(null, pvm.getValue());

        when:
        mvm.setValue(null);
        then:
        vcl.assertEventCount(3);
    }

    def testDirtyTracking() {
        when:
        assertTrue(!mvm.isDirty());
        then:
        dcl.assertEventCount(0);

        when:
        mvm.setValue("1");
        then:
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(1, false, true);

        when:
        mvm.setValue(null);
        then:
        assertTrue(!mvm.isDirty());
        dcl.assertLastEvent(2, true, false);

        when:
        mvm.setValue("2");
        then:
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(3, false, true);

        when:
        mvm.setValue("3");
        then:
        dcl.assertEventCount(3);

        when:
        mvm.clearDirty();
        then:
        assertTrue(!mvm.isDirty());
        dcl.assertLastEvent(4, true, false);
    }

    def testValueUpFromPropertyValueModelClearsDirty() {
        when:
        pvm.setValue("1");
        then:
        assertTrue(!mvm.isDirty());
        dcl.assertEventCount(0);

        when:
        mvm.setValue("1");
        then:
        assertTrue(!mvm.isDirty());
        dcl.assertEventCount(0);

        when:
        mvm.setValue("2");
        then:
        assertTrue(mvm.isDirty());
        dcl.assertLastEvent(1, false, true);

        // XXX Is this failing test important? Fixing this behavior would be fairly complex.
        // when:
        // pvm.setValue("2");
        // then:
        // assertTrue(!mvm.isDirty());
        // dcl.assertLastEvent(2, true, false);
    }

    def testTurningOnAndOffDeliverValueChangeEvents() {
        when:
        mvm.setDeliverValueChangeEvents(false);
        mvm.setValue("1");
        then:
        vcl.assertEventCount(0);
        dcl.assertEventCount(0);
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());
        assertEquals(true, mvm.isDirty());

        when:
        mvm.setValue("2");
        mvm.setDeliverValueChangeEvents(true);
        then:
        vcl.assertLastEvent(1, null, "2");
        dcl.assertLastEvent(1, false, true);

        when:
        mvm.setDeliverValueChangeEvents(false);
        pvm.setValue("1");
        then:
        vcl.assertEventCount(1);
        dcl.assertEventCount(1);
        assertEquals("1", mvm.getValue());
        assertEquals("1", pvm.getValue());
        assertEquals(false, mvm.isDirty());

        when:
        mvm.setDeliverValueChangeEvents(true);
        then:
        vcl.assertLastEvent(2, "2", "1");
        dcl.assertLastEvent(2, true, false);
    }
}