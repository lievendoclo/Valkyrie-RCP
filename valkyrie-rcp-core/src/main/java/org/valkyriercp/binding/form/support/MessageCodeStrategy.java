package org.valkyriercp.binding.form.support;

/**
 * MessageKeyStrategy is used by {@link MessageSourceFieldFaceSource} to create the codes for resolving messages.
 *
 * @author Mathias Broekelmann
 *
 */
public interface MessageCodeStrategy {

    /**
     * Creates message codes.
     *
     * @param contextId
     *            optional contextId of the field.
     * @param field
     *            the field. The field name
     * @param suffixes
     *            optional array of suffixes.
     * @return an array of message codes
     */
    String[] getMessageCodes(String contextId, String field, String[] suffixes);

}