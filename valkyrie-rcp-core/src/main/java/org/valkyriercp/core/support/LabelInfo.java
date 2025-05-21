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
package org.valkyriercp.core.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.swing.*;

/**
 * An immutable parameter object consisting of the text, mnemonic character and mnemonic character
 * index that may be associated with a labeled component. This class also acts as a factory for
 * creating instances of itself based on a string descriptor that adheres to some simple syntax
 * rules as described in the javadoc for the {@link #valueOf(String)} method.
 *
 * <p>
 * The syntax used for the label info descriptor is just the text to be displayed by the label with
 * an ampersand (&) optionally inserted before the character that is to be used as a
 * mnemonic for the label.
 * </p>
 *
 * <p>
 * Example: To create a label with the text {@code My Label} and the capital L as a mnemonic,
 * use the following descriptor:
 * </p>
 *
 * <pre>
 *     <code>My &Label</code>
 * </pre>
 *
 * <p>
 * A backslash character (\) can be used to escape ampersand characters that are to be displayed as
 * part of the label's text. For example:
 * </p>
 *
 * <pre>
 *     <code>Save \& Run</code>
 * </pre>
 *
 * <p>
 * Only one non-escaped backslash can appear in the label descriptor. Attempting to specify more
 * than one mnemonic character will result in an exception being thrown.
 * TODO finish comment regarding backslash chars in props file
 * Note that for label descriptors provided in properties files, an extra backslash will be required
 * to avoid the single backslash being interpreted as a special character.
 * </p>
 *
 * @author Keith Donald
 * @author Peter De Bruycker
 * @author Kevin Stembridge
 */
public final class LabelInfo {

    private static final Log logger = LogFactory.getLog(LabelInfo.class);

    private static final LabelInfo BLANK_LABEL_INFO = new LabelInfo("");

    private static final int DEFAULT_MNEMONIC = 0;

    private static final int DEFAULT_MNEMONIC_INDEX = -1;

    private final String text;

    private final int mnemonic;

    private final int mnemonicIndex;

    /**
     * Creates a new {@code LabelInfo} instance by parsing the given label descriptor to determine
     * the label's text and mnemonic character. The syntax rules for the descriptor are as follows:
     *
     * <ul>
     * <li>The descriptor may be null or an empty string, in which case, an instance with no text
     * or mnemonic will be returned.</li>
     * <li>The mnemonic character is indicated by a preceding ampersand (&).</li>
     * <li>A backslash character (\) can be used to escape ampersand characters that are to be
     * displayed as part of the label's text.</li>
     * <li>A double backslash (a backslash escaped by a backslash) indicates that a single backslash
     * is to appear in the label's text.</li>
     * <li>Only one non-escaped ampersand can appear in the descriptor.</li>
     * <li>A space character cannot be specified as the mnemonic character.</li>
     * </ul>
     *
     * @param labelDescriptor The label descriptor. The text may be null or empty, in which case a
     * blank {@code LabelInfo} instance will be returned.
     *
     * @return A {@code LabelInfo} instance that is described by the given descriptor.
     * Never returns null.
     *
     * @throws IllegalArgumentException if {@code labelDescriptor} violates any of the syntax rules
     * described above.
     */
    public static LabelInfo valueOf(final String labelDescriptor) {

        if (logger.isDebugEnabled()) {
            logger.debug("Creating a new LabelInfo from label descriptor [" + labelDescriptor + "]");
        }

        if (!StringUtils.hasText(labelDescriptor)) {
            return BLANK_LABEL_INFO;
        }

        StringBuffer labelText = new StringBuffer();
        char mnemonicChar = '\0';
        int mnemonicCharIndex = DEFAULT_MNEMONIC_INDEX;
        char currentChar;

        for (int i = 0; i < labelDescriptor.length();) {
            currentChar = labelDescriptor.charAt(i);
            int nextCharIndex = i + 1;

            if (currentChar == '\\') {
                //confirm that the next char is a valid escaped char, add the next char to the
                //stringbuffer then skip ahead 2 chars.
                checkForValidEscapedCharacter(nextCharIndex, labelDescriptor);
                labelText.append(labelDescriptor.charAt(nextCharIndex));
                i++;
                i++;
            }
            else if (currentChar == '&') {
                //we've found a mnemonic indicator, so...

                //confirm that we haven't already found one, ...
                if (mnemonicChar != '\0') {
                    throw new IllegalArgumentException(
                            "The label descriptor ["
                            + labelDescriptor
                            + "] can only contain one non-escaped ampersand.");
                }

                //...that it isn't the last character, ...
                if (nextCharIndex >= labelDescriptor.length()) {
                    throw new IllegalArgumentException(
                            "The label descriptor ["
                            + labelDescriptor
                            + "] cannot have a non-escaped ampersand as its last character.");
                }

                //...and that the character that it prefixes is a valid mnemonic character.
                mnemonicChar = labelDescriptor.charAt(nextCharIndex);
                checkForValidMnemonicChar(mnemonicChar, labelDescriptor);

                //...add it to the stringbuffer and set the mnemonic index to the position of
                //the newly added char, then skip ahead 2 characters
                labelText.append(mnemonicChar);
                mnemonicCharIndex = labelText.length() - 1;
                i++;
                i++;

            }
            else {
                labelText.append(currentChar);
                i++;
            }

        }

        // mnemonics work with VK_XXX (see KeyEvent) and only uppercase letters are used as event
        return new LabelInfo(labelText.toString(), Character.toUpperCase(mnemonicChar), mnemonicCharIndex);

    }

