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

public class ItemService
{
    @SuppressWarnings("unchecked")
    public List<Item> findItems(final ItemFilter filter)
    {
        List<Item> filtered = new ArrayList<>();
        for (Item supplier : generateMap().values())
        {
            if (checkFilter(supplier, filter))
            {
                filtered.add(supplier);
            }
        }
        return filtered;
    }

    private Map<Integer, Item> generateMap() {
        Map<Integer, Item> repository = new HashMap<>();
        repository.put(1, new Item("Cheese", "Gouda", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
        repository.put(2, new Item("Cheese", "Emmental", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
        repository.put(3, new Item("Cheese", "Stilton", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
        return repository;
    }

    public Item getItem(Integer id)
    {
        return generateMap().get(id);
    }

    public boolean checkFilter(Item supplier, ItemFilter filter)
    {
        boolean nameOk = true;
        boolean contactNameOk = true;
        if (filter.getNameContains() != null)
            nameOk = supplier.getName().contains(filter.getNameContains());
        return nameOk && contactNameOk;
    }
}
