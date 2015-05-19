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
package org.valkyriercp.binding.form;


/**
 * This interface is intended to mark Forms as having a specific method to set new form objects. This
 * can contain different logic than the normal {@link org.springframework.richclient.form.Form#setFormObject(Object)}. Note that the formObject
 * doesn't need to be <code>null</code> and hence the {@link org.springframework.richclient.form.AbstractForm#getNewFormObjectCommand()} isn't
 * always called.
 *
 * @author Jan Hoskens
 *
 */
public interface NewFormObjectAware
{

    /**
     * Specific method to use when setting a new formObject. This can be <code>null</code>, a base object
     * with defaults or a copy of an existing object.
     *
     * @param formObject
     *            the new form object to set.
     */
    void setNewFormObject(Object formObject);
}

