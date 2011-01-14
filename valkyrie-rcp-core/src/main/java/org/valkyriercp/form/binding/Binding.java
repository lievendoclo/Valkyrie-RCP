package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.factory.ControlFactory;

import javax.swing.*;

/**
 * A binding from a visual editing control to a form model's property.
 *
 * @author Oliver Hutchison
 */
public interface Binding extends ControlFactory {

    /**
     * Returns the form model that this binding is for.
     */
    FormModel getFormModel();

    /**
     * Returns the property that this binding is for.
     */
    String getProperty();

    /**
     * Returns the visual control that is bound to the form model's property.
     */
    JComponent getControl();
}