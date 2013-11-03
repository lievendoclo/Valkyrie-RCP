package org.valkyriercp.list;

import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides AutoCompletion to a combobox. Works with the editor of the JComboBox
 * to make the conversion between strings and the objects of the JComboBox
 * model. <br>
 * Based on code contributed to the public domain by Thomas Bierhance
 * (http://www.orbital-computer.de/JComboBox/)
 *
 * @author Peter De Bruycker
 * @author Thomas Bierhance
 */
public class ComboBoxAutoCompletion extends PlainDocument {

    private final ChangeHandler changeHandler = new ChangeHandler();

    private final JComboBox comboBox;

    private final JTextComponent editor;

    boolean hitBackspace;

    boolean hitBackspaceOnSelection;

    private Map item2string = new HashMap();

    private ComboBoxModel model;

    private boolean selectingValue;

    /**
     * Adds autocompletion support to the given <code>JComboBox</code>.
     *
     * @param comboBox
     *            the combobox
     */
    public ComboBoxAutoCompletion(final JComboBox comboBox) {
        Assert.notNull(comboBox, "The ComboBox cannot be null.");
        Assert.isTrue(!comboBox.isEditable(), "The ComboBox must not be editable.");
        Assert.isTrue(comboBox.getEditor().getEditorComponent() instanceof JTextComponent,
                "Only ComboBoxes with JTextComponent as editor are supported.");

        this.comboBox = comboBox;
        comboBox.setEditable(true);

        model = comboBox.getModel();
        model.addListDataListener(changeHandler);

        editor = (JTextComponent)comboBox.getEditor().getEditorComponent();
        editor.setDocument(this);
        editor.addFocusListener(changeHandler);
        editor.addKeyListener(changeHandler);

        fillItem2StringMap();

        // Handle initially selected object
        Object selected = comboBox.getSelectedItem();
        comboBox.getEditor().setItem(selected);
    }

    private void fillItem2StringMap() {
        editor.setDocument(new PlainDocument());

        item2string.clear();

        JTextComponent editor = (JTextComponent)comboBox.getEditor().getEditorComponent();

        // get current item of editor
        Object currentItem = comboBox.getEditor().getItem();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            Object item = comboBox.getItemAt(i);
            comboBox.getEditor().setItem(item);
            item2string.put(item, editor.getText());
        }
        // reset item in editor
        comboBox.getEditor().setItem(currentItem);

        editor.setDocument(this);
    }

    private String getStringFor(Object item) {
        return (String)item2string.get(item);
    }

    private void highlightCompletedText(int start) {
        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(start);
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String,
     *      javax.swing.text.AttributeSet)
     */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // ignore empty insert
        if (str == null || str.length() == 0)
            return;
        if(selectingValue)
            return;
        // check offset position
        if (offs < 0 || offs > getLength())
            throw new BadLocationException("Invalid offset - must be >= 0 and <= " + getLength(), offs);

        // construct the resulting string
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs, currentText.length());
        String futureText = beforeOffset + str + afterOffset;

        // lookup and select a matching item
        Object item = lookupItem(futureText);
        if (item != null) {
            selectingValue = true;
            try {
                comboBox.setSelectedItem(item);
            } finally {
                selectingValue = false;
            }
        }
        else {
            // keep old item selected if there is no match
            item = comboBox.getSelectedItem();
            // imitate no insert (later on offs will be incremented by
            // str.length(): selection won't move forward)
            offs = offs - str.length();
            // provide feedback to the user that his input has been received but
            // can not be accepted
            // comboBox.getToolkit().beep();
            // when available use:
            UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
        }

        // display the completed string
        String itemString = item == null ? "" : getStringFor(item);
        setText(itemString);

        // if the user selects an item via mouse the the whole string will be
        // inserted.
        // highlight the entire text if this happens.
        if (itemString != null) {
            if (itemString.equals(str) && offs == 0) {
                highlightCompletedText(0);
            }
            else {
                highlightCompletedText(offs + str.length());
                // show popup when the user types
                if (comboBox.isShowing()) {
                    comboBox.setPopupVisible(true);
                }
            }
        }
    }

    private Object lookupItem(String pattern) {
        Object selectedItem = model.getSelectedItem();
        // only search for a different item if the currently selected does not
        // match
        if (selectedItem != null && startsWithIgnoreCase(getStringFor(selectedItem), pattern)) {
            return selectedItem;
        }

        // iterate over all items
        for (int i = 0, n = model.getSize(); i < n; i++) {
            Object currentItem = model.getElementAt(i);
            // current item starts with the pattern?
            if (startsWithIgnoreCase(getStringFor(currentItem), pattern)) {
                return currentItem;
            }
        }

        // no item starts with the pattern => return null
        return null;
    }

    /**
     * @see javax.swing.text.Document#remove(int, int)
     */
    public void remove(int offs, int length) throws BadLocationException {
        // ignore no deletion
        if (length == 0)
            return;
        // check positions
        if (offs < 0 || offs > getLength() || length < 0 || (offs + length) > getLength())
            throw new BadLocationException("Invalid parameters.", offs);

        if (hitBackspace) {
            // user hit backspace => move the selection backwards
            // old item keeps being selected
            if (offs > 0) {
                if (hitBackspaceOnSelection)
                    offs--;
            }
            else {
                // User hit backspace with the cursor positioned on the start =>
                // beep
                comboBox.getToolkit().beep();
                // when available use:
                // UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
            }
            highlightCompletedText(offs);
            // show popup when the user types
            if (comboBox.isShowing())
                comboBox.setPopupVisible(true);
        }
        else {
            super.remove(offs, length);
        }
    }

    private void setText(String text) throws BadLocationException {
        // remove all text and insert the new text
        super.remove(0, getLength());
        super.insertString(0, text, null);
    }

    // checks if str1 starts with str2 - ignores case
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1 != null && str2 != null && str1.toUpperCase().startsWith(str2.toUpperCase());
    }

    private final class ChangeHandler extends KeyAdapter implements FocusListener, ListDataListener {

        // Highlight whole text when user hits enter
        // Register when user hits backspace
        public void keyPressed(KeyEvent e) {
            hitBackspace = false;
            switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                highlightCompletedText(0);
                break;
            // determine if the pressed key is backspace (needed by the remove
            // method)
            case KeyEvent.VK_BACK_SPACE:
                hitBackspace = true;
                hitBackspaceOnSelection = editor.getSelectionStart() != editor.getSelectionEnd();
                break;
            // ignore delete key
            case KeyEvent.VK_DELETE:
                e.consume();
                ComboBoxAutoCompletion.this.comboBox.getToolkit().beep();
                break;
            }
        }

        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when
        // tabbing out
        private boolean hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");

        public void focusGained(FocusEvent e) {
            // Highlight whole text when gaining focus
            highlightCompletedText(0);
        }

        public void focusLost(FocusEvent e) {
            // Workaround for Bug 5100422 - Hide Popup on focus loss
            if (hidePopupOnFocusLoss)
                ComboBoxAutoCompletion.this.comboBox.setPopupVisible(false);
        }

        public void contentsChanged(ListDataEvent e) {
            if(!selectingValue)
                fillItem2StringMap();
        }

        public void intervalAdded(ListDataEvent e) {
            fillItem2StringMap();
        }

        public void intervalRemoved(ListDataEvent e) {
            fillItem2StringMap();
        }
    }
}