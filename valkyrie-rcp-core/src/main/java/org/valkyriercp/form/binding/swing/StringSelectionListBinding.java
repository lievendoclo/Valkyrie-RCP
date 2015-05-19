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
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Binding which maps a property to a comboBox.
 * 
 * @author jh
 * 
 */
public class StringSelectionListBinding extends CustomBinding
{

    /** Labels to show in comboBox. */
    private Object[] selectionListLabelMessages;
    /** Keys associated with each label in the comboBox. */
    private Object[] selectionListKeys;
    /** ComboBox with selectionList. */
    private JComboBox combobox = null;
    /** Id used to configure the comboBox, including labels. */
    private String id = null;
    /** Temporary disconnecting valueModel from comboBox for internal workings. */
    private boolean settingValueModel = false;
    /** The default index to select when property doesn't map to the list. */
    private int defaultKeyIndex = -1;

    /**
     * Constructor.
     * 
     * @param comboBox
     *            JComboBox to use with this binding.
     * @param formModel
     *            formModel containing the property.
     * @param formPropertyPath
     *            path to property.
     * @param readOnly
     *            force readOnly.
     */
    public StringSelectionListBinding(JComboBox comboBox, FormModel formModel, String formPropertyPath,
            boolean readOnly)
    {
        super(formModel, formPropertyPath, null);
        this.combobox = comboBox;
        setReadOnly(readOnly);
    }

    /**
     * Will resolve keys to messages using 'id'.'key'.label as a message
     * resource key.
     * 
     * @param id
     *            id to use with message key resolving.
     * @param keys
     *            keys to extract messages from.
     * @return array containing labels/messages for each key.
     */
    private String[] createLabels(final String id, final Object[] keys)
    {
        String[] messages = new String[keys.length];
        for (int i = 0; i < messages.length; ++i)
        {
            messages[i] = getApplicationConfig().messageResolver().getMessage(id, keys[i] == null ? "null" : keys[i].toString(),
                    "label");
        }
        return messages;
    }

    /**
     * Will extract labels from a map in sorted order.
     * 
     * @param sortedKeys
     *            collection with keys in the appropriate order. Each key
     *            corresponds to one in the map.
     * @param keysAndLabels
     *            map containing both, keys and labels, from which the labels
     *            have to be extracted.
     * @return a collection containing the labels corresponding to the
     *         collection of sortedKeys.
     */
    public static Collection getLabelsFromMap(Collection sortedKeys, Map keysAndLabels)
    {
        Collection<Object> labels = new ArrayList<Object>();
        Iterator keyIt = sortedKeys.iterator();
        while (keyIt.hasNext())
        {
            labels.add(keysAndLabels.get(keyIt.next()));
        }
        return labels;
    }

    @Override
    protected void valueModelChanged(Object newValue)
    {
        preSelectItem();
        selectItem(newValue);
        readOnlyChanged();
    }

    /**
     * Selects the given item in the current selectionList of the comboBoxModel.
     * When the item doesn't map to an entry in the comboBoxModel, select a
     * default one. See {@link #getDefaultKeyIndex()} and
     * {@link #setDefaultKeyIndex(int)} for default.
     * 
     * @param item
     *            object that needs to be selected in the comboBox.
     */
    protected void selectItem(Object item)
    {
        if (this.selectionListLabelMessages != null && this.selectionListLabelMessages.length > 0)
        {
            int itemIndex = search(this.selectionListKeys, item);
            if (itemIndex > -1) // item found in list (may/can be null)
            {
                settingValueModel = true;
                this.combobox.setSelectedItem(this.selectionListLabelMessages[itemIndex]);
                settingValueModel = false;
            }
            else
            // newValue not in list, select defaultIndex or nothing
            {
                this.combobox.setSelectedIndex(getDefaultKeyIndex());
            }
        }
    }

