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
        super(null);
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
        StringSelectionListBinding stringSelectionListBinding = new StringSelectionListBinding(
                (JComboBox) component, formModel, formPropertyPath, isReadOnly());
        stringSelectionListBinding.setId(getLabelId());
        stringSelectionListBinding.setDefaultKeyIndex(defaultKeyIndex);
        if (getKeys() != null)
            stringSelectionListBinding.setSelectionList(getKeys(), getLabels() == null
                    ? getKeys()
                    : getLabels());
        return stringSelectionListBinding;
    }

}
