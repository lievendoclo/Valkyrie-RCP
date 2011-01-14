package org.valkyriercp.util;

import org.springframework.beans.BeanUtils;

import java.util.*;

public class CloneUtils {
    private static Object clonePublicCloneableObject(Object value)
    {
        try
        {
            return ((PublicCloneable) value).clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException("Clone not support for object " + value.getClass());
        }
    }

    public static Object getClone(Object value)
    {
        if (value instanceof PublicCloneable)
        {
            return clonePublicCloneableObject(value);
        }
        else if (value instanceof Collection)
        {
            Collection valueCollection = (Collection) value;
            Collection clonedCollection;
            if (valueCollection instanceof java.util.List)
            {
                clonedCollection = (valueCollection instanceof LinkedList)
                        ? new LinkedList()
                        : new ArrayList();
            }
            else if (valueCollection instanceof Set)
            {
                clonedCollection = (valueCollection instanceof SortedSet)
                        ? new TreeSet()
                        : (valueCollection instanceof LinkedHashSet) ? new LinkedHashSet() : new HashSet();
            }
            else
            {
                clonedCollection = (Collection) BeanUtils.instantiateClass(value.getClass());
            }
            for (Object valueElement : valueCollection)
            {
                if (valueElement instanceof PublicCloneable)
                {
                    clonedCollection.add(clonePublicCloneableObject(valueElement));
                }
                else
                {
                    // there isn't much else we can do?
                    clonedCollection.add(valueElement);
                }
            }
            return clonedCollection;
        }
        else if (value instanceof Map)
        {
            Map valueMap = (Map) value;
            Map clonedMap = (valueMap instanceof SortedMap)
                    ? new TreeMap()
                    : (valueMap instanceof LinkedHashMap) ? new LinkedHashMap() : new HashMap();
            for (Iterator entryIter = valueMap.entrySet().iterator(); entryIter.hasNext();)
            {
                Map.Entry entry = (Map.Entry) entryIter.next();
                if (entry.getValue() instanceof PublicCloneable)
                {
                    clonedMap.put(entry.getKey(), clonePublicCloneableObject(entry.getValue()));
                }
                else
                {
                    // there isn't much else we can do?
                    clonedMap.put(entry.getKey(), entry.getValue());
                }
            }
            return clonedMap;
        }
        return value;
    }
}
