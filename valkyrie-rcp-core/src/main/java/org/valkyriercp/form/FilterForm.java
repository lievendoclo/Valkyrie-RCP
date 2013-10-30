package org.valkyriercp.form;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.core.TitleConfigurable;

public abstract class FilterForm extends AbstractForm implements TitleConfigurable
{
    private String title;

    protected FilterForm()
    {
        this("filterForm");
    }

    protected FilterForm(String id)
    {
        setId(id);
    }

    @Override
    public FormModel createFormModel() {
        return formModelFactory.createFormModel(newFormObject(), getId());
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

