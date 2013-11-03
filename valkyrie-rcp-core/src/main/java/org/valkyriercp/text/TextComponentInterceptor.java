package org.valkyriercp.text;

import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Abstract base class for <code>FormComponentInterceptor</code>s that work on
 * <code>JTextComponent</code>s.
 *
 * @author Peter De Bruycker
 *
 */
public abstract class TextComponentInterceptor extends AbstractFormComponentInterceptor {

    public final void processComponent( String propertyName, JComponent component ) {
        JTextComponent textComponent = getTextComponent( getInnerComponent( component ) );
        if( textComponent != null ) {
            processComponent( propertyName, textComponent );
        }
    }

    /**
     * Process the text component.
     *
     * @param propertyName the name of the property
     * @param textComponent the text component
     */
    protected abstract void processComponent( String propertyName, JTextComponent textComponent );

    /**
     * Converts the given component to a <code>JTextComponent</code>. This can be a
     * simple cast if the component is already a text component, or an embedded component
     * (for example a JSpinner).
     * <p>
     * This method is protected, and can be overridden when necessary.
     *
     * @param component the component
     * @return a <code>JTextComponent</code>, or <code>null</code>
     */
    protected JTextComponent getTextComponent( JComponent component ) {
        if( component instanceof JTextField ) {
            return (JTextField) component;
        }

        if( component instanceof JSpinner ) {
            JSpinner spinner = (JSpinner) component;
            if( spinner.getEditor() instanceof JSpinner.DefaultEditor ) {
                return ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
            }
            if( spinner.getEditor() instanceof JTextField ) {
                return (JTextField) spinner.getEditor();
            }
        }

        return null;
    }

}

