package org.valkyriercp.sample.dataeditor.domain;

public class Supplier implements Comparable<Supplier>
{
    private String name;

    private String contactName;

    private String telephoneNumber;

    private String faxNumber;

    private String emailAddress;

    public Supplier()
    {
    }

    public Supplier(String contactName, String emailAddress, String faxNumber, String name, String telephoneNumber)
    {
        this.contactName = contactName;
        this.emailAddress = emailAddress;
        this.faxNumber = faxNumber;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getFaxNumber()
    {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTelephoneNumber()
    {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber)
    {
        this.telephoneNumber = telephoneNumber;
    }

    public int compareTo(Supplier supplier)
    {
        return getName().compareTo(supplier.getName());
    }
}
