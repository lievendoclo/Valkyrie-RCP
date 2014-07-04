package org.valkyriercp.binding.form.support;

import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FieldFace;
import org.valkyriercp.binding.form.FieldFaceSource;

import java.util.HashMap;
import java.util.Map;

/**
 * A convenience superclass for FieldFaceSource's that require caching to improve the performance of FieldFace lookup.
 *
 * <p>
 * FieldFace retrieval is delegated to subclasses using the {@link #loadFieldFace(String, Object)} method.
 *
 * @author Oliver Hutchison
 * @author Mathias Broekelmann
 */
public abstract class CachingFieldFaceSource implements FieldFaceSource {

    private static final Object DEFAULT_CONTEXT = new Object();

    /*
     * A cache with context keys and Map from field to FieldFace values. The keys are held with week references so this
     * class will not prevent GC of context instances.
     */
    private final AbstractCachingMapDecorator cachedFieldFaceDescriptors = new AbstractCachingMapDecorator(true) {
        public Object create(Object key) {
            return new HashMap();
        }
    };

    protected CachingFieldFaceSource() {
    }

    public FieldFace getFieldFace(String field) {
        return getFieldFace(field, null);
    }

    public FieldFace getFieldFace(final String field, final Object context) {
        Map faceDescriptors = (Map) cachedFieldFaceDescriptors.get(context == null ? DEFAULT_CONTEXT : context);
        FieldFace fieldFaceDescriptor = (FieldFace) faceDescriptors.get(field);
        if (fieldFaceDescriptor == null) {
            fieldFaceDescriptor = loadFieldFace(field, context);
            Assert.notNull(fieldFaceDescriptor, "FieldFace must not be null.");
            faceDescriptors.put(field, fieldFaceDescriptor);
        }
        return fieldFaceDescriptor;
    }

    /**
     * Loads the FieldFace for the given field path and context id. This value will be cached so performance need not be
     * a concern of this method.
     *
     * @param field
     *            the form field path
     * @param context
     *            optional context for which the FieldFace is being resolved
     * @return the FieldFace for the given context id (never null).
     */
    protected abstract FieldFace loadFieldFace(String field, Object context);
}