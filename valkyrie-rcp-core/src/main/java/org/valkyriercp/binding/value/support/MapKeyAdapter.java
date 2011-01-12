package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueModel;

import java.util.Map;

/**
 *
 * @author HP
 */
public class MapKeyAdapter extends AbstractValueModel {

    private ValueModel mapValueModel;

    private Object key;

    public MapKeyAdapter(ValueModel valueModel, Object key) {
        super();
        this.mapValueModel = valueModel;
        setKey(key);
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        Map map = (Map)mapValueModel.getValue();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public void setValue(Object value) {
        Map map = (Map)mapValueModel.getValue();
        if (map == null) {
            return;
        }
        map.put(key, value);
    }
}


