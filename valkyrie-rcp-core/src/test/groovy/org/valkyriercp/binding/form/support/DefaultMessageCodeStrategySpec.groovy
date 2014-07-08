package org.valkyriercp.binding.form.support

import spock.lang.Specification

class DefaultMessageCodeStrategySpec extends Specification {
    private DefaultMessageCodeStrategy strategy;

    public void setup() throws Exception {
        strategy = new DefaultMessageCodeStrategy();
    }

    public void teardown() throws Exception {
        strategy = null;
    }

    def testGetMessageCodesEmptyArgs() {
        given:
        def emptyValues = []
        expect:
        emptyValues == strategy.getMessageCodes(null, null, null)
        emptyValues == strategy.getMessageCodes(null, "", null)
        emptyValues == strategy.getMessageCodes("", "", null)
        emptyValues == strategy.getMessageCodes("", "", [] as String[])
        emptyValues == strategy.getMessageCodes("", "", [""] as String[])
        emptyValues == strategy.getMessageCodes("", "", ["", ""] as String[])
    }

    def testGetMessageCodesNullContextNullSuffixes() {
        expect:
        ["simpleField"] == strategy.getMessageCodes(null, "simpleField", null)
        ["fieldbase.simpleField", "simpleField"] == strategy.getMessageCodes(null, "fieldbase.simpleField", null)
    }

    def testGetMessageCodesWithContext() {
        expect:
        ["context.fieldbase.simpleField", "context.simpleField", "fieldbase.simpleField",
            "simpleField"] == strategy.getMessageCodes("context", "fieldbase.simpleField", null)
    }

    def testGetMessageCodesWithSuffix() {
        expect:
        ["simpleField.suffix"] == strategy.getMessageCodes(null, "simpleField",
                ["suffix"] as String[])
        ["simpleField.suffix1", "simpleField"] == strategy.getMessageCodes(null,
                "simpleField", ["suffix1", ""] as String[])
        ["simpleField.suffix1", "simpleField.suffix2"] == strategy.getMessageCodes(null,
                "simpleField", ["suffix1", "suffix2" ] as String[])
    }


    def testGetMessageCodesWithContextAndSuffix() {
        expect:
        ["context.fieldbase.simpleField.suffix", "context.simpleField.suffix",
            "fieldbase.simpleField.suffix", "simpleField.suffix"] == strategy.getMessageCodes("context",
                "fieldbase.simpleField", ["suffix"] as String[])
    }
}