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
import java.util.regex.Pattern;

/**
 * This DocumentFactory will create a Document which holds a {@link java.util.regex.Pattern}. This Pattern is then used
 * to verify each character that is inserted. This makes it possible to eg. allow only alfanumeric. Note
 * that if you need a specific sequence of characters, you probably need a formatted textField with formatter
 * instead.
 *
 * @author Lieven Doclo
 * @author Jan Hoskens
 */
public class RegExDocumentFactory implements DocumentFactory {

    /** Pattern to use in the RegExDocument. */
    private String characterPattern;

    /** Converting to uppercase letters? */
    private boolean convertToUppercase;

    /** Default constructor sets the pattern to use '.'. */
    public RegExDocumentFactory() {
        this(".");
    }

    /**
     * Construct a RegExDocumentFactory with the given pattern.
     *
     * @param characterPattern the pattern to use.
     */
    public RegExDocumentFactory(String characterPattern) {
        this(characterPattern, false);
    }

    /**
     * Construct a RegExDocumentFactory with the given pattern and convert to uppercase if requested.
     *
     * @param characterPattern   the pattern to use.
     * @param convertToUppercase if <code>true</code> convert all letters to uppercase.
     */
    public RegExDocumentFactory(String characterPattern, boolean convertToUppercase) {
        this.characterPattern = characterPattern;
        this.convertToUppercase = convertToUppercase;
    }

    /**
     * Returns the pattern that is used by this RegExDocumentFactory.
     *
     * @return the pattern.
     */
    public String getCharacterPattern() {
        return characterPattern;
    }

    /**
     * Set the pattern to be used by this RegExDocumentFactory.
     *
     * @param characterPattern the pattern.
     */
    public void setCharacterPattern(String characterPattern) {
        this.characterPattern = characterPattern;
    }

    /**
     * Returns <code>true</code> if the Document should convert all lowercase letters to uppercase.
     *
     * @return <code>true</code> if lowercase is converted to uppercase.
     */
    public boolean isConvertToUppercase() {
        return convertToUppercase;
    }

    /**
     * Set to <code>true</code> if all lowercase letters should be converted to uppercase.
     *
     * @param convertToUppercase set to <code>true</code> if conversion should happen.
     */
    public void setConvertToUppercase(boolean convertToUppercase) {
        this.convertToUppercase = convertToUppercase;
    }

    /** {@inheritDoc} */
    public Document createDocument() {
        return new RegExDocument(getCharacterPattern(), isConvertToUppercase());
    }

    private static class RegExDocument extends PlainDocument {

        private final Pattern pattern;

        private final boolean convertToUppercase;

        public RegExDocument(String stringPattern, boolean convertToUppercase) {
            this.pattern = Pattern.compile(stringPattern, Pattern.DOTALL);
            this.convertToUppercase = convertToUppercase;
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            char[] upper = str.toCharArray();
            StringBuilder builder = new StringBuilder();
            char character;
            for (int i = 0; i < upper.length; i++)
            {
                if (convertToUppercase)
                    character = Character.toUpperCase(upper[i]);
                else
                    character = upper[i];
                if (pattern.matcher(Character.toString(character)).find()) {
                    builder.append(character);
                }
            }
            super.insertString(offs, builder.toString(), a);
        }
    }
}