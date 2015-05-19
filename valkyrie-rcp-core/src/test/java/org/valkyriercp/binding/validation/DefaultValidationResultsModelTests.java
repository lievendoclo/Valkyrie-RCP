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
package org.valkyriercp.binding.validation;

import org.junit.Before;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.binding.validation.support.DefaultValidationResults;
import org.valkyriercp.binding.validation.support.DefaultValidationResultsModel;
import org.valkyriercp.core.Severity;
import org.valkyriercp.test.TestPropertyChangeListener;

import static org.junit.Assert.assertEquals;

/**
 * Tests for @link DefaultValidationResultsModel
 *
 * @author  Oliver Hutchison
 */
public class DefaultValidationResultsModelTests extends AbstractValkyrieTest {
    private DefaultValidationResultsModel vrm;

    private TestPropertyChangeListener warnListener;

    private TestPropertyChangeListener infoListener;

    private TestPropertyChangeListener errorsListener;

    private TestValidationListener listener;

    private TestValidationListener field1Listener;

    private TestValidationListener nullListener;

    @Before
    public void setUp() {
        vrm = new DefaultValidationResultsModel();
        errorsListener = new TestPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY);
        vrm.addPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY, errorsListener);
        warnListener = new TestPropertyChangeListener(ValidationResultsModel.HAS_WARNINGS_PROPERTY);
        vrm.addPropertyChangeListener(ValidationResultsModel.HAS_WARNINGS_PROPERTY, warnListener);
        infoListener = new TestPropertyChangeListener(ValidationResultsModel.HAS_INFO_PROPERTY);
        vrm.addPropertyChangeListener(ValidationResultsModel.HAS_INFO_PROPERTY, infoListener);
        listener = new TestValidationListener();
        vrm.addValidationListener(listener);
        field1Listener = new TestValidationListener();
        vrm.addValidationListener("field1", field1Listener);
        nullListener = new TestValidationListener();
        vrm.addValidationListener(ValidationMessage.GLOBAL_PROPERTY, nullListener);
    }

    @Test
    public void testUpdatesFirePropertyChangeEvents() {
        vrm.updateValidationResults(getResults("field1", Severity.INFO));
        assertEquals(1, infoListener.eventCount());
        assertEquals(Boolean.FALSE, infoListener.lastEvent().getOldValue());
        assertEquals(Boolean.TRUE, infoListener.lastEvent().getNewValue());
        vrm.updateValidationResults(getResults("field1", Severity.INFO));
        assertEquals(1, infoListener.eventCount());

        vrm.updateValidationResults(getResults("field1", Severity.WARNING));
        assertEquals(1, warnListener.eventCount());
        assertEquals(Boolean.FALSE, warnListener.lastEvent().getOldValue());
        assertEquals(Boolean.TRUE, warnListener.lastEvent().getNewValue());
        assertEquals(2, infoListener.eventCount());
        assertEquals(Boolean.TRUE, infoListener.lastEvent().getOldValue());
        assertEquals(Boolean.FALSE, infoListener.lastEvent().getNewValue());
        vrm.updateValidationResults(getResults("field1", Severity.WARNING));
        assertEquals(1, warnListener.eventCount());

        vrm.updateValidationResults(getResults("field1", Severity.ERROR));
        assertEquals(1, errorsListener.eventCount());
        assertEquals(Boolean.FALSE, errorsListener.lastEvent().getOldValue());
        assertEquals(Boolean.TRUE, errorsListener.lastEvent().getNewValue());
        assertEquals(2, warnListener.eventCount());
        assertEquals(Boolean.TRUE, warnListener.lastEvent().getOldValue());
        assertEquals(Boolean.FALSE, warnListener.lastEvent().getNewValue());
        vrm.updateValidationResults(getResults("field1", Severity.ERROR));
        assertEquals(1, errorsListener.eventCount());

        vrm.clearAllValidationResults();
        assertEquals(2, infoListener.eventCount());
        assertEquals(Boolean.TRUE, infoListener.lastEvent().getOldValue());
        assertEquals(Boolean.FALSE, infoListener.lastEvent().getNewValue());
        assertEquals(2, warnListener.eventCount());
        assertEquals(Boolean.TRUE, warnListener.lastEvent().getOldValue());
        assertEquals(Boolean.FALSE, warnListener.lastEvent().getNewValue());
        assertEquals(2, errorsListener.eventCount());
        assertEquals(Boolean.TRUE, errorsListener.lastEvent().getOldValue());
        assertEquals(Boolean.FALSE, errorsListener.lastEvent().getNewValue());
    }

    @Test
    public void testEventsHaveCorectSource() {
        vrm.updateValidationResults(getResults("field1", Severity.ERROR));
        assertEquals(vrm, errorsListener.lastEvent().getSource());
        assertEquals(vrm, listener.lastResults());

        ValidationResultsModel delegateFor = new DefaultValidationResultsModel();
        vrm = new DefaultValidationResultsModel(delegateFor);
        vrm.addValidationListener(listener);
        vrm.addPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY, errorsListener);
        vrm.updateValidationResults(getResults("field1", Severity.ERROR));
        assertEquals(delegateFor, errorsListener.lastEvent().getSource());
        assertEquals(delegateFor, listener.lastResults());
    }

    @Test
    public void testUpdatesFireValidationEvents() {
        vrm.updateValidationResults(getResults("field1", Severity.INFO));
        assertEquals(1, listener.eventCount());
        assertEquals(1, field1Listener.eventCount());
        assertEquals(0, nullListener.eventCount());
        assertEquals(vrm, listener.lastResults());
        assertEquals(vrm, field1Listener.lastResults());
        assertEquals(null, nullListener.lastResults());

        vrm.updateValidationResults(getResults("field1", Severity.INFO, ValidationMessage.GLOBAL_PROPERTY, Severity.ERROR));
        assertEquals(2, listener.eventCount());
        assertEquals(2, field1Listener.eventCount());
        assertEquals(1, nullListener.eventCount());
        assertEquals(vrm, nullListener.lastResults());

        vrm.clearAllValidationResults();
        assertEquals(3, listener.eventCount());
        assertEquals(3, field1Listener.eventCount());
        assertEquals(2, nullListener.eventCount());

        vrm.clearAllValidationResults();
        assertEquals(3, listener.eventCount());
        assertEquals(3, field1Listener.eventCount());
        assertEquals(2, nullListener.eventCount());

        vrm.updateValidationResults(getResults(ValidationMessage.GLOBAL_PROPERTY, Severity.INFO));
        assertEquals(4, listener.eventCount());
        assertEquals(3, field1Listener.eventCount());
        assertEquals(3, nullListener.eventCount());
    }

    @Test
    public void testMessageCount() {
    	DefaultValidationResultsModel resultsModel = new DefaultValidationResultsModel();
    	resultsModel.addMessage(new DefaultValidationMessage("property1", Severity.ERROR, "message1"));
    	resultsModel.addMessage(new DefaultValidationMessage("property1", Severity.INFO, "message2"));
    	resultsModel.addMessage(new DefaultValidationMessage("property2", Severity.ERROR, "message3"));
    	assertEquals("Number of messages should be 3", 3, resultsModel.getMessageCount());
    	assertEquals("Number of messages registered for property1 should be 2", 2, resultsModel.getMessageCount("property1"));
    	assertEquals("Number of messages flagged as INFO should be 1", 1, resultsModel.getMessageCount(Severity.INFO));
    }

    @Test
    public void testAddChildEvents() {
    	DefaultValidationResultsModel childModel = new DefaultValidationResultsModel();

    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.ERROR, "childErrorMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.WARNING, "childWarningMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.INFO, "childInfoMessage1"));
    	vrm.add(childModel);
    	assertEquals("ParentModel adds child with Error.", 1, listener.eventCount());
    	assertEquals("ChildModel has ErrorMessage.", 1, errorsListener.eventCount());
    	assertEquals("ChildModel has WarningMessage.", 1, warnListener.eventCount());
    	assertEquals("ChildModel has InfoMessage.", 1, infoListener.eventCount());
    	assertEquals("ChildModel has ErrorMessage.", Boolean.TRUE, errorsListener.lastEvent().getNewValue());
    	assertEquals("ChildModel has WarningMessage.", Boolean.TRUE, warnListener.lastEvent().getNewValue());
    	assertEquals("ChildModel has InfoMessage.", Boolean.TRUE, infoListener.lastEvent().getNewValue());
    }

    @Test
    public void testChildEvents() {
    	DefaultValidationResultsModel childModel = new DefaultValidationResultsModel();

    	vrm.add(childModel);
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.ERROR, "childErrorMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.WARNING, "childWarningMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.INFO, "childInfoMessage1"));
    	assertEquals("Child added errorMessage, warningMessage and InfoMessage.", 3, listener.eventCount());
    	assertEquals("ChildModel added ErrorMessage.", 1, errorsListener.eventCount());
    	assertEquals("ChildModel added WarningMessage.", 1, warnListener.eventCount());
    	assertEquals("ChildModel added InfoMessage.", 1, infoListener.eventCount());
    	assertEquals("ChildModel added ErrorMessage.", Boolean.TRUE, errorsListener.lastEvent().getNewValue());
    	assertEquals("ChildModel added WarningMessage.", Boolean.TRUE, warnListener.lastEvent().getNewValue());
    	assertEquals("ChildModel added InfoMessage.", Boolean.TRUE, infoListener.lastEvent().getNewValue());
    }

    @Test
    public void testRemoveChildEvents() {
    	DefaultValidationResultsModel childModel = new DefaultValidationResultsModel();

    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.ERROR, "childErrorMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.WARNING, "childWarningMessage1"));
    	childModel.addMessage(new DefaultValidationMessage("childProperty1", Severity.INFO, "childInfoMessage1"));
    	vrm.add(childModel);
    	vrm.remove(childModel);
    	assertEquals("Child removed, revalidate.", 2, listener.eventCount());
    	assertEquals("Child removed, revalidate ErrorMessages.", 2, errorsListener.eventCount());
    	assertEquals("Child removed, revalidate WarningMessages.", 2, warnListener.eventCount());
    	assertEquals("Child removed, revalidate InfoMessages.", 2, infoListener.eventCount());
    	assertEquals("Child removed, revalidate ErrorMessages.", Boolean.FALSE, errorsListener.lastEvent().getNewValue());
    	assertEquals("Child removed, revalidate WarningMessages.", Boolean.FALSE, warnListener.lastEvent().getNewValue());
    	assertEquals("Child removed, revalidate InfoMessages.", Boolean.FALSE, infoListener.lastEvent().getNewValue());
    }

    private ValidationResults getResults(String field, Severity severity) {
        DefaultValidationResults vr = new DefaultValidationResults();
        vr.addMessage(field, severity, "");
        return vr;
    }

    private ValidationResults getResults(String field1, Severity severity1, String field2, Severity severity2) {
        DefaultValidationResults vr = new DefaultValidationResults();
        vr.addMessage(field1, severity1, "");
        vr.addMessage(field2, severity2, "");
        return vr;
    }

    public static class TestValidationListener implements ValidationListener {

        private ValidationResults lastResults;

        private int eventCount = 0;

        public void validationResultsChanged(ValidationResults results) {
            lastResults = results;
            eventCount++;
        }

        public int eventCount() {
            return eventCount;
        }

        public ValidationResults lastResults() {
            return lastResults;
        }
    }
}

