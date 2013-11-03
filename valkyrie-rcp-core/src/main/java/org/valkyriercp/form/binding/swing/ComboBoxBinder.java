package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.rules.closure.Closure;

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class ComboBoxBinder extends AbstractListBinder {
    public static final String RENDERER_KEY = "renderer";

    public static final String EDITOR_KEY = "editor";

    /**
     * context key for a value which is used to mark an empty Selection. If this value is selected null will be assigned
     * to the fields value
     */
    public static final String EMPTY_SELECTION_VALUE = "emptySelectionValue";

    private Object renderer;

    private Object editor;

    private Object emptySelectionValue;

    public ComboBoxBinder() {
        this(null, new String[] { SELECTABLE_ITEMS_KEY, COMPARATOR_KEY, RENDERER_KEY, EDITOR_KEY, FILTER_KEY,
                EMPTY_SELECTION_VALUE });
    }

    public ComboBoxBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    public ComboBoxBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        Assert.isInstanceOf(JComboBox.class, control, formPropertyPath);
        return new ComboBoxBinding((JComboBox) control, formModel, formPropertyPath, getRequiredSourceClass());
    }

    protected void applyContext(AbstractListBinding binding, Map context) {
        super.applyContext(binding, context);
        ComboBoxBinding comboBoxBinding = (ComboBoxBinding) binding;
        if (context.containsKey(RENDERER_KEY)) {
            comboBoxBinding.setRenderer((ListCellRenderer) decorate(context.get(RENDERER_KEY), comboBoxBinding
                    .getRenderer()));
        } else if (renderer != null) {
            comboBoxBinding.setRenderer((ListCellRenderer) decorate(renderer, comboBoxBinding.getRenderer()));
        }
        if (context.containsKey(EDITOR_KEY)) {
            comboBoxBinding.setEditor((ComboBoxEditor) decorate(context.get(EDITOR_KEY), comboBoxBinding.getEditor()));
        } else if (editor != null) {
            comboBoxBinding.setEditor((ComboBoxEditor) decorate(editor, comboBoxBinding.getEditor()));
        }
        if (context.containsKey(EMPTY_SELECTION_VALUE)) {
            comboBoxBinding.setEmptySelectionValue(context.get(EMPTY_SELECTION_VALUE));
        } else if (emptySelectionValue != null) {
            comboBoxBinding.setEmptySelectionValue(emptySelectionValue);
        }
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createComboBox();
    }

    public void setRenderer(ListCellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Defines a closure which is called to create the renderer. The argument for the closure will be the default
     * renderer (see {@link JComboBox#getRenderer()} of the combobox. The closure must create an instance of
     * {@link ListCellRenderer}
     *
     * @param rendererClosure
     *            the closure which is used to create the renderer
     */
    public void setRendererClosure(Closure rendererClosure) {
        this.renderer = rendererClosure;
    }

    public void setEditor(ComboBoxEditor editor) {
        this.editor = editor;
    }

    /**
     * Defines a closure which is called to create the editor. The argument for the closure will be the default editor
     * (see {@link JComboBox#getEditor()} of the combobox. The closure must create an instance of {@link ComboBoxEditor}
     *
     * @param editorClosure
     *            the closure which is used to create the editor
     */
    public void setEditorClosure(Closure editorClosure) {
        this.editor = editorClosure;
    }

    public Object getEmptySelectionValue() {
        return emptySelectionValue;
    }

    public void setEmptySelectionValue(Object emptySelectionValue) {
        this.emptySelectionValue = emptySelectionValue;
    }
}
