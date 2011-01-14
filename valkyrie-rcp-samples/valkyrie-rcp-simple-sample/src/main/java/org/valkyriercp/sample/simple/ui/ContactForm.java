/*
 * Copyright 2002-2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.valkyriercp.sample.simple.ui;

import com.jgoodies.forms.layout.FormLayout;
import org.valkyriercp.form.FormModelHelper;
import org.valkyriercp.form.binding.swing.NumberBinder;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;
import org.valkyriercp.sample.simple.domain.Contact;
import org.valkyriercp.sample.simple.ui.binder.TodoItemListBinding;
import org.valkyriercp.widget.AbstractFocussableWidgetForm;

import javax.swing.*;
import java.util.HashMap;

public class ContactForm extends AbstractFocussableWidgetForm
{
	public ContactForm(Contact contact) {
		super(FormModelHelper.createFormModel(contact, "contactForm"));
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
        formBuilder.addPropertyAndLabel("dateOfBirth");
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
        JComponent zipField = formBuilder.addPropertyAndLabel("address.zip")[1];
		((JTextField) zipField).setColumns(8);
        formBuilder.nextRow();
		formBuilder.addHorizontalSeparator("Memo", 7);
		formBuilder.nextRow("fill:default:grow");
        formBuilder.addTextArea("memo", 1, formBuilder.getRow(), 7, 1);
         formBuilder.nextRow();
        formBuilder.addHorizontalSeparator("Todo items", 7);
		formBuilder.nextRow("fill:default:grow");
        TodoItemListBinding todoItemListBinding = new TodoItemListBinding(getFormModel(), "todoItems");
        formBuilder.addBinding(todoItemListBinding, 1, formBuilder.getRow(), 7, 1);

        /*
		TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
		formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");

		formBuilder.addSeparator("General");
		formBuilder.row();
		firstNameField = formBuilder.add("firstName")[1];
		formBuilder.add("lastName");
		formBuilder.row();
		formBuilder.add("dateOfBirth", "colSpan=1");
		formBuilder.row();
		formBuilder.add("homePhone");
		formBuilder.add("workPhone");
		formBuilder.row();
		formBuilder.add("emailAddress");
		formBuilder.row();
		formBuilder.row();
		formBuilder.add("contactType", "colSpan=1 align=left");
		formBuilder.row();
		formBuilder.addSeparator("Address");
		formBuilder.row();
		formBuilder.add("address.address1");
		formBuilder.row();
		formBuilder.add("address.address2");
		formBuilder.row();
		formBuilder.add("address.address3");
		formBuilder.row();
		formBuilder.add("address.city", "colSpan=1 align=left");
		formBuilder.row();
		// formBuilder.add(getBindingFactory().createBoundComboBox( "address.state", MasterLists.STATE_CODE), "colSpan=1 align=left" );
		formBuilder.add("address.state", "colSpan=1 align=left");
		formBuilder.row();

		// We want to make the zip code UI field smaller than the default. The add method
		// returns an array of two components, the field label and the component bound to
		// the property.
		JComponent zipField = formBuilder.add("address.zip", "colSpan=1 align=left")[1];
		((JTextField) zipField).setColumns(8);
		formBuilder.row();
		*/

		return formBuilder.getPanel();
	}

}