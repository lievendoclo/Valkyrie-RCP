package org.valkyriercp.sample.simple.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.NumberBinder;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.simple.domain.Contact;
import org.valkyriercp.sample.simple.ui.binder.TodoItemListBinding;
import org.valkyriercp.widget.AbstractFocussableWidgetForm;

import javax.swing.*;
import java.util.HashMap;

public class ContactForm extends AbstractFocussableWidgetForm
{
    @Override
    public FormModel createFormModel() {
        return getApplicationConfig().formModelFactory().createFormModel(new Contact(), "contactForm");
    }

    protected JComponent createFormControl()
    {
        FormLayout layout = new FormLayout("right:pref, 4dlu, fill:pref:grow, 6dlu, right:pref, 4dlu, fill:pref:grow", "default");
        FormLayoutFormBuilder formBuilder = new FormLayoutFormBuilder(getBindingFactory(), layout);
        
        formBuilder.setLabelAttributes("r, c");
        formBuilder.addHorizontalSeparator("General", 7);
        formBuilder.nextRow();
        formBuilder.addPropertyAndLabel("lastName");
        setFocusControl(formBuilder.addPropertyAndLabel("firstName", 5)[1]);
        formBuilder.nextRow();

        formBuilder.addPropertyAndLabel("dateOfBirth", "jxDatePickerDateFieldBinder");
//        Map context = Maps.newHashMap();
//        context.put(JXDatePickerDateFieldBinder.DATE_FORMAT, "dd/MM/yyyy");
//        formBuilder.addLabel("dateOfBirth");
//        formBuilder.addBinding(getApplicationConfig().bindingFactoryProvider().getBindingFactory(getFormModel()).createBinding("dateOfBirth", context), 3);
        formBuilder.nextRow();
        formBuilder.addPropertyAndLabel("homePhone");
		formBuilder.addPropertyAndLabel("workPhone", 5);
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("emailAddress");
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("contactType");
		formBuilder.nextRow();
        NumberBinder binder = new NumberBinder();
        binder.setLeftDecoration("€");
        formBuilder.addLabel("monthlyIncome");
        formBuilder.addBinding(binder.bind(getFormModel(), "monthlyIncome", new HashMap()), 3);
		formBuilder.nextRow();
		formBuilder.addHorizontalSeparator("Address", 7);
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("address.address1");
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("address.address2");
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("address.address3");
		formBuilder.nextRow();
		formBuilder.addPropertyAndLabel("address.city");
		formBuilder.nextRow();
		// formBuilder.add(getBindingFactory().createBoundComboBox( "address.state", MasterLists.STATE_CODE), "colSpan=1 align=left" );
		formBuilder.addPropertyAndLabel("address.state");
		formBuilder.nextRow();
        formBuilder.addPropertyAndLabel("address.zip");
        formBuilder.nextRow();
		formBuilder.addHorizontalSeparator("Memo", 7);
		formBuilder.nextRow("fill:default:grow");
        formBuilder.addTextArea("memo", 1, formBuilder.getRow(), 7, 1);
         formBuilder.nextRow();
        formBuilder.addHorizontalSeparator("Todo items", 7);
		formBuilder.nextRow("fill:default:grow");
        TodoItemListBinding todoItemListBinding = new TodoItemListBinding(getFormModel(), "todoItems");
        formBuilder.addBinding(todoItemListBinding, 1, formBuilder.getRow(), 7, 1);

		return formBuilder.getPanel();
	}

}