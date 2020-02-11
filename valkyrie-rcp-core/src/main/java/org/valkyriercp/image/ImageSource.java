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

import java.awt.*;

/**
 * A interface for retrieving images by key. The key abstracts away the path to
 * the image resource, effectively acting as an alias. For example, the key
 * <code>wizard.pageImage</code> may map to a image resource in the classpath
 * at <code>/images/wizard/title_banner.gif</code>
 * <p>
 * <p>
 * Image source implementations are responsible for resolving the underlying
 * image resource and loading it into memory. They may also cache image
 * resources.
 *
 * @author Keith Donald
 */
public interface ImageSource {

    /**
     * Loads the image with the specified key. Caching may or may not be
     * performed.
     * <p>
     * <p>
     * If the load is successful, the image resource is returned. If the load
     * fails, a broken image indicator is returned if it is set for this source.
     * If not set, an exception is thrown.
     *
     * @param key
     *            The image key
     * @return The image.
     * @throws NoSuchImageResourceException,
     *             if no image could be found and no broken image indicator is
     *             set.
     */
    public Image getImage(String key);

    /**
     * Returns the image resource indexed by the specified key. A resource is a
     * lightweight pointer to the image in the classpath, filesystem, or
     * network, and not the actual loaded image itself.
     *
     * @param key
     *            The image key.
     * @return The image resource.
     */
    public AwtImageResource getImageResource(String key);

    boolean hasImageFor(Object key);
}
