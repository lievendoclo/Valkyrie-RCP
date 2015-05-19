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

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.util.*;


/**
 * Binder to configure {@link StringSelectionListBinding}.
 * 
 * @author jh
 * 
 */
public class StringSelectionListBinder extends AbstractBinder
{

    /**
     * Constant to use when having a combobox mapped to a {@link Boolean} which
     * needs three state logic.
     */
    public static final Collection TRUE_FALSE_NULL = Arrays.asList(new Object[]{Boolean.TRUE, Boolean.FALSE,
            null});
    
    public static final String LABELS_KEY = "labels";
    public static final String LABEL_ID_KEY = "labelId";
    public static final String KEYS_KEY = "keys";
    public static final String DEFAULT_KEY_INDEX_KEY = "defaultKeyIndex";
    public static final String ADD_NULL_KEY = "addNull";

    private String labelId = null;
    private Collection keys = null;
    private Collection labels = null;
    private int defaultKeyIndex = -1;
    private boolean addNull = false;

    /**
     * 
     * @param addNull indien deze waarde op true staat, dan wordt null aan de selectiemogelijkheden toegevoegd.
     * Wordt deze voor de labels en keys geplaatst, dan komt deze eerst, anders laatst.
     */
    public void setAddNull(boolean addNull)
    {
        this.addNull = addNull;
        
        if (addNull && labels != null && keys == null)
            throw new IllegalStateException("Keys should be defined before labels!");
        
        // indien keys reeds gezet zijn (dus null moet achteraan toegevoegd worden
        if (addNull && keys != null)
        {
            keys.add(null);
            // eventueel aan labels toevoegen 
            if (labels != null)
                labels.add(null);    
        }
    }

    public StringSelectionListBinder()
    {
        super(null, new String[] {KEYS_KEY, LABELS_KEY, LABEL_ID_KEY, DEFAULT_KEY_INDEX_KEY, ADD_NULL_KEY});
    }

    public void setLabelId(String labelId)
    {
        this.labelId = labelId;
    }

    protected String getLabelId()
    {
        return labelId;
    }

    @Override
    protected JComponent createControl(Map context)
    {
        return new JComboBox();
    }

    public void setLabels(Collection labels)
    {
        if (addNull)
        {
            this.labels = new ArrayList();
            this.labels.add(null);
            this.labels.addAll(labels);
        }
        else
        {
            this.labels = labels;    
        }        
    }

    protected Collection getLabels()
    {
        return labels;
    }

    public void setKeys(Collection keys)
    {
        if (addNull)
        {
            this.keys = new ArrayList();
            this.keys.add(null);
            this.keys.addAll(keys);
        }
        else
        {
            this.keys = keys;    
        }
    }

    protected Collection getKeys()
    {
        return keys;
    }

    public void setMap(Map map)
    {
        List<Map.Entry> mapEntries = sortMapEntriesOnValues(map);
        setKeys(new ArrayList());
        setLabels(new ArrayList());
        extractListFromMapEntries(mapEntries, (List) keys, (List) labels);
    }

    private void extractListFromMapEntries(List<Map.Entry> mapEntries, List keys, List values) {
        for (Map.Entry mapEntry : mapEntries) {
            keys.add(mapEntry.getKey());
            keys.add(mapEntry.getValue());
        }
    }

    private List<Map.Entry> sortMapEntriesOnValues(Map map) {
        Set<Map.Entry> entrySet = map.entrySet();
        ArrayList<Map.Entry> entries = new ArrayList<Map.Entry>(entrySet);
        Collections.sort(entries, new MapEntryValueComparator());
        return entries;
    }

    public static class MapEntryValueComparator implements Comparator<Map.Entry>
    {
        public int compare(Map.Entry entryLeft, Map.Entry entryRight) {
            return ((Comparable) entryLeft.getValue()).compareTo(entryRight.getValue());
        }
    }

    public void setSubSetOfMap(Collection sortedKeys, Map map)
    {
        setKeys(sortedKeys);
        setLabels(StringSelectionListBinding.getLabelsFromMap(sortedKeys, map));
    }

    public void setDefaultKeyIndex(int defaultKeyIndex)
    {
        this.defaultKeyIndex = defaultKeyIndex;
    }
    
    

    @Override
    protected Binding doBind(JComponent component, FormModel formModel, String formPropertyPath, Map context)
    {
        String labelId = null;
        Collection keys = null;
        Collection labels = null;
        int defaultKeyIndex = -1;
        boolean addNull = false;
        if(context.containsKey(LABEL_ID_KEY)) {
            labelId = (String) context.get(LABEL_ID_KEY);
        } else {
            labelId = this.labelId;
        }
        if(context.containsKey(KEYS_KEY)) {
            keys = (Collection) context.get(KEYS_KEY);
            if(context.containsKey(ADD_NULL_KEY) || (Boolean) context.get(ADD_NULL_KEY))
                keys.add(null);
            else if(!context.containsKey(ADD_NULL_KEY) || addNull)
                keys.add(null);
        } else {
            keys = this.keys;
        }
        if(context.containsKey(LABELS_KEY)) {
            labels = (Collection) context.get(LABELS_KEY);
            if(context.containsKey(ADD_NULL_KEY) || (Boolean) context.get(ADD_NULL_KEY))
                labels.add(null);
            else if(!context.containsKey(ADD_NULL_KEY) || addNull)
                labels.add(null);
        } else {
            labels = this.labels;
        }
        if(context.containsKey(DEFAULT_KEY_INDEX_KEY)) {
            defaultKeyIndex = (Integer) context.get(DEFAULT_KEY_INDEX_KEY);
        } else {
            defaultKeyIndex = this.defaultKeyIndex;
        }
        StringSelectionListBinding stringSelectionListBinding = new StringSelectionListBinding(
                (JComboBox) component, formModel, formPropertyPath, isReadOnly());
        stringSelectionListBinding.setId(labelId);
        stringSelectionListBinding.setDefaultKeyIndex(defaultKeyIndex);
        if (keys != null)
            stringSelectionListBinding.setSelectionList(keys, labels == null
                    ? keys
                    : labels);
        return stringSelectionListBinding;
    }

}
