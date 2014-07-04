package org.valkyriercp.image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.collection.AbstractCachingMapDecorator;

import javax.annotation.PostConstruct;
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
@org.springframework.stereotype.Component
public class DefaultIconSource implements IconSource {
    protected static final Log logger = LogFactory.getLog(DefaultIconSource.class);

    private IconCache cache;

    @Autowired
    private ImageSource imageSource;

    @PostConstruct
    protected void initIconCache() {
        this.cache = new IconCache(imageSource);
    }

    public Icon getIcon(String key) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Resolving icon with key '" + key + "'");
            }
            return (ImageIcon)cache.get(key);
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

    protected String doProcessImageKeyBeforeLookup(String key) {
        // subclasses can override
        return key;
    }

    protected IconCache cache() {
        return cache;
    }

    /**
     * Icon cache using soft references.
     *
     * @author Keith Donald
     */
    protected static class IconCache extends AbstractCachingMapDecorator {
        private ImageSource images;

        public IconCache(ImageSource images) {
            super(true);
            this.images = images;
        }

        public Object create(Object key) {
            Image image = images.getImage((String)key);
            return new ImageIcon(image);
        }

        public ImageSource images() {
            return images;
        }
    }
}