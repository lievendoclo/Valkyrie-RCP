package org.valkyriercp.binding.form.support;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

import static org.junit.Assert.fail;

/**
 * @author Mathias Broekelmann
 *
 */
public class DefaultMessageCodeStrategyTests {

    private DefaultMessageCodeStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new DefaultMessageCodeStrategy();
    }

    @After
    public void tearDown() throws Exception {
        strategy = null;
    }

    @Test
    public final void testGetMessageCodesEmptyArgs() {
        String[] emptyValues = new String[0];
        assertEquals(emptyValues, strategy.getMessageCodes(null, null, null));
        assertEquals(emptyValues, strategy.getMessageCodes(null, "", null));
        assertEquals(emptyValues, strategy.getMessageCodes("", "", null));
        assertEquals(emptyValues, strategy.getMessageCodes("", "", emptyValues));
        assertEquals(emptyValues, strategy.getMessageCodes("", "", new String[] { "" }));
        assertEquals(emptyValues, strategy.getMessageCodes("", "", new String[] { "", "" }));
    }

    @Test
    public final void testGetMessageCodesNullContextNullSuffixes() {
        assertEquals(new String[] { "simpleField" }, strategy.getMessageCodes(null, "simpleField", null));
        assertEquals(new String[] { "fieldbase.simpleField", "simpleField" }, strategy.getMessageCodes(null,
                "fieldbase.simpleField", null));
    }

    @Test
    public final void testGetMessageCodesWithContext() {
        assertEquals(new String[] { "context.fieldbase.simpleField", "context.simpleField", "fieldbase.simpleField",
                "simpleField" }, strategy.getMessageCodes("context", "fieldbase.simpleField", null));
    }

    @Test
    public final void testGetMessageCodesWithSuffix() {
        assertEquals(new String[] { "simpleField.suffix" }, strategy.getMessageCodes(null, "simpleField",
                new String[] { "suffix" }));
        assertEquals(new String[] { "simpleField.suffix1", "simpleField" }, strategy.getMessageCodes(null,
                "simpleField", new String[] { "suffix1", "" }));
        assertEquals(new String[] { "simpleField.suffix1", "simpleField.suffix2" }, strategy.getMessageCodes(null,
                "simpleField", new String[] { "suffix1", "suffix2" }));
    }

    @Test
    public final void testGetMessageCodesWithContextAndSuffix() {
        assertEquals(new String[] { "context.fieldbase.simpleField.suffix", "context.simpleField.suffix",
                "fieldbase.simpleField.suffix", "simpleField.suffix" }, strategy.getMessageCodes("context",
                "fieldbase.simpleField", new String[] { "suffix" }));
    }

    protected void assertEquals(Object[] expected, Object[] actual) {
        if (!Arrays.equals(expected, actual)) {
            fail(buildMessage(expected, actual));
        }
    }

    private String buildMessage(Object[] expected, Object[] actual) {
        return "expected " + ObjectUtils.nullSafeToString(expected) + ", got " + ObjectUtils.nullSafeToString(actual);
    }

    protected void assertEquals(String message, Object[] expected, Object[] actual) {
        if (!Arrays.equals(expected, actual)) {
            fail(message);
        }
    }
}
