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
package org.valkyriercp.list;

import org.valkyriercp.binding.value.support.AbstractValueModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class to adapt the selection model of a list into a value model. This allows
 * it to be used in conjunction with various Guard implementations.
 *
 * @author Larry Streepy
 * @see ListSingleSelectionGuard
 * @see ListMultipleSelectionGuard
 */
public class ListSelectionValueModelAdapter extends AbstractValueModel implements ListSelectionListener {

    private ListSelectionModel model;
    private int[] currentSelection = new int[0];
    private boolean skipSelectionModelUpdate = false;

    /**
     * Constructor.
     *
     * @param model selection model to adapt
     */
    public ListSelectionValueModelAdapter( ListSelectionModel model ) {
        this.model = model;
        this.model.addListSelectionListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged( ListSelectionEvent e ) {
        if( !e.getValueIsAdjusting() ) {

            // We need to install this new value into the value model, but
            // we don't want to propogate it back down to the adapted selection
            // model (since we are responding to a change in that model).
            skipSelectionModelUpdate = true;
            setValue(getSelectedRows());
            skipSelectionModelUpdate = false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.binding.value.ValueModel#getValue()
     */
    public Object getValue() {
        return currentSelection;
    }

    /**
     * Set the selection value.
     *
     * @param newValue must be an integer array (int[])
     */
    public void setValue( Object newValue ) {
        int[] newSelection = (int[]) newValue;

        if( hasChanged(currentSelection, newSelection) ) {

            int[] oldValue = currentSelection;
            currentSelection = newSelection;
            fireValueChange(oldValue, currentSelection);

            if( !skipSelectionModelUpdate) {
                // Don't want notifications while we do this
                model.removeListSelectionListener(this);

                // Install the selection on the adapted model
                model.clearSelection();

                int i = 0;
                int len = newSelection.length;
                while( i < len ) {
                    int start = newSelection[i];
                    while( i < len - 1 && newSelection[i] == newSelection[i + 1] - 1 ) {
                        i++;
                    }
                    int end = newSelection[i];
                    model.addSelectionInterval(start, end);
                    i++;
                }

                // Reinstall listener
                model.addListSelectionListener(this);
            }
        }
    }

    /**
     * See if two arrays are different.
     */
    private boolean hasChanged( int[] oldValue, int[] newValue ) {
        if( oldValue.length == newValue.length ) {
            for( int i = 0; i < newValue.length; i++ ) {
                if( oldValue[i] != newValue[i] ) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Returns the indices of all selected rows in the model.
     *
     * @return an array of integers containing the indices of all selected rows,
     *         or an empty array if no row is selected
     */
    private int[] getSelectedRows() {
        int iMin = model.getMinSelectionIndex();
        int iMax = model.getMaxSelectionIndex();

        if( (iMin == -1) || (iMax == -1) ) {
            return new int[0];
        }

        int[] rvTmp = new int[1 + (iMax - iMin)];
        int n = 0;
        for( int i = iMin; i <= iMax; i++ ) {
            if( model.isSelectedIndex(i) ) {
                rvTmp[n++] = i;
            }
        }
        int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }
}

