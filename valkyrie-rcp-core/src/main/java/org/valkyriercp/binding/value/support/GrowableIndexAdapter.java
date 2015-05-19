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
package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.IndexAdapter;
import org.valkyriercp.binding.value.ValueModel;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Keith Donald
 */
public class GrowableIndexAdapter extends AbstractIndexAdapter implements IndexAdapter {

    private boolean autoGrow;

    private ValueModel collectionValueModel;

    public GrowableIndexAdapter(boolean autoGrow, ValueModel collectionValueModel) {
        this.autoGrow = autoGrow;
        this.collectionValueModel = collectionValueModel;
    }

    public boolean isAutoGrow() {
        return autoGrow;
    }

    public void fireIndexedObjectChanged() {
    }

    public Object getValue() {
        return getCollectionValue(getIndex());
    }

    protected Object getCollectionValue(int index) {
        Object collection = collectionValueModel.getValue();
        if (collection.getClass().isArray()) {
            growArrayIfNeccessary(index);
            return Array.get(collection, index);
        }
        else if (collection instanceof List) {
            growCollectionIfNeccessary(index);
            return ((List)collection).get(index);
        }
        else if (collection instanceof Set) {
            growCollectionIfNeccessary(index);
            Set setValue = (Set)collection;
            Iterator it = setValue.iterator();
            for (int j = 0; it.hasNext(); j++) {
                Object element = it.next();
                if (j == index) {
                    return element;
                }
            }
            return null;
        }
        else {
            throw new IllegalArgumentException("Value must be a collection " + collection);
        }
    }

    public void setValue(Object value) {
        Object oldValue = setCollectionValue(getIndex(), value);
        fireValueChange(oldValue, value);
    }

    protected Object setCollectionValue(int index, Object value) {
        Object collection = collectionValueModel.getValue();
        if (collection.getClass().isArray()) {
            growArrayIfNeccessary(index);
            Object old = Array.get(value, index);
            Array.set(collection, index, value);
            return old;
        }
        else if (collection instanceof List) {
            growCollectionIfNeccessary(index);
            return ((List)value).set(index, value);
        }
        else if (collection instanceof Set) {
            growCollectionIfNeccessary(index);
            Set setValue = (Set)value;
            Iterator it = setValue.iterator();
            Object old = null;
            for (int j = 0; it.hasNext(); j++) {
                Object element = it.next();
                if (j == index) {
                    old = element;
                    it.remove();
                }
            }
            setValue.add(value);
            return old;
        }
        else {
            throw new IllegalArgumentException("Value must be a collection " + value);
        }
    }

    private void growArrayIfNeccessary(int index) {
        Object value = collectionValueModel.getValue();
        if (isAutoGrow()) {
            if (index >= Array.getLength(value)) {
                value = Array.newInstance(value.getClass(), index + 1);
                Object newArray = Array.get(value, index);
                System.arraycopy(value, 0, newArray, 0, Array.getLength(value));
                collectionValueModel.setValue(newArray);
            }
        }
    }

    private void growCollectionIfNeccessary(int index) {
        if (isAutoGrow()) {
            Object value = collectionValueModel.getValue();
            if (value instanceof List) {
                List listValue = (List)value;
                if (isAutoGrow()) {
                    while (index >= listValue.size()) {
                        listValue.add(null);
                    }
                }
            }
            else if (value instanceof Set) {
                Set setValue = (Set)value;
                while (index >= setValue.size()) {
                    setValue.add(null);
                }
            }
        }
    }
}

