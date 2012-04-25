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
