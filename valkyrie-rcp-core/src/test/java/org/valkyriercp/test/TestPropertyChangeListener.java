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
package org.valkyriercp.test;

import junit.framework.Assert;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of PropertyChangeListener that logs all
 * events received. Intended to be used in unit tests.
 *
 * @author Oliver Hutchison
 */
public class TestPropertyChangeListener implements PropertyChangeListener {

    private String onlyForProperty;

    private List eventsRecevied = new ArrayList();

    public TestPropertyChangeListener(String onlyForProperty) {
        this.onlyForProperty = onlyForProperty;
    }

    public void reset() {
        eventsRecevied.clear();
    }

    public List getEventsRecevied() {
        return eventsRecevied;
    }

    public int eventCount() {
        return eventsRecevied.size();
    }

    public PropertyChangeEvent lastEvent() {
        return (PropertyChangeEvent)eventsRecevied.get(eventCount() - 1);
    }

    public void propertyChange(PropertyChangeEvent e) {
        Assert.assertEquals("Received PropertyChangeEvent for unexpected property", onlyForProperty,
                e.getPropertyName());
        eventsRecevied.add(e);
    }

    public void assertEventCount(int count) {
        Assert.assertEquals("Listener has received unexpected number of events", count, eventCount());
    }

    public void assertLastEvent(int count, Object oldValue, Object newValue) {
        assertEventCount(count);
        Assert.assertEquals("Listener has received unexpected oldValue", oldValue, lastEvent().getOldValue());
        Assert.assertEquals("Listener has received unexpected newValue", newValue, lastEvent().getNewValue());
    }

    public void assertLastEvent(int count, boolean oldValue, boolean newValue) {
        assertLastEvent(count, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }
}

