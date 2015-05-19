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
package org.valkyriercp.form.binding.swing;

import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.support.AbstractBinderSelectionStrategy;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class SwingBinderSelectionStrategy extends AbstractBinderSelectionStrategy
{

    private Map<String, Binder> idBoundBinders = new HashMap<String, Binder>();

    /*
    * Default constructor.
    */
    public SwingBinderSelectionStrategy()
    {
        super(JTextField.class);
    }

    /**
     * Set a map with predefined binders
     *
     * @param binders
     */
    public void setIdBoundBinders(Map binders)
    {
        this.idBoundBinders = binders;
    }

    /**
     * Register additional map of id bound binders
     *
     * @param binders
     */
    public void registerIdBoundBinders(Map<String, Binder> binders)
    {
        idBoundBinders.putAll(binders);
    }

    /**
     * Try to find a binder with a specified id. If no binder is found, try
     * to locate it into the application context, check whether it's a binder and
     * add it to the id bound binder map.
     *
     * @param id Id of the binder
     * @return Binder or <code>null</code> if not found.
     */
    public Binder getIdBoundBinder(String id)
    {
        Binder binder = idBoundBinders.get(id);
        if (binder == null) //  try to locate the binder bean
        {
            Object binderBean = getApplicationContext().getBean(id);
            if (binderBean instanceof Binder)
            {
                if (binderBean != null)
                {
                    idBoundBinders.put(id, (Binder) binderBean);
                    binder = (Binder) binderBean;
                }
            }
            else
            {
                throw new IllegalArgumentException("Bean '" + id + "' was found, but was not a binder");
            }
        }
        return binder;
    }

    /**
     * Select a binder based on a control type
     *
     * @param controlType Type of control
     * @return  The binder for that control
     */
    public Binder selectBinder(Class controlType)
    {
        return findBinderByControlType(controlType);
    }
}