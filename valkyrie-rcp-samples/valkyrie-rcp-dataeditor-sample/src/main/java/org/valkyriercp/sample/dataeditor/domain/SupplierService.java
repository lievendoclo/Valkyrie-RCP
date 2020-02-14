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
package org.valkyriercp.sample.dataeditor.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierService
{
    @SuppressWarnings("unchecked")
    public List<Supplier> findSuppliers(final SupplierFilter filter)
    {
        List<Supplier> filtered = new ArrayList<Supplier>();
        for (Supplier supplier : generateMap().values())
        {
            if (checkFilter(supplier, filter))
            {
                filtered.add(supplier);
            }
        }
        return filtered;
    }

    public Supplier getSupplier(Integer id)
    {
        return generateMap().get(id);
    }

    private Map<Integer, Supplier> generateMap() {
        Map<Integer, Supplier> repository = new HashMap<>();
        repository.put(1, new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636"));
        repository.put(2, new Supplier("Tim Sears","tim@microshoft.com","555-3634","Microshoft","555-8978"));
        repository.put(3, new Supplier("Peter Deloye","peter@ibmn.com","525-6636","IBMN","556-5654"));
        return repository;
    }

    public boolean checkFilter(Supplier supplier, SupplierFilter filter)
    {
        boolean nameOk = true;
        boolean contactNameOk = true;
        if (filter.getNameContains() != null)
            nameOk = supplier.getName().contains(filter.getNameContains());
        if (filter.getContactNameContains() != null)
            contactNameOk = supplier.getContactName().contains(filter.getContactNameContains());
        return nameOk && contactNameOk;
    }
}
