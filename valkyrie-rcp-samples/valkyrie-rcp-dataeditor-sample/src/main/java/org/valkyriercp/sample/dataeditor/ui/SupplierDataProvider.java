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
package org.valkyriercp.sample.dataeditor.ui;


import org.valkyriercp.sample.dataeditor.domain.Supplier;
import org.valkyriercp.sample.dataeditor.domain.SupplierFilter;
import org.valkyriercp.sample.dataeditor.domain.SupplierService;
import org.valkyriercp.widget.editor.provider.AbstractDataProvider;

import java.util.List;

public class SupplierDataProvider extends AbstractDataProvider
{
    private SupplierService service;

    public SupplierDataProvider(SupplierService service)
    {
        this.service = service;
    }

    public boolean supportsFiltering()
    {
        return true;
    }

    public List getList(Object criteria)
    {
        if (criteria instanceof SupplierFilter)
        {
            return service.findSuppliers((SupplierFilter) criteria);
        }
        else if (criteria instanceof Supplier)
        {
            return service.findSuppliers(SupplierFilter.fromSupplier((Supplier) criteria));
        }
        else
        {
            throw new IllegalArgumentException("This provider can only filter through SupplierFilter, not " + criteria.getClass());
        }
    }

    public boolean supportsUpdate()
    {
        return true;
    }

    @Override
    public Object doCreate(Object newData)
    {
        return newData;
    }

    @Override
    public void doDelete(Object dataToRemove)
    {
    }

    @Override
    public Object doUpdate(Object updatedData)
    {
        return updatedData;
    }

    public boolean supportsCreate()
    {
        return true;
    }

    public boolean supportsClone()
    {
        return false;
    }

    public boolean supportsDelete()
    {
        return true;
    }
}
