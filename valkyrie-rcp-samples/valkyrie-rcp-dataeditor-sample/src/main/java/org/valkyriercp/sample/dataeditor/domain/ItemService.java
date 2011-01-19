package org.valkyriercp.sample.dataeditor.domain;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ItemService
{
    private static final Map<Integer, Item> REPOSITORY = new HashMap<Integer, Item>();

    static
    {
        REPOSITORY.put(1, new Item("Cheese", "Gouda", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
        REPOSITORY.put(2, new Item("Cheese", "Emmental", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
        REPOSITORY.put(3, new Item("Cheese", "Stilton", new Supplier("Jake Johnson","jake@springcource.com","555-5236","SpringCource","555-3636")));
    }

    @SuppressWarnings("unchecked")
    public List<Item> findItems(final ItemFilter filter)
    {
        List<Item> filtered = new ArrayList<Item>();
        for (Item supplier : REPOSITORY.values())
        {
            if (checkFilter(supplier, filter))
            {
                filtered.add(supplier);
            }
        }
        return filtered;
    }

    public Item getItem(Integer id)
    {
        return REPOSITORY.get(id);
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
