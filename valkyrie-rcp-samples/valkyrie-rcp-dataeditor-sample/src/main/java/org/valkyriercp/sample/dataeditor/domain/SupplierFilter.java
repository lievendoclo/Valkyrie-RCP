package org.valkyriercp.sample.dataeditor.domain;

public class SupplierFilter
{
    private String nameContains;

    private String contactNameContains;

    public String getContactNameContains()
    {
        return contactNameContains;
    }

    public void setContactNameContains(String contactNameContains)
    {
        this.contactNameContains = contactNameContains;
    }

    public String getNameContains()
    {
        return nameContains;
    }

    public void setNameContains(String nameContains)
    {
        this.nameContains = nameContains;
    }

    public static SupplierFilter fromSupplier(Supplier f)
    {
        SupplierFilter filter = new SupplierFilter();
        filter.setContactNameContains(f.getContactName());
        filter.setNameContains(f.getName());
        return filter;
    }
}
