package org.valkyriercp.form;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.HierarchicalFormModel;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.form.support.DefaultFormModel;
import org.valkyriercp.binding.validation.support.RulesValidator;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.rules.RulesSource;

/**
 * This provides a collection of useful functions for working with {@link FormModel}s.
 *
 * @author Keith Donald
 * @author Jim Moore
 */
public class FormModelFactory {

    public ValidatingFormModel createFormModel(Object formObject, String formId) {
        return createFormModel(formObject, true, formId);
    }

    public ValidatingFormModel createUnbufferedFormModel(Object formObject, String formId) {
        return createFormModel(formObject, false, formId);
    }

    public ValidatingFormModel createFormModel(Object formObject, boolean bufferChanges, String formId) {
        DefaultFormModel formModel = new DefaultFormModel(formObject, bufferChanges);
        formModel.setId(formId);
        return formModel;
    }

    public ValidatingFormModel createFormModel(Object formObject, boolean bufferChanges, RulesSource rulesSource,
                                                      String formId) {
        DefaultFormModel formModel = new DefaultFormModel(formObject, bufferChanges);
        formModel.setId(formId);
        formModel.setValidator(new RulesValidator(formModel, rulesSource));
        return formModel;
    }

    public ValidatingFormModel createCompoundFormModel(Object formObject, String formId) {
        DefaultFormModel model = new DefaultFormModel(formObject);
        model.setId(formId);
        return model;
    }

    public ValidatingFormModel createFormModel(ValueModel formObjectHolder) {
        return createFormModel(formObjectHolder, true, null);
    }

    public ValidatingFormModel createFormModel(ValueModel formObjectHolder, String formId) {
        return createFormModel(formObjectHolder, true, formId);
    }

    public ValidatingFormModel createUnbufferedFormModel(ValueModel formObjectHolder, String formId) {
        return createFormModel(formObjectHolder, false, formId);
    }

    public ValidatingFormModel createFormModel(ValueModel formObjectHolder, boolean bufferChanges,
                                                      String formId) {
        DefaultFormModel formModel = new DefaultFormModel(formObjectHolder, bufferChanges);
        formModel.setId(formId);
        return formModel;
    }

    public ValidatingFormModel createCompoundFormModel(ValueModel formObjectHolder, String formId) {
        DefaultFormModel model = new DefaultFormModel(formObjectHolder);
        model.setId(formId);
        return model;
    }

    public ValidatingFormModel createFormModel(Object formObject) {
        return createFormModel(formObject, true);
    }

    public ValidatingFormModel createUnbufferedFormModel(Object formObject) {
        return createFormModel(formObject, false);
    }

    public ValidatingFormModel createFormModel(Object formObject, boolean bufferChanges) {
        return createFormModel(formObject, bufferChanges, null);
    }

    public HierarchicalFormModel createCompoundFormModel(Object formObject) {
        return createCompoundFormModel(formObject, null);
    }

    public FormModel createChildPageFormModel(HierarchicalFormModel parentModel) {
        return createChildPageFormModel(parentModel, null);
    }

    public ValidatingFormModel createChildPageFormModel(HierarchicalFormModel parentModel,
                                                               String childPageName) {
        ValidatingFormModel child = createFormModel(parentModel.getFormObjectHolder());
        child.setId(childPageName);
        parentModel.addChild(child);
        return child;
    }

    /**
     * Create a child form model nested by this form model identified by the
     * provided name. The form object associated with the created child model is
     * the value model at the specified parent property path.
     *
     * @param parentModel                 the model to create the FormModelFactory in
     * @param childPageName               the name to associate the created FormModelFactory
     *                                    with in the groupingModel
     * @param childFormObjectPropertyPath the path into the groupingModel that
     *                                    the FormModelFactory is for
     *
     * @return The child form model
     */
    public ValidatingFormModel createChildPageFormModel(HierarchicalFormModel parentModel, String childPageName,
                                                               String childFormObjectPropertyPath) {
        final ValueModel childValueModel = parentModel.getValueModel(childFormObjectPropertyPath);
        return createChildPageFormModel(parentModel, childPageName, childValueModel);
    }

    public ValidatingFormModel createChildPageFormModel(HierarchicalFormModel parentModel, String childPageName,
                                                               ValueModel childFormObjectHolder) {
        ValidatingFormModel child = createFormModel(childFormObjectHolder);
        child.setId(childPageName);
        parentModel.addChild(child);
        return child;
    }

    public ValidationResultsReporter createSingleLineResultsReporter(ValidatingFormModel formModel,
                                                                     Messagable messageReceiver) {
        return new SimpleValidationResultsReporter(formModel.getValidationResults(), messageReceiver);
    }


    /**
     * Returns the child of the formModel with the given page name.
     *
     * @param formModel     the parent model to get the child from
     * @param childPageName the name of the child to retrieve
     *
     * @return null the child can not be found
     *
     * @throws IllegalArgumentException if childPageName or formModel are null
     */
    public FormModel getChild(HierarchicalFormModel formModel, String childPageName) {
        if (childPageName == null) throw new IllegalArgumentException("childPageName == null");
        if (formModel == null) throw new IllegalArgumentException("formModel == null");

        final FormModel[] children = formModel.getChildren();

        if (children == null) return null;

        for (int i = 0; i < children.length; i++) {
            final FormModel child = children[i];
            if (childPageName.equals(child.getId())) return child;
        }

        return null;
    }

}
