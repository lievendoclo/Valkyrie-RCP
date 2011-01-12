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

