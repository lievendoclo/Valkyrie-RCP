package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.util.Map;

/**
 * A Binder is responsible for creating a binding between a form model's property and
 * a control that may be used to visualize and/or edit that property.
 *
 * @author Oliver Hutchison
 */
public interface Binder {

    /**
     * Returns a binding between a form model's property and a control that will be
     * created by this Binder.
     * @param formModel the form model that this binding is for
     * @param formModel the property that this binding is for
     * @param context additional context that may be used by this binder.
     * @return a Binding (never null).
     * @throws UnsupportedOperationException if this binder is unable to create its
     * own control
     */
    Binding bind(FormModel formModel, String formPropertyPath, Map context);

    /**
     * Returns a binding between a form model's property and the provided control.
     * @param control the visual control that will be bound to the form model's property.
     * @param formModel the form model that this binding is for
     * @param formModel the property that this binding is for
     * @param context additional context that may be used by this binder
     * @return a Binding (never null).
     * @throws UnsupportedOperationException if this binder is unable to bind the
     * provided control or if this binder is unable to bind a provided control
     */
    Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context);
}
