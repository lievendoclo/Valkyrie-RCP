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
package org.valkyriercp.sample.vldocking.ui;

import org.springframework.util.Assert;
import org.valkyriercp.application.event.LifecycleApplicationEvent;
import org.valkyriercp.dialog.CloseAction;
import org.valkyriercp.dialog.ConfirmationDialog;
import org.valkyriercp.dialog.FormBackedDialogPage;
import org.valkyriercp.dialog.TitledPageApplicationDialog;
import org.valkyriercp.form.Form;
import org.valkyriercp.sample.vldocking.domain.Contact;
import org.valkyriercp.sample.vldocking.domain.ContactDataStore;

/**
 * This is a dialog for editing the properties of a Contact object. It is a simple "form backed" dialog, meaning that
 * the body of the dialog is provided from a "form backed" dialog page. The Ok (finish) button will be wired into the
 * "page complete" state of the dialog page, which in turn gets its state from the automatic validation of the
 * properties on the form.
 * @author Larry Streepy
 * @see FormBackedDialogPage
 * @see ContactForm
 */
public class ContactPropertiesDialog extends TitledPageApplicationDialog {

	/** The form that allows for editing the contact. */
	private Form form;

	/** Are we creating a new Contact or editing an existing one? */
	private boolean creatingNew = false;

	/** The data store holding all our contacts, used to add a new contact. */
	private ContactDataStore dataStore;

	public ContactPropertiesDialog(ContactDataStore dataStore) {
		this(null, dataStore);
	}

	public ContactPropertiesDialog(Contact contact, ContactDataStore dataStore) {
		Assert.notNull(dataStore, "The data store is required to edit a contact");
		if (contact == null) {
			creatingNew = true;
			contact = new Contact();
		}
		setCloseAction(CloseAction.DISPOSE);
		form = new ContactForm();
        form.setFormObject(contact);
		setDialogPage(new FormBackedDialogPage(form));
		this.dataStore = dataStore;

	}

//    @PostConstruct
//    private void postConstruct() {
//       getApplicationConfig().applicationObjectConfigurer().configure(this, "contact");
//    }

	private Contact getEditingContact() {
		return (Contact) form.getFormModel().getFormObject();
	}

	protected void onAboutToShow() {
		if (creatingNew) {
			setTitle(getApplicationConfig().messageResolver().getMessage("contactProperties.new.title"));
		}
		else {
			Contact contact = getEditingContact();
			String title = getApplicationConfig().messageResolver().getMessage("contactProperties.edit.title", new Object[] { contact.getFirstName(),
					contact.getLastName() });
			setTitle(title);
		}
	}

	protected boolean onFinish() {
		// commit any buffered edits to the model
		form.getFormModel().commit();
		// Update the persistent store with the new/modified object.
		String eventType;
		if (creatingNew) {
			eventType = LifecycleApplicationEvent.CREATED;
			dataStore.add(getEditingContact());
		}
		else {
			eventType = LifecycleApplicationEvent.MODIFIED;
		}
		// And notify the rest of the application of the change
		getApplicationConfig().applicationContext().publishEvent(new LifecycleApplicationEvent(eventType, getEditingContact()));
		return true;
	}

	protected void onCancel() {
		// Warn the user if they are about to discard their changes
		if (form.getFormModel().isDirty()) {
			String msg = getApplicationConfig().messageResolver().getMessage("contactProperties.dirtyCancelMessage");
			String title = getApplicationConfig().messageResolver().getMessage("contactProperties.dirtyCancelTitle");
			ConfirmationDialog dlg = new ConfirmationDialog(title, msg) {
				protected void onConfirm() {
					ContactPropertiesDialog.super.onCancel();
				}
			};
			dlg.showDialog();
		}
		else {
			super.onCancel();
		}
	}
}
