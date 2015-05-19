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


