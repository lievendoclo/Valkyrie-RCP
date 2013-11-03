package org.valkyriercp.binding.validation;

import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.binding.validation.support.DefaultValidationResults;
import org.valkyriercp.core.Severity;

import java.util.Set;

import static org.junit.Assert.*;

public class DefaultValidationResultsTests extends AbstractValkyrieTest {
    private DefaultValidationResults vr = new DefaultValidationResults();

    @Test
    public void testAddAndGetMessages() {
        assertEquals(0, vr.getMessageCount());
        assertEquals(0, vr.getMessageCount(Severity.INFO));
        assertEquals(0, vr.getMessageCount("field1"));
        assertEquals(false, vr.getHasErrors());
        assertEquals(false, vr.getHasWarnings());
        assertEquals(false, vr.getHasInfo());

        vr.addMessage("field1", Severity.INFO, "message");
        assertEquals(1, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.INFO));
        assertEquals(1, vr.getMessageCount("field1"));
        ValidationMessage vm = (ValidationMessage)vr.getMessages().iterator().next();
        assertEquals("field1", vm.getProperty());
        assertEquals(Severity.INFO, vm.getSeverity());
        assertEquals("message", vm.getMessage());
        assertContainsMessage(vm, vr.getMessages("field1"));
        assertContainsMessage(vm, vr.getMessages(Severity.INFO));
        assertEquals(false, vr.getHasErrors());
        assertEquals(false, vr.getHasWarnings());
        assertEquals(true, vr.getHasInfo());

        vm = new DefaultValidationMessage("field2", Severity.WARNING, "message");
        vr.addMessage(vm);
        assertEquals(2, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.WARNING));
        assertEquals(1, vr.getMessageCount("field2"));
        assertContainsMessage(vm, vr.getMessages());
        assertContainsMessage(vm, vr.getMessages("field2"));
        assertContainsMessage(vm, vr.getMessages(Severity.WARNING));
        assertEquals(false, vr.getHasErrors());
        assertEquals(true, vr.getHasWarnings());
        assertEquals(true, vr.getHasInfo());

        vm = new DefaultValidationMessage(ValidationMessage.GLOBAL_PROPERTY, Severity.ERROR, "message");
        vr.addMessage(vm);
        assertEquals(3, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.ERROR));
        assertEquals(1, vr.getMessageCount(ValidationMessage.GLOBAL_PROPERTY));
        assertContainsMessage(vm, vr.getMessages());
        assertContainsMessage(vm, vr.getMessages(ValidationMessage.GLOBAL_PROPERTY));
        assertContainsMessage(vm, vr.getMessages(Severity.ERROR));
        assertEquals(true, vr.getHasErrors());
        assertEquals(true, vr.getHasWarnings());
        assertEquals(true, vr.getHasInfo());

        vm = new DefaultValidationMessage("field1", Severity.ERROR, "message");
        vr.addMessage(vm);
        assertEquals(4, vr.getMessageCount());
        assertEquals(2, vr.getMessageCount(Severity.ERROR));
        assertEquals(2, vr.getMessageCount("field1"));
        assertContainsMessage(vm, vr.getMessages());
        assertContainsMessage(vm, vr.getMessages("field1"));
        assertContainsMessage(vm, vr.getMessages(Severity.ERROR));

        DefaultValidationResults vr2 = new DefaultValidationResults();
        vm = new DefaultValidationMessage("newField", Severity.INFO, "message");
        vr2.addMessage(vm);
        ValidationMessage vm2 = new DefaultValidationMessage("newField", Severity.ERROR, "message");
        vr2.addMessage(vm2);

        vr.addAllMessages(vr2.getMessages());
        assertEquals(6, vr.getMessageCount());
        assertEquals(3, vr.getMessageCount(Severity.ERROR));
        assertEquals(2, vr.getMessageCount(Severity.INFO));
        assertEquals(2, vr.getMessageCount("newField"));
        assertContainsMessage(vm, vr.getMessages());
        assertContainsMessage(vm2, vr.getMessages());
        assertContainsMessage(vm, vr.getMessages("newField"));
        assertContainsMessage(vm2, vr.getMessages("newField"));
        assertContainsMessage(vm, vr.getMessages(Severity.INFO));
        assertContainsMessage(vm2, vr.getMessages(Severity.ERROR));
    }

    @Test
    public void testCanNotAddSameMessage() {
        ValidationMessage vm = new DefaultValidationMessage("field2", Severity.WARNING, "message");
        vr.addMessage(vm);
        assertEquals(1, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.WARNING));
        assertEquals(1, vr.getMessageCount("field2"));

        vr.addMessage(vm);
        assertEquals(1, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.WARNING));
        assertEquals(1, vr.getMessageCount("field2"));

        vm = new DefaultValidationMessage("field2", Severity.WARNING, "message");
        vr.addMessage(vm);
        assertEquals(1, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.WARNING));
        assertEquals(1, vr.getMessageCount("field2"));

        DefaultValidationResults vr2 = new DefaultValidationResults();
        vr2.addMessage(vm);
        vr.addAllMessages(vr2);
        assertEquals(1, vr.getMessageCount());
        assertEquals(1, vr.getMessageCount(Severity.WARNING));
        assertEquals(1, vr.getMessageCount("field2"));
    }

    private void assertContainsMessage(ValidationMessage vm, Set messages) {
        assertTrue("Set of messages does not contain expected message '" + vm + "'", messages.contains(vm));
    }

    @Test
    public void testReturnedListsAreNotModifiable() {
        vr.addMessage("field1", Severity.ERROR, "what ever!");
        try {
            vr.getMessages().clear();
            fail();
        }
        catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            vr.getMessages("field1").clear();
            fail();
        }
        catch (UnsupportedOperationException e) {
            // expected
        }
        try {
            vr.getMessages(Severity.ERROR).clear();
            fail();
        }
        catch (UnsupportedOperationException e) {
            // expected
        }
    }
}