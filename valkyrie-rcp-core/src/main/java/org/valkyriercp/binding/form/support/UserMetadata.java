package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.form.FieldMetadata;
import org.valkyriercp.binding.form.FormModel;

/**
 * Class which holds keys for user metadata used in spring rich
 *
 * @author Mathias Broekelmann
 *
 */
public abstract class UserMetadata {
    /**
     * The name of a user metadata value which is used to determine if a value for a form field is protected and should
     * be hidden on logging or error messages.
     * <p>
     * The value must be an instance of {@link Boolean}, if true any value of the field will not be displayed either
     * through logging or showing value errors
     */
    public static final String PROTECTED_FIELD = "org.springframework.binding.support.ProtectedField";

    /**
     * tests if the usermetadata of the field has a boolean value true for the key {@value #PROTECTED_FIELD}
     *
     * @param fieldName
     *            the fieldname
     * @return true if the field is protected, otherwise false
     */
    public static boolean isFieldProtected(FormModel formModel, String fieldName) {
        FieldMetadata metaData = formModel.getFieldMetadata(fieldName);
        return Boolean.TRUE.equals(metaData.getUserMetadata(UserMetadata.PROTECTED_FIELD));
    }

    /**
     * defines the protectable state for a field
     *
     * @param formModel the formmodel
     * @param fieldName the field to protect
     * @param protectedField if true the field will be defined as protectable otherwise false
     */
    public static void setFieldProtected(FormModel formModel, String fieldName, boolean protectedField) {
        FieldMetadata metaData = formModel.getFieldMetadata(fieldName);
        metaData.getAllUserMetadata().put(PROTECTED_FIELD, Boolean.valueOf(protectedField));
    }
}

