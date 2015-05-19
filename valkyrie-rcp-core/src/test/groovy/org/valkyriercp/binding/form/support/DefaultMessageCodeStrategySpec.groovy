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