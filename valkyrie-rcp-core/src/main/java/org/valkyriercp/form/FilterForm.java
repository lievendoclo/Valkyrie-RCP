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
package org.valkyriercp.form;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.util.ValkyrieRepository;

public abstract class FilterForm extends AbstractForm implements TitleConfigurable
{
    private String title;

    protected FilterForm()
    {
        this("filterForm");
    }

    protected FilterForm(String id)
    {
        super(id);
    }

    @Override
    public FormModel createFormModel() {
        return ValkyrieRepository.getInstance().getApplicationConfig().formModelFactory().createFormModel(newFormObject(), getId());
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

