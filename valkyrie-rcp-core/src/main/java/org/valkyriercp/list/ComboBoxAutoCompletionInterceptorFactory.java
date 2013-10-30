package org.valkyriercp.list;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.swing.*;

/**
 * @author Peter De Bruycker
 */
public class ComboBoxAutoCompletionInterceptorFactory implements FormComponentInterceptorFactory {

    public class ComboBoxAutoCompletionInterceptor extends AbstractFormComponentInterceptor {

        /**
         * Constructs a new <code>AutoCompletionInterceptor</code> instance.
         *
         * @param formModel
         *            the formModel
         */
        public ComboBoxAutoCompletionInterceptor(FormModel formModel) {
            super(formModel);
        }

        public void processComponent(String propertyName, JComponent component) {
            JComponent inner = getInnerComponent(component);
            if (inner instanceof JComboBox ) {
                JComboBox comboBox = (JComboBox) inner;
                if( comboBox.isEditable()) {
                    // It's editable, so install autocompletion for editable comboboxes
                    new EditableComboBoxAutoCompletion(comboBox);
                } else {
                    new ComboBoxAutoCompletion(comboBox);
                }
            }
        }
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new ComboBoxAutoCompletionInterceptor(formModel);
    }
}
