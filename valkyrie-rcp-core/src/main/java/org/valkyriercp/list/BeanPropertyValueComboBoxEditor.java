package org.valkyriercp.list;

import org.springframework.beans.BeanWrapperImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Geoffrey De Smet
 */
public class BeanPropertyValueComboBoxEditor implements ComboBoxEditor {

    private final BeanWrapperImpl beanWrapper = new BeanWrapperImpl();

    private Object current;

    private final ComboBoxEditor innerEditor;

    private final String renderedProperty;

    /**
     * Constructs a new <code>BeanPropertyValueComboBoxEditor</code>
     * instance. The <code>toString</code> method is used to render
     * the items.
     *
     * @param editor
     *            the <code>ComboBoxEditor</code> to use internally
     */
    public BeanPropertyValueComboBoxEditor(ComboBoxEditor editor) {
        this(editor, null);
    }

    /**
     * Constructs a new <code>BeanPropertyValueComboBoxEditor</code>
     * instance.
     *
     * @param innerEditor
     *            the <code>ComboBoxEditor</code> which is used to render the value of the property
     * @param renderedProperty
     *            the property used to render the items
     */
    public BeanPropertyValueComboBoxEditor(ComboBoxEditor innerEditor, String renderedProperty) {
        this.innerEditor = innerEditor;
        this.renderedProperty = renderedProperty;
    }

    /**
     * Should only be used if the innerEditor will be set later
     *
     * @param renderedProperty
     */
    public BeanPropertyValueComboBoxEditor(String renderedProperty) {
        this(null, renderedProperty);
    }

    /**
     * @see javax.swing.ComboBoxEditor#addActionListener(java.awt.event.ActionListener)
     */
    public void addActionListener(ActionListener l) {
        innerEditor.addActionListener(l);
    }

    /**
     * @see javax.swing.ComboBoxEditor#getEditorComponent()
     */
    public Component getEditorComponent() {
        return innerEditor.getEditorComponent();
    }

    /**
     * @see javax.swing.ComboBoxEditor#getItem()
     */
    public Object getItem() {
        return current;
    }

    /**
     * @see javax.swing.ComboBoxEditor#removeActionListener(java.awt.event.ActionListener)
     */
    public void removeActionListener(ActionListener l) {
        innerEditor.removeActionListener(l);
    }

    /**
     * @see javax.swing.ComboBoxEditor#selectAll()
     */
    public void selectAll() {
        innerEditor.selectAll();
    }

    /**
     * @see javax.swing.ComboBoxEditor#setItem(Object)
     */
    public void setItem(Object item) {
        current = item;
        if (item == null) {
            innerEditor.setItem("");
        } else {
            beanWrapper.setWrappedInstance(item);
            if (renderedProperty != null) {
                innerEditor.setItem(String.valueOf(beanWrapper.getPropertyValue(renderedProperty)));
            } else {
                innerEditor.setItem(String.valueOf(item));
            }
        }
    }

    /**
     * @return the property name
     */
    public String getPropertyName() {
        return this.renderedProperty;
    }
}

