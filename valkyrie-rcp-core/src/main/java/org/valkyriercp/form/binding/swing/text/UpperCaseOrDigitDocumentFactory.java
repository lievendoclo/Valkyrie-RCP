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

public class UpperCaseOrDigitDocumentFactory implements DocumentFactory {
    @Override
    public Document createDocument() {
        return new UpperCaseLetterOrDigitDocument();
    }

    public class UpperCaseLetterOrDigitDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            StringBuilder strippedStr = new StringBuilder();
            char[] strArray = str.toCharArray();
            for (int i = 0; i < strArray.length; ++i) {           
                if (Character.isLetterOrDigit(strArray[i]))
                    strippedStr.append(strArray[i]);
            }
            if (strippedStr.length() > 0) {
                str = str.toUpperCase();
                super.insertString(offs, str, a);
            }
        }
    }
}
