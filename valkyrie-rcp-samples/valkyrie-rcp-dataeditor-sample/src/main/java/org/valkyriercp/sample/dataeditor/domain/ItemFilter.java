package org.valkyriercp.sample.dataeditor.domain;

public class ItemFilter
{
    private String quickSearch;

    private String nameContains;

    public String getNameContains()
    {
        return nameContains;
    }

    public void setNameContains(String nameContains)
    {
        this.nameContains = nameContains;
    }

    public String getQuickSearch()
    {
        return quickSearch;
    }

    public void setQuickSearch(String quickSearch)
    {
        this.quickSearch = quickSearch;
    }
}
