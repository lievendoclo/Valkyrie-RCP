package org.valkyriercp.form;

import org.valkyriercp.core.TitleConfigurable;

public abstract class FilterForm extends AbstractForm implements TitleConfigurable
{
    private String title;

    protected FilterForm()
    {
    }

    protected FilterForm(String id)
    {
        super(id);
    }

    @Override
    protected void init()
    {
        Object filterModel = newFormObject();
        setFormModel(FormModelHelper.createFormModel(filterModel));
        getFormModel().setId(getId());
        getApplicationConfig().applicationObjectConfigurer().configure(this, getId());
    }

    public Object getFilterCriteria()
    {
        return getFormObject();
    }

    public void resetCriteria()
    {
        this.setFormObject(newFormObject());
    }

    protected Object newFormObject()
    {
        return null;
    }

    public void setDefaultCriteria()
    {
        Object newFormObject = newFormObject();
        setFormObject(newFormObject);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
}

