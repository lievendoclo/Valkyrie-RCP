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
package org.valkyriercp.form;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.validation.ValidationResultsModel;
import org.valkyriercp.core.Guarded;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Actually enables/disables registered 'guarded' objects based on the state of a
 * {@link ValidatingFormModel}.
 *
 * One instance of this FormGuard supports multiple guarded objects on one formModel. Each guarded object can
 * specify upon which state of the formModel it wants to be enabled:
 */
public class FormGuard implements PropertyChangeListener {

    /** Guard-registration mask-bit indicating enabled() will be set only if the formmodel has no errors. */
    public static final int ON_NOERRORS = 1;
    /**
     * Guard-registration mask-bit indicating enabled() will be set only if the formmodel has changes (is
     * dirty).
     */
    public static final int ON_ISDIRTY = 1 << 1;
    /** Guard-registration mask-bit indicating enabled() will be set only if the formmodel is enabled. */
    public static final int ON_ENABLED = 1 << 2;

    /**
     * Guard-registration mask-pattern indicating enabled() will be set just like is the case for the new-form
     * command. i.e. as soon as the formmodel is enabled.
     */
    public static final int LIKE_NEWFORMOBJCOMMAND = ON_ENABLED;
    /**
     * Guard-registration mask-pattern indicating enabled() will be set just like is the case for the revert
     * command. i.e. only if the formmodel is enabled AND uncommitted changes are present.
     */
    public static final int LIKE_REVERTCOMMAND = ON_ISDIRTY + LIKE_NEWFORMOBJCOMMAND;
    /**
     * Guard-registration mask-pattern indicating enabled() will be set just like is the case for the commit
     * command. i.e. only if the formmodel is enabled AND uncomitted changes are presenent AND the model is
     * valid.
     */
    public static final int LIKE_COMMITCOMMAND = ON_NOERRORS + LIKE_REVERTCOMMAND;

    /**
     * Guard-registration mask-pattern indicating enabled() will be set only if the formmodel is both enabled
     * and has no errors.
     */
    public static final int FORMERROR_GUARDED = ON_ENABLED + ON_NOERRORS;

    private final ValidatingFormModel formModel;

    private final Map guardedEntries = Collections.synchronizedMap(new HashMap());

    /**
     * Creates the FormGuard monitoring the passed formModel.
     *
     * @param formModel
     *            which state-changes should be passed to registered guarded objects.
     */
    public FormGuard(ValidatingFormModel formModel) {
        this.formModel = formModel;

        this.formModel.addPropertyChangeListener(FormModel.ENABLED_PROPERTY, this);
        this.formModel.addPropertyChangeListener(ValidationResultsModel.HAS_ERRORS_PROPERTY, this);
        this.formModel.addPropertyChangeListener(FormModel.DIRTY_PROPERTY, this);
    }

    /**
     * Creates the FormGuard monitoring the passed formModel, and adds the passed guarded-object.
     *
     * For backwards compatibility this assumes the {@link #FORMERROR_GUARDED} mask.
     *
     * @param formModel
     *            which state-changes should be passed to registered guarded objects.
     * @param guarded
     *            object that will get en/dis-abled
     */
    public FormGuard(ValidatingFormModel formModel, Guarded guarded) {
        this(formModel, guarded, FORMERROR_GUARDED);
    }

    /**
     * Creates the FormGuard monitoring the passed formModel, and adds the passed guarded-object using the
     * specified mask.
     *
     * @param formModel
     *            which state-changes should be passed to registered guarded objects.
     * @param guarded
     *            object that will get en/dis-abled
     * @param mask
     *            specifying what formModel state should enable the guarded object.
     */
    public FormGuard(ValidatingFormModel formModel, Guarded guarded, int mask) {
        this(formModel);
        addGuarded(guarded, mask);
    }

    private void updateAllGuarded() {
        int formState = getFormModelState();

        Iterator guardedIter = this.guardedEntries.keySet().iterator();
        while (guardedIter.hasNext()) {
            Guarded guarded = (Guarded) guardedIter.next();
            int mask = ((Integer) this.guardedEntries.get(guarded)).intValue();

            boolean b = stateMatchesMask(formState, mask);
            guarded.setEnabled(b);
        }
    }

    private boolean stateMatchesMask(int formState, int mask) {
        return ((mask & formState) == mask);
    }

    private int getFormModelState() {
        int formState = 0;
        if (!formModel.getHasErrors())
            formState += ON_NOERRORS;
        if (formModel.isDirty())
            formState += ON_ISDIRTY;
        if (formModel.isEnabled())
            formState += ON_ENABLED;
        return formState;
    }

    /**
     * @see PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        updateAllGuarded();
    }

    /**
     * Adds a guarded object to be guarded by this FormGuard
     *
     * @param newGuarded
     *            object to be guarded
     * @param mask
     *            indicating which state of the formModel should enable the guarded obhject
     */
    public void addGuarded(Guarded newGuarded, int mask) {
        this.guardedEntries.put(newGuarded, new Integer(mask));
        newGuarded.setEnabled(stateMatchesMask(getFormModelState(), mask));
    }

    /**
     * Removes the guarded object from the management of this FormGuard.
     *
     * @param toRemove
     *            object that no longer should be managed
     *
     * @return <code>false</code> if the object toRemove was not present in the list of managed guarded objects.
     */
    public boolean removeGuarded(Guarded toRemove) {
        Object mask = this.guardedEntries.remove(toRemove);
        return mask != null;
    }
}