    /**
     * Arrays.binarySearch(..) does exist in java, but assumes that array is
     * sorted, we don't have this restriction so dumb search from first to
     * last...
     * 
     * @param keys
     * @param toFind
     * @return
     */
    private int search(Object[] keys, Object toFind)
    {
        for (int i = 0; i < keys.length; ++i)
        {
            if (toFind == null)
            {
                if (keys[i] == null)
                    return i;
            }
            else if (toFind.equals(keys[i]))
                return i;
        }
        return -1; // niets gevonden
    }

    /**
     * @return Default index in list
     */
    public int getDefaultKeyIndex()
    {
        return defaultKeyIndex;
    }

    /**
     * Set index to use when value of property doesn't match to an item in the
     * comboBox list.
     * 
     * @param defaultKeyIndex
     *            default index to select.
     */
    public void setDefaultKeyIndex(int defaultKeyIndex)
    {
        this.defaultKeyIndex = defaultKeyIndex;
    }

    @Override
    protected JComponent doBindControl()
    {
        this.combobox.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                if (!settingValueModel && combobox.isEnabled() && combobox.getSelectedIndex() != -1)
                    controlValueChanged(selectionListKeys[combobox.getSelectedIndex()]);

            }
        });
        valueModelChanged(getValue());
        return this.combobox;
    }

    @Override
    protected void readOnlyChanged()
    {
        combobox.setEnabled(isEnabled() && !isReadOnly());
    }

    @Override
    protected void enabledChanged()
    {
        readOnlyChanged();
    }

    /**
     * PreSelect hook. Define additional things to be executed before selecting
     * the propertyValue in the comboBox list.
     */
    protected void preSelectItem()
    {

    }

    /**
     * @param keys
     *            array of keys to set, use keys as labels.
     * @see #setSelectionList(Object[], Object[])
     */
    public final void setSelectionList(Object[] keys)
    {
        setSelectionList(keys, keys);
    }

    /**
     * Replace the comboBoxModel in order to use the new keys/labels.
     * 
     * @param keys
     *            array of objects to use as keys.
     * @param labels
     *            array of objects to use as labels in the comboBox.
     */
    public final void setSelectionList(Object[] keys, Object[] labels)
    {
        selectionListKeys = keys;
        selectionListLabelMessages = createLabels(getId(), labels);
        combobox.setModel(new DefaultComboBoxModel(selectionListLabelMessages));
        selectItem(getValue());
    }

    /**
     * @param keys
     *            collection of keys to set, use keys as labels.
     * @see #setSelectionList(Object[], Object[])
     */
    public final void setSelectionList(Collection keys)
    {
        setSelectionList(keys.toArray());
    }

    /**
     * @param keys
     *            collection of keys to use, corresponding to the labels.
     * @param labels
     *            collection of labels to use, corresponding to the keys.
     * @see #setSelectionList(Object[], Object[])
     */
    public final void setSelectionList(Collection keys, Collection labels)
    {
        setSelectionList(keys.toArray(), labels.toArray());
    }

    /**
     * @param sortedKeys
     *            sorted subcollection of map containing the needed keys.
     * @param keysAndLabels
     *            map containing the keys (supercollection) and mapped labels.
     * @see #setSelectionList(Object[], Object[])
     */
    public final void setSelectionList(Collection sortedKeys, Map keysAndLabels)
    {
        setSelectionList(sortedKeys, getLabelsFromMap(sortedKeys, keysAndLabels));
    }

    /**
     * @param keysAndLabels
     *            map containing keys and labels to set in comboBox.
     * @see #setSelectionList(Object[], Object[])
     */
    public final void setSelectionList(Map keysAndLabels)
    {
        setSelectionList(keysAndLabels.keySet(), keysAndLabels.values());
    }

    /**
     * @return id to resolve messages.
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Set id to resolve messages.
     * 
     * @param id
     *            to use for message keys.
     */
    public void setId(String id)
    {
        this.id = id;
    }

}
