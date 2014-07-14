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

import org.valkyriercp.sample.vldocking.domain.Contact;
import org.valkyriercp.sample.vldocking.domain.ContactDataStore;
import org.valkyriercp.table.support.AbstractObjectTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
/**
 * This class provides a concrete implementation of a table showing {@link org.valkyriercp.sample.vldocking.domain.Contact} objects.
 * @author lstreepy
 */
public class ContactTable extends AbstractObjectTable {

	/** The data store holding all our contacts. */
	private ContactDataStore dataStore;

	/**
	 * Default constructor.
	 */
	public ContactTable(ContactDataStore dataStore) {
		super("contacts", new String[] { "lastName", "firstName", "address.address1", "address.city", "address.state",
				"address.zip" });
		this.dataStore = dataStore;
	}

	protected void configureTable(JTable table) {
		// Adjust the table column widths
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(100);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(200);
		tcm.getColumn(3).setPreferredWidth(50);
		tcm.getColumn(4).setPreferredWidth(10);
		tcm.getColumn(5).setPreferredWidth(50);
	}

	/**
	 * Provide the initial data for the table. Note that this is hard coded for this sample. You would normally access a
	 * persistent store, or some other source to get the data for the table.
	 */
	protected Object[] getDefaultInitialData() {
		return dataStore.getAllContacts();
	}

	/**
	 * Get the array of selected Contact objects in the table.
	 * @return array of Contacts, zero length if nothing is selected
	 */
	public Contact[] getSelectedContacts() {
		int[] selected = getTable().getSelectedRows();
		Contact[] contacts = new Contact[selected.length];
		for (int i = 0; i < selected.length; i++) {
			contacts[i] = (Contact) getTableModel().getElementAt(selected[i]);
		}
		return contacts;
	}

	public Contact getSelectedContact() {
		return (Contact) getSelectedContacts()[0];
	}
}
