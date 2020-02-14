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

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
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
    private final Cache<Object, Map<String, FieldFace>> cachedFieldFaceDescriptors = new Cache2kBuilder<Object, Map<String, FieldFace>>() {}
            .loader(key -> new HashMap<>())
            .build();

    protected CachingFieldFaceSource() {
    }

    public FieldFace getFieldFace(String field) {
        return getFieldFace(field, null);
    }

    public FieldFace getFieldFace(final String field, final Object context) {
        Map faceDescriptors = cachedFieldFaceDescriptors.get(context == null ? DEFAULT_CONTEXT : context);
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