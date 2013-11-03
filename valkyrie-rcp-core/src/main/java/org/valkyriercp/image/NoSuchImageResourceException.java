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
