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
package org.valkyriercp.form.binding.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class MaxLengthDocumentFactory implements DocumentFactory {
    private int maxLength;

    public MaxLengthDocumentFactory(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Document createDocument() {
        return new MaxLengthDocument(maxLength);
    }
    
    public class MaxLengthDocument extends PlainDocument {
        private int maxLength;

        public MaxLengthDocument(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(int i, String s, AttributeSet attributeSet) throws BadLocationException {
            if(getContent().length() <= maxLength) {
                super.insertString(i, s, attributeSet);
            }
        }
    }
}
