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

