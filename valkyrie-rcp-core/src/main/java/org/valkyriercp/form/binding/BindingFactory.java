package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.util.Map;

/**
 * A BindingFactory creates bindings for a specific FormModel.
 *
 * @author Oliver Hutchison
 */
public interface BindingFactory {

    /**
     * Returns the form model that contains the properties being bound.
     */
    FormModel getFormModel();

    /**
     * Returns a binding for the provided formPropertyPath.
     */
    Binding createBinding(String formPropertyPath);

    /**
     * Returns a binding for the provided formPropertyPath.
     */
    Binding createBinding(String formPropertyPath, Map context);

    /**
     * Returns a binding to a control of type controlType for the provided formPropertyPath
     */
    Binding createBinding(Class controlType, String formPropertyPath);

    /**
     * Returns a binding to a control of type controlType for the provided formPropertyPath
     */
    Binding createBinding(Class controlType, String formPropertyPath, Map context);

    /**
     * Returns a binding between the provided control and the provided formPropertyPath
     */
    Binding bindControl(JComponent control, String formPropertyPath);

    /**
     * Returns a binding between the provided control and the provided formPropertyPath
     */
    Binding bindControl(JComponent control, String formPropertyPath, Map context);
}
