package org.valkyriercp.sample.dataeditor.domain;

public class Item implements Comparable<Item>
{
    private String name;
    private String description;
    private Supplier supplier;

    public Item()
    {
    }

    public Item(String descriptiom, String name, Supplier supplier)
    {
        this.description = descriptiom;
        this.name = name;
        this.supplier = supplier;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Supplier getSupplier()
    {
        return supplier;
    }

    public void setSupplier(Supplier supplier)
    {
        this.supplier = supplier;
    }

    public int compareTo(Item o)
    {
        return getName().compareTo(o.getName());
    }
}
