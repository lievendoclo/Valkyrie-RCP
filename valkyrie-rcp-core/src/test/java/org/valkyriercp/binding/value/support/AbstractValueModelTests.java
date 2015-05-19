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

import org.junit.Before;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.test.TestPropertyChangeListener;

import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public abstract class AbstractValueModelTests extends AbstractValkyrieTest {

    private TestAbstractValueModel vm;

    private TestPropertyChangeListener pcl;

    private class EqualsEverything {
        public boolean equals(Object other) {
            return true;
        }
    }

    @Before
    public void doSetUp() {
        vm = new TestAbstractValueModel();
        pcl = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);
        vm.addValueChangeListener(pcl);
    }

    @Test
    public void testSetValueSilently() {
        PropertyChangeListener pcl1 = new TestPropertyChangeListener(ValueModel.VALUE_PROPERTY);

        vm.setValueSilently("1", pcl1);
        assertEquals("should have notified listener", 1, pcl.eventCount());
        assertEquals("should have changed value", vm.value, "1");

        vm.setValueSilently("2", pcl);
        assertEquals("should not have notified listener", 1, pcl.eventCount());
        assertEquals("should have changed value", vm.value, "2");

        vm.setValue("3");
        assertEquals("should have notified listener", 2, pcl.eventCount());
        assertEquals("should have changed value", vm.value, "3");
    }

    @Test
    public void testFirePrimativeValueChange() {
        vm.fireValueChange(true, false);
        assertEquals(1, pcl.eventCount());
        assertSame(Boolean.TRUE, pcl.lastEvent().getOldValue());
        assertSame(Boolean.FALSE, pcl.lastEvent().getNewValue());

        vm.fireValueChange(1, 2);
        assertEquals(2, pcl.eventCount());
        assertEquals(new Integer(1), pcl.lastEvent().getOldValue());
        assertEquals(new Integer(2), pcl.lastEvent().getNewValue());

        vm.fireValueChange(1l, 2l);
        assertEquals(3, pcl.eventCount());
        assertEquals(new Long(1), pcl.lastEvent().getOldValue());
        assertEquals(new Long(2), pcl.lastEvent().getNewValue());

        vm.fireValueChange(1f, 2f);
        assertEquals(4, pcl.eventCount());
        assertEquals(new Double(1f), pcl.lastEvent().getOldValue());
        assertEquals(new Double(2f), pcl.lastEvent().getNewValue());
    }

    @Test
    public void testFireValueChange() {
        Object o1 = new Object();
        Object o2 = new Object();

        vm.fireValueChange(o1, o2);
        assertEquals(1, pcl.eventCount());
        assertSame(o1, pcl.lastEvent().getOldValue());
        assertSame(o2, pcl.lastEvent().getNewValue());

        vm.fireValueChange(o1, o1);
        assertEquals(1, pcl.eventCount());

        vm.fireValueChange(o1, null);
        assertEquals(2, pcl.eventCount());
        assertEquals(o1, pcl.lastEvent().getOldValue());
        assertEquals(null, pcl.lastEvent().getNewValue());

        vm.fireValueChange(null, o1);
        assertEquals(3, pcl.eventCount());
        assertEquals(null, pcl.lastEvent().getOldValue());
        assertEquals(o1, pcl.lastEvent().getNewValue());

        vm.fireValueChange(null, null);
        assertEquals(3, pcl.eventCount());

        vm.fireValueChange(new Integer(1), new Integer(1));
        assertEquals(3, pcl.eventCount());

        vm.fireValueChangeWhenStillEqual();
        assertEquals(4, pcl.eventCount());
        assertEquals(vm.value, pcl.lastEvent().getOldValue());
        assertEquals(vm.value, pcl.lastEvent().getNewValue());

        Object equalsEverything = new EqualsEverything();
        vm.fireValueChange(o1, equalsEverything);
        assertEquals(5, pcl.eventCount());
        assertEquals(o1, pcl.lastEvent().getOldValue());
        assertSame(equalsEverything, pcl.lastEvent().getNewValue());

        vm.fireValueChange(equalsEverything, o1);
        assertEquals(6, pcl.eventCount());
        assertSame(equalsEverything, pcl.lastEvent().getOldValue());
        assertEquals(o1, pcl.lastEvent().getNewValue());
    }

    @Test
    public void testHasValueChangedChecksIdentityForUnsafeClasses() {
        Object equalsEverything = new EqualsEverything();
        Object o2 = new Object();

        assertTrue(vm.hasValueChanged(equalsEverything, o2));
        assertTrue(vm.hasValueChanged(null, o2));
        assertTrue(vm.hasValueChanged(null, equalsEverything));
        assertTrue(vm.hasValueChanged(equalsEverything, null));

        assertTrue(!vm.hasValueChanged(equalsEverything, equalsEverything));
        assertTrue(!vm.hasValueChanged(null, null));
    }

    @Test
    public void testHasValueChangedChecksEqualityForSafeClasses() {
        vm.setValueChangeDetector( new DefaultValueChangeDetector() );

        testChecksEqualityForSafeClasses(new Boolean(true), new Boolean(false), new Boolean(true));
        testChecksEqualityForSafeClasses(new Byte((byte)1), new Byte((byte)2), new Byte((byte)1));
        testChecksEqualityForSafeClasses(new Short((short)1), new Short((short)2), new Short((short)1));
        testChecksEqualityForSafeClasses(new Integer(1), new Integer(2), new Integer(1));
        testChecksEqualityForSafeClasses(new Long(1), new Long(2), new Long(1));
        testChecksEqualityForSafeClasses(new Float(1), new Float(2), new Float(1));
        testChecksEqualityForSafeClasses(new Double(1), new Double(2), new Double(1));
        testChecksEqualityForSafeClasses(new BigInteger("1"), new BigInteger("2"), new BigInteger("1"));
        testChecksEqualityForSafeClasses(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1"));
        testChecksEqualityForSafeClasses(new Character('a'), new Character('b'), new Character('a'));
        testChecksEqualityForSafeClasses("1", "2", new String("1"));
    }

    private void testChecksEqualityForSafeClasses(Object o1, Object o2, Object o3) {
        Object other = new Object();

        assertNotSame(o1, o2);
        assertNotSame(o1, o3);
        assertEquals(o1, o3);

        assertTrue(vm.hasValueChanged(o1, o2));
        assertTrue(vm.hasValueChanged(null, o2));
        assertTrue(vm.hasValueChanged(o1, null));
        assertTrue(vm.hasValueChanged(o1, other));

        assertTrue(vm.hasValueChanged(other, o1));
        assertTrue(!vm.hasValueChanged(o1, o1));
        assertTrue(!vm.hasValueChanged(o1, o3));
        assertTrue(!vm.hasValueChanged(o3, o1));
    }


    public class TestAbstractValueModel extends AbstractValueModel {

        public Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object newValue) {
            Object oldValue = value;
            value = newValue;
            fireValueChange(oldValue, newValue);
        }
    }
}
