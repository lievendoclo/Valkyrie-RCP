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
package org.valkyriercp.command.config;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.valkyriercp.core.ButtonConfigurer;
import org.valkyriercp.core.support.LabelInfo;

import javax.swing.*;

/**
 * An immutable parameter object consisting of the text, mnemonic character, mnemonic character
 * index and keystroke accelerator that may be associated with a labeled command button.
 *
 * <p>
 * This class also acts as a factory for creating instances of itself based on a string descriptor.
 * The syntax used for this descriptor is described in the javadoc for the {@link #valueOf(String)}
 * method.
 *
 *
 * @author Keith Donald
 * @author Kevin Stembridge
 *
 * @see LabelInfo
 * @see javax.swing.KeyStroke
 */
public final class CommandButtonLabelInfo implements ButtonConfigurer {

    /** A default instance to be used for command buttons with no label information. */
    public static final CommandButtonLabelInfo BLANK_BUTTON_LABEL = new CommandButtonLabelInfo("");

    private final LabelInfo labelInfo;

    private final KeyStroke accelerator;

    /**
     * Return an instance of this class, created by parsing the information in the given label
     * descriptor string. The expected format of this descriptor is the same as that used by
     * the {@link LabelInfo} class, with the following additions:
     *
     * <ul>
     * <li>The @ symbol is an escapable character.</li>
     * <li>Everything after the first unescaped @ symbol will be treated as the textual representation
     * of the keystroke accelerator.</li>
     * </ul>
     *
     * The expected format of the keystroke accelerator string is as described in the javadocs for the
     * {@link KeyStroke#getKeyStroke(String)} method.
     *
     *
     * @param labelDescriptor The label descriptor. May be null or empty, in which case, a default
     * blank label info will be returned.
     * @return A CommandButtonLabelInfo instance, never null.
     *
     * @throws IllegalArgumentException if {@code labelDescriptor} contains invalid syntax.
     *
     * @see LabelInfo
     * @see KeyStroke
     */
    public static CommandButtonLabelInfo valueOf(String labelDescriptor) {

        if (!StringUtils.hasText(labelDescriptor)) {
            return BLANK_BUTTON_LABEL;
        }

        StringBuffer labelInfoBuffer = new StringBuffer();
        char currentChar;
        KeyStroke keyStroke = null;

        for (int i = 0, textCharsIndex = 0; i < labelDescriptor.length(); i++, textCharsIndex++) {
            currentChar = labelDescriptor.charAt(i);

            if (currentChar == '\\') {
                int nextCharIndex = i + 1;
                //if this backslash is escaping an @ symbol, we remove the backslash and
                //continue with the next char
                if (nextCharIndex < labelDescriptor.length()) {
                    char nextChar = labelDescriptor.charAt(nextCharIndex);

                    if (nextChar == '@') {
                        labelInfoBuffer.append(nextChar);
                        i++;
                    } else {
                        labelInfoBuffer.append(currentChar);
                        labelInfoBuffer.append(nextChar);
                        i++;
                    }

                }

            }
            else if (currentChar == '@') {
                //we've found the accelerator indicator

                if (i + 1 == labelDescriptor.length()) {
                    throw new IllegalArgumentException(
                            "The label descriptor ["
                            + labelDescriptor
                            + "] does not specify a valid accelerator after the last "
                            + "non-espaced @ symbol.");
                }

                String acceleratorStr = labelDescriptor.substring(i + 1);
                keyStroke = KeyStroke.getKeyStroke(acceleratorStr);

                if (keyStroke == null) {
                    throw new IllegalArgumentException(
                            "The keystroke accelerator string ["
                            + acceleratorStr
                            + "] from the label descriptor ["
                            + labelDescriptor
                            + "] is not a valid keystroke format.");
                }

                break;

            }
            else {
                labelInfoBuffer.append(currentChar);
            }

        }

        LabelInfo info = LabelInfo.valueOf(labelInfoBuffer.toString());

        return new CommandButtonLabelInfo(info, keyStroke);

    }

    /**
     * Creates a new {@code CommandButtonLabelInfo} that will display the given text on its label.
     * There will be no associated mnemonic character and no keystroke accelerator.
     *
     * @param text The label text to be displayed. Must not be null.
     *
     * @throws IllegalArgumentException if {@code text} is null.
     */
    public CommandButtonLabelInfo(String text) {
        this(new LabelInfo(text), null);
    }

    /**
     * Creates a new {@code CommandButtonLabelInfo} with the given label information and keystroke
     * accelerator.
     *
     * @param labelInfo The label information. Must not be null.
     * @param accelerator The keystroke accelerator. May be null.
     *
     * @throws IllegalArgumentException if {@code labelInfo} is null.
     */
    public CommandButtonLabelInfo(LabelInfo labelInfo, KeyStroke accelerator) {
        Assert.notNull(labelInfo, "labelInfo");
        this.labelInfo = labelInfo;
        this.accelerator = accelerator;
    }

    /**
     * Returns the displayable text.
     *
     * @return The label text. Maybe an empty string but never null.
     */
    public String getText() {
        return this.labelInfo.getText();
    }

    /**
     * Returns the mnemonic for the label.
     *
     * @return The mnemonic for the label.
     */
    public int getMnemonic() {
        return this.labelInfo.getMnemonic();
    }

    /**
     * Returns the zero-based index for the mnemonic character within the label text.
     *
     * @return The mnemonic index or -1 to indicate that there is no associated mnemonic character.
     */
    public int getMnemonicIndex() {
        return this.labelInfo.getMnemonicIndex();
    }

    /**
     * Returns the keystroke accelerator for the label.
     *
     * @return The keystroke accelerator, or null.
     */
    public KeyStroke getAccelerator() {
        return this.accelerator;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return this.labelInfo.hashCode() + (this.accelerator != null ? this.accelerator.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof CommandButtonLabelInfo)) {
            return false;
        }

        CommandButtonLabelInfo other = (CommandButtonLabelInfo)obj;

        if (!this.labelInfo.equals(other.labelInfo)) {
            return false;
        }

        if (!ObjectUtils.nullSafeEquals(this.accelerator, other.accelerator)) {
            return false;
        }

        return true;

    }

    /**
     * Configures an existing button appropriately based on this label info's
     * properties.
     *
     * @param button
     */
    public AbstractButton configure(AbstractButton button) {
        Assert.notNull(button);
        button.setText(this.labelInfo.getText());
        button.setMnemonic(this.labelInfo.getMnemonic());
        button.setDisplayedMnemonicIndex(this.labelInfo.getMnemonicIndex());
        configureAccelerator(button, getAccelerator());
        return button;
    }

    /**
     * Sets the given keystroke accelerator on the given button.
     *
     * @param button The button. May be null.
     * @param keystrokeAccelerator The accelerator. May be null.
     */
    protected void configureAccelerator(AbstractButton button, KeyStroke keystrokeAccelerator) {
        if ((button instanceof JMenuItem) && !(button instanceof JMenu)) {
            ((JMenuItem)button).setAccelerator(keystrokeAccelerator);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringCreator(this)
                .append("labelInfo", this.labelInfo)
                .append("accelerator", this.accelerator)
                .toString();
    }

}

