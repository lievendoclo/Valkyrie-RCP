package org.valkyriercp.form;

import org.valkyriercp.binding.validation.ValidationListener;

public interface ValidationResultsReporter extends ValidationListener {

    /**
     * Return the "has errors" status of the validation results model(s).
     * @return true if this model or any child model is marked as having errors
     */
    public boolean hasErrors();

}