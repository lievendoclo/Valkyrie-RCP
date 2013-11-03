package org.valkyriercp.binding.validation;

import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.core.Severity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultValidationMessageTests extends AbstractValkyrieTest {

    @Test
    public void testDefaultValidationMessage() {
        ValidationMessage vm = new DefaultValidationMessage("property", Severity.INFO, "message");
        assertEquals("property", vm.getProperty());
        assertEquals(Severity.INFO, vm.getSeverity());
        assertEquals("message", vm.getMessage());
    }

    @Test
    public void testToString() {
        ValidationMessage vm = new DefaultValidationMessage("property", Severity.INFO, "message");
        assertTrue(vm.toString().endsWith("property = \'property\', severity = \'info\', message = \'message\']"));
    }
}

