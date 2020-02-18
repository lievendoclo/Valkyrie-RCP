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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * The default implementation of ImageIconRegistry. This implementation caches
 * all icons using soft references (TODO it just lazy loads them, but it doesn't use SoftReference).
 * More specifically, cached icons will remain
 * in memory unless there is a shortage of resources in the system.
 *
 * @author Keith Donald
 */
public class DefaultIconSource implements IconSource {
    protected static final Log logger = LogFactory.getLog(DefaultIconSource.class);

    private final ImageSource imageSource;
    private final Cache<String, ImageIcon> cache;

    public DefaultIconSource(ImageSource imageSource) {
        this.imageSource = imageSource;
        cache = new Cache2kBuilder<String, ImageIcon>() {}
                .loader(key -> {
                    Image image = imageSource.getImage(key);
                    return new ImageIcon(image);
                })
                .build();
    }

    public Icon getIcon(String key) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Resolving icon with key '" + key + "'");
            }
            if(imageSource.hasImageFor(key)) {
                return cache.get(key);
            } else {
                return null;
            }

        }
        catch (NoSuchImageResourceException e) {
            if (logger.isInfoEnabled()) {
                logger.info("No image resource found for icon with key '" + key + "'; returning a <null> icon.");
            }
            return null;
        }
    }

    public void clear() {
        cache.clear();
    }
}
