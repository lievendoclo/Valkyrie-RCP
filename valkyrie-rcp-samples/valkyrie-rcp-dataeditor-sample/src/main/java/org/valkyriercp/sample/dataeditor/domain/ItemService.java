package org.valkyriercp.sample.dataeditor.domain;

import com.google.common.collect.Maps;
import com.sun.xml.internal.bind.v2.util.QNameMap;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ItemService
{
    @SuppressWarnings("unchecked")
    public List<Item> findItems(final ItemFilter filter)
    {
        List<Item> filtered = new ArrayList<Item>();
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
        Map<Integer, Item> repository = Maps.newHashMap();
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
