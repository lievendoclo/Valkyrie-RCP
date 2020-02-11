/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.sample.dockingframes.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides a trivial domain object for the sample application. It represents a simple Contact entry in a
 * personal address book. It is not very useful in that it only allows a single address for an individual and it doesn't
 * support arbitrary contact data, just predefined fields. However, since we're not going into the Address Book
 * business, this will suffice for demonstration purposes in this sample application.
 * <p>
 * This class makes use of one subordinate (or nested) object in order to show how nested property paths can be used in
 * forms. It doesn't really serve any other great design need.
 * <p>
 * The validation rules for this class are provided externally, by {@link SimpleValidationRulesSource}. This
 * configuration is often required when you don't have any mechanism to extend the domain object directly, or for other
 * design reasons, don't want to include the validation rules directly in the domain object implementation.
 * @author Larry Streepy
 * @see SimpleValidationRulesSource
 */
public class Contact implements Comparable<Contact>
{

	private int id;

	private ContactType contactType;

	private String firstName;

	private String middleName;

	private String lastName;

	private Date dateOfBirth;

	private Address address;

	private String homePhone;

	private String workPhone;

	private String emailAddress;

    private String memo;

    private BigDecimal monthlyIncome;

    private List<TodoItem> todoItems;

	/**
	 * Default constructor.
	 */
	public Contact() {
		setAddress(new Address()); // Avoid null sub-object
        todoItems = new ArrayList<TodoItem>();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the homePhone
	 */
	public String getHomePhone() {
		return homePhone;
	}

	/**
	 * @param homePhone the homePhone to set
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the workPhone
	 */
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * @param workPhone the workPhone to set
	 */
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	/**
	 * @return the contactType
	 */
	public ContactType getContactType() {
		return contactType;
	}

	/**
	 * @param contactType the contactType to set
	 */
	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public BigDecimal getMonthlyIncome()
    {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome)
    {
        this.monthlyIncome = monthlyIncome;
    }

    public List<TodoItem> getTodoItems()
    {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems)
    {
        this.todoItems = todoItems;
    }

    /**
	 * Compare two objects for equality. Just test their ids.
	 * @param o object to compare
	 */
	public boolean equals(Object o) {
		if (o instanceof Contact) {
			return id == ((Contact) o).id;
		}
		return false;
	}

	/**
	 * Hashcode.
	 */
	public int hashCode() {
		return id;
	}

    public int compareTo(Contact o)
    {
        return getId() - o.getId();
    }
}
