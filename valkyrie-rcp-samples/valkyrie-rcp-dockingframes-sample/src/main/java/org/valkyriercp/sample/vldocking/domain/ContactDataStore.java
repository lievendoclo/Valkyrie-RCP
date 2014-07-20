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
package org.valkyriercp.sample.vldocking.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * This class provides a trivial in-memory datastore to hold all the contacts. In a real application, this would
 * probably be a server-side object that is accessed via an interface using the typical Spring wiring and remoting.
 * @author Larry Streepy
 */
public class ContactDataStore {

	/** Simple Id generator. */
	private static int nextId = 1;

	/** Our contacts. */
	private HashSet contacts = new HashSet();

	/**
	 * Default constructor - "load" our initial data.
	 */
	public ContactDataStore() {
		loadData();
	}

	/**
	 * Get all the contacts.
	 * @return Array of all contact objects
	 */
	public Contact[] getAllContacts() {
		return (Contact[]) contacts.toArray(new Contact[0]);
	}

	/**
	 * Update a contact.
	 */
	public void update(Contact contact) {
		contacts.add(contact);
	}

	/**
	 * Delete a contact.
	 */
	public void delete(Contact contact) {
		contacts.remove(contact);
	}

	/**
	 * Add a new contact.
	 */
	public void add(Contact contact) {
		contact.setId(nextId++); // Give it a unique id
		contacts.add(contact);
	}

	/**
	 * Load our initial data.
	 */
	private void loadData() {
		contacts.add(makeContact("Larry", "Streepy", "123 Some St.", "Apt. #26C", "New York", "NY", "10010",
				ContactType.BUSINESS, "Lorem ipsum..."));
		contacts.add(makeContact("Keith", "Donald", "456 WebFlow Rd.", "2", "Cooltown", "NY", "10001",
				ContactType.BUSINESS, "Lorem ipsum..."));
		contacts.add(makeContact("Steve", "Brothers", "10921 The Other Street", "", "Denver", "CO", "81234-2121",
				ContactType.PERSONAL, "Lorem ipsum..."));
		contacts.add(makeContact("Carlos", "Mencia", "4321 Comedy Central", "", "Hollywood", "CA", "91020",
				ContactType.PERSONAL, "Lorem ipsum..."));
		contacts.add(makeContact("Jim", "Jones", "1001 Another Place", "", "Dallas", "TX", "71212",
				ContactType.PERSONAL, "Lorem ipsum..."));
		contacts.add(makeContact("Jenny", "Jones", "1001 Another Place", "", "Dallas", "TX", "75201",
				ContactType.PERSONAL, "Lorem ipsum..."));
		contacts.add(makeContact("Greg", "Jones", "9 Some Other Place", "Apt. 12D", "Chicago", "IL", "60601",
				ContactType.PERSONAL, "Lorem ipsum..."));
	}

    private List<TodoItem> getTodoItemList()
    {
        List<TodoItem> l = new ArrayList<TodoItem>();
        l.add(new TodoItem("test", "test", new Date()));
        return l;
    }

	/**
	 * Make a Contact object with the given data.
	 * @return Contact object
	 */
	private Contact makeContact(String first, String last, String address1, String address2, String city, String state,
			String zip, ContactType contactType, String memo) {
		Contact contact = new Contact();
		contact.setId(nextId++);
		contact.setContactType(contactType);
		contact.setFirstName(first);
		contact.setLastName(last);
        contact.setMemo(memo);

		Address address = contact.getAddress();
		address.setAddress1(address1);
		address.setAddress2(address2);
		address.setCity(city);
		address.setState(state);
		address.setZip(zip);

        contact.setTodoItems(getTodoItemList());

		return contact;
	}
}
