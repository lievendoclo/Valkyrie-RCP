package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;

/**
 * Strategy interface that encapsulates the selection of a <code>Binder</code>
 * for the provided form model, property name and optionally control type.
 *
 * @author Oliver Hutchison
 */
public interface BinderSelectionStrategy {

    /**
     * Returns a binder for the provided form model and property name.
     * @param formModel the form model which contains the property to be bound
     * @param propertyName the name of the property to be bound
     * @return the <code>Binder</code> (never null). This binder must be capable of
     * generating it's own control to bind to.
     */
    Binder selectBinder(FormModel formModel, String propertyName);

    /**
     * Returns a binder that is capable of binding the provided control type to
     * the provided form model and property name.
     * @param controlType the type of the control to be bound
     * @param formModel the form model which contains the property to be bound
     * @param propertyName the name of the property to be bound
     * @return the <code>Binder</code> (never null). This binder must be capable of
     * binding to a pre-created control that is of the specified <code>controlType</code>.
     */
    Binder selectBinder(Class controlType, FormModel formModel, String propertyName);

    void registerBinderForPropertyName(Class parentObjectType, String propertyName, Binder binder);

    void registerBinderForPropertyType(Class propertyType, Binder binder);

    void registerBinderForControlType(Class controlType, Binder binder);
}
