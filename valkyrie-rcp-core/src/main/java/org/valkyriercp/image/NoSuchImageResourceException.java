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
package org.valkyriercp.image;

import org.springframework.core.io.Resource;

/**
 * Indicates that a image resource could not be found from an underlying data
 * source.
 *
 * @author Keith Donald
 */
public class NoSuchImageResourceException extends RuntimeException {
    private Object imageKey;

    /**
     * Creates an exception message indicating the specified imageKey did not
     * map to a valid resource.
     *
     * @param imageKey
     *            The unknown image key.
     */
    public NoSuchImageResourceException(Object imageKey) {
        super();
        this.imageKey = imageKey;
    }

    /**
     * Creates an exception message indicating the specified imageKey did not
     * map to a valid resource. A cause is provided.
     *
     * @param imageKey
     *            The unknown image key.
     * @param cause
     *            The reason the resource was not found.
     */
    public NoSuchImageResourceException(Object imageKey, Throwable cause) {
        super(cause);
        this.imageKey = imageKey;
    }

    public String getMessage() {
        if (Resource.class.isInstance(imageKey))
            return "No image at resource '" + imageKey + "' exists.";

        return "No image with key '" + imageKey + "' exists in source bundle.";
    }

    public String toString() {
        return getMessage();
    }

}