    /**
     * Confirms that the character at the specified index within the given label descriptor is
     * a valid 'escapable' character. i.e. either an ampersand or backslash.
     *
     * @param index The position within the label descriptor of the character to be checked.
     * @param labelDescriptor The label descriptor.
     *
     * @throws NullPointerException if {@code labelDescriptor} is null.
     * @throws IllegalArgumentException if the given {@code index} position is beyond the length
     * of the string or if the character at that position is not an ampersand or backslash.
     */
    private static void checkForValidEscapedCharacter(int index, String labelDescriptor) {

        if (index >= labelDescriptor.length()) {
            throw new IllegalArgumentException(
                    "The label descriptor contains an invalid escape sequence. Backslash "
                    + "characters (\\) must be followed by either an ampersand (&) or another "
                    + "backslash.");
        }

        char escapedChar = labelDescriptor.charAt(index);

        if (escapedChar != '&' && escapedChar != '\\') {
            throw new IllegalArgumentException(
                    "The label descriptor ["
                    + labelDescriptor
                    + "] contains an invalid escape sequence. Backslash "
                    + "characters (\\) must be followed by either an ampersand (&) or another "
                    + "backslash.");
        }

    }

    /**
     * Confirms that the given character is allowed to be used as a mnemonic. Currently, only
     * spaces are disallowed.
     *
     * @param mnemonicChar The mnemonic character.
     * @param labelDescriptor The label descriptor.
     */
    private static void checkForValidMnemonicChar(char mnemonicChar, String labelDescriptor) {

        if (mnemonicChar == ' ') {
            throw new IllegalArgumentException(
                    "The mnemonic character cannot be a space. ["
                    + labelDescriptor
                    + "]");
        }

    }

    /**
     * Creates a new {@code LabelInfo} with the given text and no specified mnemonic.
     *
     * @param text The text to be displayed by the label. This may be an empty string but
     * cannot be null.
     *
     * @throws IllegalArgumentException if {@code text} is null.
     */
    public LabelInfo(String text) {
        this(text, DEFAULT_MNEMONIC, DEFAULT_MNEMONIC_INDEX);
    }

    /**
     * Creates a new {@code LabelInfo} with the given text and mnemonic character.
     *
     * @param text The text to be displayed by the label. This may be an empty string but cannot
     * be null.
     * @param mnemonic The character from the label text that acts as a mnemonic.
     *
     * @throws IllegalArgumentException if {@code text} is null or if {@code mnemonic} is a
     * negative value.
     */
    public LabelInfo(String text, int mnemonic) {
        this(text, mnemonic, DEFAULT_MNEMONIC_INDEX);
    }

