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
package org.valkyriercp.binding.value.swing;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

/**
 * Sets the value of the value model associated with a formatted text field when the text field changes according to the
 * value commit policy.
 *
 * This setter will also update the formatted text field value when the underlying value model value changes.
 *
 * @author Oliver Hutchison
 * @author Keith Donald
 */
public class FormattedTextFieldAdapter extends AbstractValueModelAdapter implements PropertyChangeListener,
        DocumentListener, FocusListener {

    private final JFormattedTextField component;

    private boolean settingValue;

    private boolean ignoreValue;

    public FormattedTextFieldAdapter(JFormattedTextField component, ValueModel valueModel,
            ValueCommitPolicy commitPolicy) {
        super(valueModel);
        this.component = component;
        this.component.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        this.component.addPropertyChangeListener("value", this);
        if (commitPolicy == ValueCommitPolicy.AS_YOU_TYPE) {
            component.getDocument().addDocumentListener(this);
        }

        // mathiasbr: register focus listener to avoid a race condition.
        // If the formatted text field lost its focus the value will be replaced.
        // This results into two events: the first one sets the value to null
        // the second one sets the value to its value before it was set to null.
        // If the focus is moved to the button which is bound to the validation
        // and the field has a constraint (like required) the button will be disabled
        // while it is pressed. Although the button will be enabled when the second
        // event with the previous value is set again but the button will lost its
        // armed and pressed state when it is disabled.
        // The result is that a user has to click the button twice
        // before the button fires the actionPerformed event
        component.addFocusListener(this);

        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object value) {
        settingValue = true;
        try {
            component.setValue(value);
        } finally {
            settingValue = false;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (logger.isDebugEnabled()) {
            Class valueClass = (e.getNewValue() != null ? e.getNewValue().getClass() : null);
            logger.debug("Formatted text field property '" + e.getPropertyName() + "' changed; new value is '"
                    + e.getNewValue() + "', valueClass=" + valueClass);
        }
        adaptedValueChanged(component.getValue());
    }

    public void insertUpdate(DocumentEvent e) {
        tryToCommitEdit();
    }

    public void removeUpdate(DocumentEvent e) {
        if (!ignoreValue)
            tryToCommitEdit();
        else
            ignoreValue = false;
    }

    public void changedUpdate(DocumentEvent e) {
        tryToCommitEdit();
    }

    private void tryToCommitEdit() {
        if (!settingValue) {
            try {
                component.commitEdit();
            } catch (ParseException e) {
                // ignore
            }
        }
    }

    public void focusGained(FocusEvent e) {
        ignoreValue = false;
    }

    public void focusLost(FocusEvent e) {
        ignoreValue = true;
    }
}
