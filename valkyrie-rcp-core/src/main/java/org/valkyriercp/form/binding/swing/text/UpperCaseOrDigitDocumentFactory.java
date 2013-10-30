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