    /**
     * Creates a new {@code LabelInfo} with the given text, mnemonic character and mnemonic index.
     *
     * @param text The text to be displayed by the label. This may be an empty string but cannot
     * be null.
     * @param mnemonic The character from the label text that acts as a mnemonic.
     * @param mnemonicIndex The zero-based index of the mnemonic character within the label text.
     * If the specified label text is an empty string, this property will be ignored and set to -1.
     *
     * @throws IllegalArgumentException if {@code text} is null, if {@code mnemonic} is a negative
     * value, if {@code mnemonicIndex} is less than -1 or if {@code mnemonicIndex} is outside the
     * length of {@code text}.
     */
    public LabelInfo(String text, int mnemonic, int mnemonicIndex) {

        Assert.notNull(text, "text");
        Assert.isTrue(mnemonic >= 0, "mnemonic must be greater than or equal to 0");
        Assert.isTrue(mnemonicIndex >= -1, "mnemonicIndex must be greater than or equal to -1");

        Assert.isTrue(mnemonicIndex < text.length(),
                      "The mnemonic index must be less than the text length; mnemonicIndex = "
                      + mnemonicIndex
                      + ", text length = "
                      + text.length());

        this.text = text;

        if (!StringUtils.hasText(text)) {
            mnemonicIndex = DEFAULT_MNEMONIC_INDEX;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Constructing a new LabelInfo instance with properties: text='"
                         + text
                         + "', mnemonic="
                         + mnemonic
                         + ", mnemonicIndex="
                         + mnemonicIndex);
        }

        this.mnemonic = mnemonic;
        this.mnemonicIndex = mnemonicIndex;

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + this.mnemonic;
        result = PRIME * result + this.mnemonicIndex;
        result = PRIME * result + ((this.text == null) ? 0 : this.text.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LabelInfo other = (LabelInfo) obj;
        if (this.mnemonic != other.mnemonic)
            return false;
        if (this.mnemonicIndex != other.mnemonicIndex)
            return false;
        if (this.text == null) {
            if (other.text != null)
                return false;
        } else if (!this.text.equals(other.text))
            return false;
        return true;
    }

    /**
     * Configures the given label with the parameters from this instance.
     *
     * @param label The label that is to be configured.
     * @throws IllegalArgumentException if {@code label} is null.
     */
    public void configureLabel(JLabel label) {

        Assert.notNull(label, "label");

        label.setText(this.text);
        label.setDisplayedMnemonic(getMnemonic());

        if (getMnemonicIndex() >= -1) {
            label.setDisplayedMnemonicIndex(getMnemonicIndex());
        }

    }

    /**
     * Configures the given label with the property values described by this instance and then sets
     * it as the label for the given component.
     *
     * @param label The label to be configured.
     * @param component The component that the label is 'for'.
     *
     * @throws IllegalArgumentException if either argument is null.
     *
     * @see JLabel#setLabelFor(java.awt.Component)
     */
    public void configureLabelFor(JLabel label, JComponent component) {

        Assert.notNull(label, "label");
        Assert.notNull(component, "component");

        configureLabel(label);

        if (!(component instanceof JPanel)) {
            String labelText = label.getText();

            if (!labelText.endsWith(":")) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Appending colon to text field label text '" + this.text + "'");
                }

                label.setText(labelText + ":");
            }

        }

        label.setLabelFor(component);

    }

    /**
     * Configures the given button with the properties held in this instance. Note that this
     * instance doesn't hold any keystroke accelerator information.
     *
     * @param button The button to be configured.
     *
     * @throws IllegalArgumentException if {@code button} is null.
     */
    public void configureButton(AbstractButton button) {
        Assert.notNull(button, "button should not be null");
        button.setText(this.text);
        button.setMnemonic(getMnemonic());
        button.setDisplayedMnemonicIndex(getMnemonicIndex());
    }

    /**
     * Returns the text to be displayed by the label.
     * @return The label text, possibly an empty string but never null.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the character that is to be treated as the mnemonic character for the label.
     *
     * @return The mnemonic character.
     */
    public int getMnemonic() {
        return this.mnemonic;
    }

    /**
     * Returns the index within the label text of the mnemonic character.
     * @return The index of the mnemonic character, or -1 if no mnemonic index is specified.
     */
    public int getMnemonicIndex() {
        return this.mnemonicIndex;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringCreator(this)
                .append("text", this.text)
                .append("mnemonic", this.mnemonic)
                .append("mnemonicIndex", this.mnemonicIndex)
                .toString();
    }

}
